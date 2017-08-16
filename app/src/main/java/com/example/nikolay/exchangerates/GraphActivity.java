package com.example.nikolay.exchangerates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.nikolay.exchangerates.Helpers.DBHelper;
import com.example.nikolay.exchangerates.HistoryRate.ControllerHistory;
import com.example.nikolay.exchangerates.HistoryRate.ExchangeApiHistory;
import com.example.nikolay.exchangerates.Models.JsonModelHistory;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Graph extends AppCompatActivity {
    private static final String TAG="myLogs";
    private static ExchangeApiHistory exchangeApiHistory;
    private Integer dayOfYear_prev,dayOfYear,mYear,mMonth,mDay;
    private String totalDate;
    private TextView info2;
    private String ccy_0,buy_0, buy_1;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        info2=(TextView)findViewById(R.id.textView_info2);

        dbHelper=new DBHelper(this);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("graph_data", null, null);
        db.close();

        //30 Days from current Date
        final ArrayList dataList=new ArrayList();
        final Calendar c=Calendar.getInstance();
        dayOfYear_prev=c.get(Calendar.DAY_OF_YEAR)-7;

        for(int i=0;i<30;i++) {
            dayOfYear = dayOfYear_prev-1;
            dayOfYear_prev=dayOfYear_prev-1;
            c.set(Calendar.DAY_OF_YEAR, dayOfYear);
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH) + 1;
            mDay = c.get(Calendar.DAY_OF_MONTH);
            totalDate = mDay + "." + mMonth + "." + mYear;
            dataList.add(totalDate);
        }

        final ArrayList<Double>EuroList=new ArrayList<>();
        final ArrayList<Double>EuroList_sorted=new ArrayList<>();
        final ArrayList<Double>UsdList_sorted=new ArrayList<>();
        exchangeApiHistory = ControllerHistory.getApi();

        for (int x=0;x<30;x++) {
            exchangeApiHistory.getData(dataList.get(x).toString()).enqueue(new Callback<JsonModelHistory>() {
                @Override
                public void onResponse(Call<JsonModelHistory> call, Response<JsonModelHistory> response) {
                    ContentValues cv=new ContentValues();
                    SQLiteDatabase db=dbHelper.getWritableDatabase();
                    ccy_0=response.body().getDate().toString();
                    buy_0=response.body().getExchangeRate().get(17).getPurchaseRate().toString();
                    buy_1=response.body().getExchangeRate().get(15).getPurchaseRate().toString();
                    cv.put("data",ccy_0);
                    cv.put("eur_buy",buy_0);
                    cv.put("usd_buy",buy_1);
                    db.insert("graph_data",null,cv);
                    Double convert_Int=Double.parseDouble(buy_0);
                    EuroList.add(convert_Int);
                    db.close();

                    if (EuroList.size()>29) {
                       SQLiteDatabase db1=dbHelper.getReadableDatabase();
                        Cursor c=db1.query("graph_data",null,null,null,null,null,"data ASC");
                        c.moveToFirst();
                        int eur_buy_Index = c.getColumnIndex("eur_buy");
                        int usd_buy_Index=c.getColumnIndex("usd_buy");
                        EuroList_sorted.add(c.getDouble(eur_buy_Index));
                        UsdList_sorted.add(c.getDouble(usd_buy_Index));

                        for (int d=0;d<29;d++) {
                            c.moveToNext();
                            int eur_buy_Index1 = c.getColumnIndex("eur_buy");
                            int usd_buy_Index1=c.getColumnIndex("usd_buy");
                            EuroList_sorted.add(c.getDouble(eur_buy_Index1));
                            UsdList_sorted.add(c.getDouble(usd_buy_Index1));
                        }

                        Log.d(TAG,"Euro_list: "+EuroList_sorted.toString());
                        Log.d(TAG,"Usd_list: "+UsdList_sorted.toString());

                        GraphView graph = (GraphView) findViewById(R.id.graph);
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(1, EuroList_sorted.get(0)),
                                new DataPoint(2, EuroList_sorted.get(1)),
                                new DataPoint(3, EuroList_sorted.get(2)),
                                new DataPoint(4, EuroList_sorted.get(3)),
                                new DataPoint(5, EuroList_sorted.get(4)),
                                new DataPoint(6, EuroList_sorted.get(5)),
                                new DataPoint(7, EuroList_sorted.get(6)),
                                new DataPoint(8, EuroList_sorted.get(7)),
                                new DataPoint(9, EuroList_sorted.get(8)),
                                new DataPoint(10, EuroList_sorted.get(9)),
                                new DataPoint(11, EuroList_sorted.get(10)),
                                new DataPoint(12, EuroList_sorted.get(11)),
                                new DataPoint(13, EuroList_sorted.get(12)),
                                new DataPoint(14, EuroList_sorted.get(13)),
                                new DataPoint(15, EuroList_sorted.get(14)),
                                new DataPoint(16, EuroList_sorted.get(15)),
                                new DataPoint(17, EuroList_sorted.get(16)),
                                new DataPoint(18, EuroList_sorted.get(17)),
                                new DataPoint(19, EuroList_sorted.get(18)),
                                new DataPoint(20, EuroList_sorted.get(19)),
                                new DataPoint(21, EuroList_sorted.get(20)),
                                new DataPoint(22, EuroList_sorted.get(21)),
                                new DataPoint(23, EuroList_sorted.get(22)),
                                new DataPoint(24, EuroList_sorted.get(23)),
                                new DataPoint(25, EuroList_sorted.get(24)),
                                new DataPoint(26, EuroList_sorted.get(25)),
                                new DataPoint(27, EuroList_sorted.get(26)),
                                new DataPoint(28, EuroList_sorted.get(27)),
                                new DataPoint(29, EuroList_sorted.get(28)),
                                new DataPoint(30, EuroList_sorted.get(29)),
                        });
                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(1, UsdList_sorted.get(0)),
                                new DataPoint(2, UsdList_sorted.get(1)),
                                new DataPoint(3, UsdList_sorted.get(2)),
                                new DataPoint(4, UsdList_sorted.get(3)),
                                new DataPoint(5, UsdList_sorted.get(4)),
                                new DataPoint(6, UsdList_sorted.get(5)),
                                new DataPoint(7, UsdList_sorted.get(6)),
                                new DataPoint(8, UsdList_sorted.get(7)),
                                new DataPoint(9, UsdList_sorted.get(8)),
                                new DataPoint(10, UsdList_sorted.get(9)),
                                new DataPoint(11, UsdList_sorted.get(10)),
                                new DataPoint(12, UsdList_sorted.get(11)),
                                new DataPoint(13, UsdList_sorted.get(12)),
                                new DataPoint(14, UsdList_sorted.get(13)),
                                new DataPoint(15, UsdList_sorted.get(14)),
                                new DataPoint(16, UsdList_sorted.get(15)),
                                new DataPoint(17, UsdList_sorted.get(16)),
                                new DataPoint(18, UsdList_sorted.get(17)),
                                new DataPoint(19, UsdList_sorted.get(18)),
                                new DataPoint(20, UsdList_sorted.get(19)),
                                new DataPoint(21, UsdList_sorted.get(20)),
                                new DataPoint(22, UsdList_sorted.get(21)),
                                new DataPoint(23, UsdList_sorted.get(22)),
                                new DataPoint(24, UsdList_sorted.get(23)),
                                new DataPoint(25, UsdList_sorted.get(24)),
                                new DataPoint(26, UsdList_sorted.get(25)),
                                new DataPoint(27, UsdList_sorted.get(26)),
                                new DataPoint(28, UsdList_sorted.get(27)),
                                new DataPoint(29, UsdList_sorted.get(28)),
                                new DataPoint(30, UsdList_sorted.get(29)),
                        });

                        info2.setText("Download complete");
                        graph.getViewport().setScalable(true);
                        graph.getViewport().setYAxisBoundsManual(true);
                        graph.getViewport().setMinX(1.0);
                        graph.getViewport().setMaxX(30.0);
                        graph.getViewport().setMinY(20.0);
                        graph.getViewport().setMaxY(40.0);
                        series.setTitle("UAH/EUR");
                        series2.setTitle("UAH/USD");
                        series2.setColor(Color.GREEN);
                        graph.addSeries(series);
                        graph.addSeries(series2);
                        graph.getLegendRenderer().setVisible(true);
                        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

                        c.close();
                        db1.close();
                    }
                }
                @Override
                public void onFailure(Call<JsonModelHistory> call, Throwable t) {
                    Log.d(TAG, "Log=" + t);
                }
            });
        }
    }
}
