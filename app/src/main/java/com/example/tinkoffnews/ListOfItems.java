package com.example.tinkoffnews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

//Класс для получения и десерализации ответа от сервера
public class ListOfItems {
    @SerializedName("payload")
    @Expose
    private List<LinkedTreeMap> items;


    List<LinkedTreeMap> getItems() {
        return items;
    }

    public void setItems(List<LinkedTreeMap> items) {
        this.items = items;
    }
}
