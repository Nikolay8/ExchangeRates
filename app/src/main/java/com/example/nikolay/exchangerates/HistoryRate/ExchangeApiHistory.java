package com.example.nikolay.exchangerates.HistoryRate;

import com.example.nikolay.exchangerates.Current_Rate.JsonModel;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nikolay on 18.07.2017.
 */

public interface ExchangeApiHistory {
    @GET("exchange_rates?json&")
    Call<JsonModelHistory> getData(@Query("date") String satdate);

}
