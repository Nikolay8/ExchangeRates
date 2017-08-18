package com.example.nikolay.exchangerates.Network;

import com.example.nikolay.exchangerates.Models.JsonModel;
import com.example.nikolay.exchangerates.Models.JsonModelHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nikolay on 18.07.2017.
 */

public interface ExchangeApi {
    @GET("pubinfo?json&exchange&coursid=3")
    Call<List<JsonModel>> getCurrentExchangeRate();

    @GET("exchange_rates?json&")
    Call<JsonModelHistory> getExchangeRateHistory(@Query("date") String satdate);
}
