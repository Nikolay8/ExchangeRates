package com.example.nikolay.exchangerates.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nikolay on 18.07.2017.
 */

public class ApiController {
    static final String BASE_URL="https://api.privatbank.ua/p24api/";

    public static ExchangeApi getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ExchangeApi.class);
    }
}
