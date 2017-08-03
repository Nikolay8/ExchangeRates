package com.example.nikolay.exchangerates;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikolay.exchangerates.HistoryRate.ControllerHistory;
import com.example.nikolay.exchangerates.HistoryRate.ExchangeApiHistory;
import com.example.nikolay.exchangerates.HistoryRate.JsonModelHistory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExchangeHistory extends AppCompatActivity {
    private static final String TAG="myLogs";
    private static ExchangeApiHistory exchangeApiHistory;
    int DIALOG_DATE=1;
    int myYear=2015;
    int myMonth=0;
    int myDay=01;
    private TextView textView_eur_his,textView_rur_his,textView_usd_his,textView_info;

    protected Dialog onCreateDialog(int id){
        if (id==DIALOG_DATE){
            DatePickerDialog tpd=new DatePickerDialog(this,myCallBack,myYear,myMonth,myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    OnDateSetListener myCallBack=new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myYear=year;
            myMonth=monthOfYear+1;
            myDay=dayOfMonth;

            if (myYear<2015){
                Toast toast=Toast.makeText(ExchangeHistory.this,
                        "You can view the 2015 minimum.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            } else {

                exchangeApiHistory = ControllerHistory.getApi();
                final String totalDate = myDay + "." + myMonth + "." + myYear;
                exchangeApiHistory.getData(totalDate).enqueue(new Callback<JsonModelHistory>() {

                    @Override
                    public void onResponse(Call<JsonModelHistory> call, Response<JsonModelHistory> response) {
                        textView_info.setText("Exchange Rate on " + response.body().getDate());

                        String ccy_0 = response.body().getExchangeRate().get(17).getCurrency();
                        String buy_0 = response.body().getExchangeRate().get(17).getPurchaseRate().toString();
                        String sell_0 = response.body().getExchangeRate().get(17).getSaleRate().toString();
                        textView_eur_his.setText("\n" + ccy_0 + " " + "\nBuy " + buy_0 + " " + "\nSell " + sell_0);
                        Log.d(TAG, ccy_0 + " " + buy_0 + " " + sell_0);

                        String ccy_1 = response.body().getExchangeRate().get(13).getCurrency();
                        String buy_1 = response.body().getExchangeRate().get(13).getPurchaseRate().toString();
                        String sell_1 = response.body().getExchangeRate().get(13).getSaleRate().toString();
                        textView_rur_his.setText("\n" + ccy_1 + " " + "\nBuy " + buy_1 + " " + "\nSell " + sell_1);
                        Log.d(TAG, ccy_1 + " " + buy_1 + " " + sell_1);

                        String ccy_2 = response.body().getExchangeRate().get(15).getCurrency();
                        String buy_2 = response.body().getExchangeRate().get(15).getPurchaseRate().toString();
                        String sell_2 = response.body().getExchangeRate().get(15).getSaleRate().toString();
                        textView_usd_his.setText("\n" + ccy_2 + " " + "\nBuy " + buy_2 + " " + "\nSell " + sell_2);
                        Log.d(TAG, ccy_2 + " " + buy_2 + " " + sell_2);
                    }

                    @Override
                    public void onFailure(Call<JsonModelHistory> call, Throwable t) {
                        Log.d(TAG, "Log=" + t);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_history);
        textView_info=(TextView)findViewById(R.id.textView_info);
        textView_eur_his=(TextView)findViewById(R.id.textView_eur_his);
        textView_rur_his=(TextView)findViewById(R.id.textView_rur_his);
        textView_usd_his=(TextView)findViewById(R.id.textView_usd_his);
        showDialog(DIALOG_DATE);
    }
}
