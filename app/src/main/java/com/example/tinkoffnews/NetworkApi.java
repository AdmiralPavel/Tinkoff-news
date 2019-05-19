package com.example.tinkoffnews;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Singleton для связи с сервером
class NetworkApi {
    private static NetworkApi instance;
    private Retrofit retrofit;
    private static final String BASE_URL = "https://api.tinkoff.ru/";

    private NetworkApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static NetworkApi getInstance() {
        if (instance == null) {
            instance = new NetworkApi();
        }
        return instance;
    }

    TinkoffApi getTinkoffApi() {
        return retrofit.create(TinkoffApi.class);
    }
}
