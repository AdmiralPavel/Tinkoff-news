package com.example.tinkoffnews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Интерфейс с запросами для Tinkoff
public interface TinkoffApi {
    @GET("v1/news")
    Call<ListOfItems> getListOfItems();
    @GET("v1/news_content/")
    Call<News> getItem(@Query("id") String id);
}
