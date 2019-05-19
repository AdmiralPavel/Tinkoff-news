package com.example.tinkoffnews;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        NetworkApi.getInstance()
                .getTinkoffApi()
                .getItem(getIntent().getExtras().get("id").toString())
                .enqueue(new Callback<News>() {
                    //В случае получения ответа с сервера
                    @Override
                    public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
                        News list = response.body();
                        //((TextView)findViewById(R.id.title)).setText(list.getItems().get("text").toString());
                        /*((TextView)findViewById(R.id.text)).setText(list.getItems().get("content").toString().replace("&#39;", "\'")
                                .replace("&quot;", "\"")
                                .replace("&nbsp;", " ")
                                .replace("&laqyo;", "«")
                                .replace("&raquo;", "»")
                                .replace("&mdash;", "—")
                                .replace("&amp;", "&")
                                .replace("rsquo;", "’")
                                .replace("&reg;", "®"));*/
                    }

                    //Если проблемы с интернетом, показываем сообщение об этом
                    @Override
                    public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Failure! Check your internet connection", Toast.LENGTH_SHORT);
                        toast.show();
                        //swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
}
