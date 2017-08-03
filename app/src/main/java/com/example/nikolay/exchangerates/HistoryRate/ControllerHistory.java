package com.example.nikolay.exchangerates.HistoryRate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nikolay on 18.07.2017.
 */

public class ControllerHistory {
    //https://api.privatbank.ua/p24api/exchange_rates?json&date=20.01.2015
    static final String BASE_URL="https://api.privatbank.ua/p24api/";

    public static ExchangeApiHistory getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ExchangeApiHistory exchangeApiHistory = retrofit.create(ExchangeApiHistory.class);
        return exchangeApiHistory;
    }
}
