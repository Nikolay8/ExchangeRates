package com.example.nikolay.exchangerates.Current_Rate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Nikolay on 18.07.2017.
 */

public interface ExchangeApi {
    @GET("pubinfo?json&exchange&coursid=3")
    Call<List<JsonModel>> getData();
}
