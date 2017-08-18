package com.example.nikolay.exchangerates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nikolay.exchangerates.Helpers.DBHelper;
import com.example.nikolay.exchangerates.Models.ExchangeRate;
import com.example.nikolay.exchangerates.Models.JsonModelHistory;
import com.example.nikolay.exchangerates.Network.ApiController;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";
    private List<Double> usdPurchaseRates = new ArrayList<>();
    private List<Double> eurPurchaseRates = new ArrayList<>();
    private SQLiteDatabase database;

    @BindView(R.id.graph)
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);
        database = new DBHelper(this).getWritableDatabase();

        initPlot();
        int amountOfDays = 30;
        final List<String> dateList = getDateList(amountOfDays);
        for (int i = 0; i < dateList.size(); i++) {
            boolean existInDatabase = extractRatesFromDatabase(dateList.get(i));
            if (!existInDatabase) {
                ApiController.getApi().getExchangeRateHistory(dateList.get(i)).enqueue(new Callback<JsonModelHistory>() {
                    @Override
                    public void onResponse(Call<JsonModelHistory> call, Response<JsonModelHistory> response) {
                        JsonModelHistory result = response.body();
                        boolean existsOnServer = result.getExchangeRates() != null
                                && !result.getExchangeRates().isEmpty();
                        if (existsOnServer) {
                            Double usdPurchaseRate;
                            Double eurPurchaseRate;
                            ContentValues cv = new ContentValues();
                            cv.put("date", result.getDate());
                            for (ExchangeRate exchangeRate : result.getExchangeRates()) {
                                switch (exchangeRate.getCurrency()) {
                                    case "USD":
                                        usdPurchaseRate = exchangeRate.getPurchaseRate();
                                        usdPurchaseRates.add(usdPurchaseRate);
                                        cv.put("usd_buy", usdPurchaseRate);
                                        break;
                                    case "EUR":
                                        eurPurchaseRate = exchangeRate.getPurchaseRate();
                                        eurPurchaseRates.add(eurPurchaseRate);
                                        cv.put("eur_buy", eurPurchaseRate);
                                        break;
                                }
                            }
                            database.insert("graph_data", null, cv);
                            updatePlotData();
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonModelHistory> call, Throwable t) {
                        Log.d(TAG, "Error loading data: " + t);
                    }
                });
            }
        }
        updatePlotData();
    }

    private boolean extractRatesFromDatabase(String date) {
        Cursor cursor = database.query("graph_data", null, "date = '" + date + "'", null, null, null, "date ASC");
        if (cursor.moveToFirst()) {
            usdPurchaseRates.add(cursor.getDouble(cursor.getColumnIndex("usd_buy")));
            eurPurchaseRates.add(cursor.getDouble(cursor.getColumnIndex("eur_buy")));
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    private List<String> getDateList(int amountOfDays) {
        List<String> dataList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < amountOfDays; i++) {
            dataList.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DATE, -1);
        }
        return dataList;
    }

    private DataPoint[] getDataPoin(List<Double> rates) {
        DataPoint[] points = new DataPoint[rates.size()];
        for (int i = 0; i < rates.size(); i++) {
            int day = i + 1;
            double rate = rates.get(i);
            points[i] = new DataPoint(day, rate);
        }
        return points;
    }

    private void initPlot() {
        graph.getViewport().setScalable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(1.0);
        graph.getViewport().setMaxX(30.0);
        graph.getViewport().setMinY(20.0);
        graph.getViewport().setMaxY(40.0);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    private void updatePlotData() {
        LineGraphSeries<DataPoint> euroSeries = new LineGraphSeries<>(getDataPoin(eurPurchaseRates));
        LineGraphSeries<DataPoint> usdSeries = new LineGraphSeries<>(getDataPoin(usdPurchaseRates));
        graph.removeAllSeries();
        euroSeries.setTitle("UAH/EUR");
        usdSeries.setTitle("UAH/USD");
        usdSeries.setColor(Color.GREEN);
        graph.addSeries(euroSeries);
        graph.addSeries(usdSeries);
    }
}
