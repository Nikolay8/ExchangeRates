package com.example.nikolay.exchangerates;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nikolay.exchangerates.Network.ApiController;
import com.example.nikolay.exchangerates.Models.JsonModel;
import com.example.nikolay.exchangerates.Helpers.DBHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";
    private static final int NOTIFICATION_ID = 1;
    private String ccy_0, buy_0, sell_0, ccy_2, buy_2, sell_2;
    private SQLiteDatabase db;

    @BindView(R.id.textView_eur)
    TextView textView_eur;
    @BindView(R.id.textView_rur)
    TextView textView_rur;
    @BindView(R.id.textView_usd)
    TextView textView_usd;
    @BindView(R.id.textView_btc)
    TextView textView_btc;
    @BindView(R.id.button_exchange)
    Button button_exchange;
    @BindView(R.id.button_chart)
    Button button_chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = new DBHelper(this).getWritableDatabase();

        button_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExchangeHistoryActivity.class);
                startActivity(intent);
            }
        });

        button_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent2);
            }
        });

        ApiController.getApi().getCurrentExchangeRate().enqueue(new Callback<List<JsonModel>>() {
            @Override
            public void onResponse(Call<List<JsonModel>> call, Response<List<JsonModel>> response) {
                ContentValues cv = new ContentValues();

                //EUR
                ccy_0 = response.body().get(0).getCcy();
                buy_0 = response.body().get(0).getBuy();
                sell_0 = response.body().get(0).getSale();
                cv.put("eur_buy", buy_0);
                cv.put("eur_sell", sell_0);
                textView_eur.setText(ccy_0 + " " + "\nBuy " + buy_0 + " " + "\nSell " + sell_0);
                Log.d(TAG, ccy_0 + " " + buy_0 + " " + sell_0);

                //RUR
                String ccy_1 = response.body().get(1).getCcy();
                String buy_1 = response.body().get(1).getBuy();
                String sell_1 = response.body().get(1).getSale();
                cv.put("rur_buy", buy_1);
                cv.put("rur_sell", sell_1);
                textView_rur.setText("\n" + ccy_1 + " " + "\nBuy " + buy_1 + " " + "\nSell " + sell_1);
                Log.d(TAG, ccy_1 + " " + buy_1 + " " + sell_1);

                //USD
                ccy_2 = response.body().get(2).getCcy();
                buy_2 = response.body().get(2).getBuy();
                sell_2 = response.body().get(2).getSale();
                cv.put("usd_buy", buy_2);
                cv.put("usd_sell", sell_2);
                textView_usd.setText("\n" + ccy_2 + " " + "\nBuy " + buy_2 + " " + "\nSell " + sell_2);
                Log.d(TAG, ccy_2 + " " + buy_2 + " " + sell_2);

                //BTC
                String ccy_3 = response.body().get(3).getCcy();
                String buy_3 = response.body().get(3).getBuy();
                String sell_3 = response.body().get(3).getSale();
                cv.put("btc_buy", buy_3);
                cv.put("btc_sell", sell_3);
                textView_btc.setText("\n" + ccy_3 + " " + "\nBuy " + buy_3 + " " + "\nSell " + sell_3);
                Log.d(TAG, ccy_3 + " " + buy_3 + " " + sell_3);

                sendNotification();
                db.insert("current_data", null, cv);
                db.close();
            }

            @Override
            public void onFailure(Call<List<JsonModel>> call, Throwable t) {
                Log.d(TAG, "Log=" + t);
                Cursor c = db.query("current_data", null, null, null, null, null, null);
                c.moveToLast();
                int eur_buy_Index = c.getColumnIndex("eur_buy");
                int eur_sell_Index = c.getColumnIndex("eur_sell");
                int rur_buy_Index = c.getColumnIndex("rur_buy");
                int rur_sell_Index = c.getColumnIndex("rur_sell");
                int usd_buy_Index = c.getColumnIndex("usd_buy");
                int usd_sell_Index = c.getColumnIndex("usd_sell");
                int btc_buy_Index = c.getColumnIndex("btc_buy");
                int btc_sell_Index = c.getColumnIndex("btc_sell");

                textView_eur.setText("EUR(saved data)" + " " + "\nBuy " + c.getString(eur_buy_Index) + " " + "\nSell "
                        + c.getString(eur_sell_Index));
                textView_rur.setText("\nRUR(saved data)" + " " + "\nBuy " + c.getString(rur_buy_Index) + " " + "\nSell "
                        + c.getString(rur_sell_Index));
                textView_usd.setText("\nUSD(saved data)" + " " + "\nBuy " + c.getString(usd_buy_Index) + " " + "\nSell "
                        + c.getString(usd_sell_Index));
                textView_btc.setText("\nBTC(saved data)" + " " + "\nBuy " + c.getString(btc_buy_Index) + " " + "\nSell "
                        + c.getString(btc_sell_Index));

                c.close();
                db.close();
            }
        });
    }

    public void sendNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Exchange Rate")
                .setContentText(ccy_0 + " " + "Buy " + buy_0 + " " + "Sell " + sell_0 +
                        ccy_2 + " " + "Buy " + buy_2 + " " + "Sell " + sell_2);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ExchangeHistoryActivity.class);
        Intent resultIntent = new Intent(this, ExchangeHistoryActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
