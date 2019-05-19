package com.example.tinkoffnews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

//Класс для получения и десерализации ответа от сервера
public class News {
    @SerializedName("payload")
    @Expose
    private Object items;


    Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }
}
