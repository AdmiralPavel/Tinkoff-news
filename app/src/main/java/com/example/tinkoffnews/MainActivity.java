package com.example.tinkoffnews;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener{
    RecyclerViewAdapter adapter;
    //Список, полученный от сервера
    ListOfItems list;
    //Список с заголовками
    List<String> topicNames;
    SwipeRefreshLayout swipeRefreshLayout;

    //Метод для получения кэшированного списка из ллокальной базы данных
    private void getFromDB() {
        topicNames = new ArrayList<>();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("topicNames.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS topicNames (name TEXT, id INTEGER)");
        Cursor cursor = db.rawQuery("SELECT * FROM topicNames;", null);
        //Бежим по БД, доставая данные
        if (cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                //Некоторые символы кодируются неверно, при получении строки вручную преобразуем их
                topicNames.add(cursor.getString(0).replace("&#39;", "\'")
                        .replace("&quot;", "\"")
                        .replace("&nbsp;", " ")
                        .replace("&laqyo;", "«")
                        .replace("&raquo;", "»")
                        .replace("&mdash;", "—")
                        .replace("&amp;", "&")
                        .replace("rsquo;", "’")
                        .replace("&reg;", "®")
                );
                cursor.moveToNext();
            }
        //В случае, если в локальной БД ничего нет, просим пользователя обновить
        if (topicNames.size() == 0)
            topicNames.add("You haven't download any data. Please swipe to refresh");
        cursor.close();
        db.close();
    }

    //Метод, использующийся для инициализации RecyclerView
    private void recyclerViewInitialize() {
        getFromDB();
        RecyclerView recyclerView = findViewById(R.id.topicNamesView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, topicNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this::sendRequests);
        sendRequests();
        recyclerViewInitialize();
    }

    //Метод для выполнения GET запроса StackOverflow
    private void sendRequests() {
        NetworkApi.getInstance()
                .getTinkoffApi()
                .getListOfItems()
                .enqueue(new Callback<ListOfItems>() {
                    //В случае получения ответа с сервера
                    @Override
                    public void onResponse(@NonNull Call<ListOfItems> call, @NonNull Response<ListOfItems> response) {
                        //Кэшируем наш ответ
                        list = response.body();
                        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("topicNames.db", MODE_PRIVATE, null);
                        db.execSQL("CREATE TABLE IF NOT EXISTS topicNames (name TEXT, id INTEGER)");
                        int i = 0;
                        db.execSQL("DELETE FROM topicNames");
                        for (LinkedTreeMap map : list.getItems()) {
                            db.execSQL("INSERT INTO topicNames VALUES ('" + ((String) map.get("text")).replace("'", "&#39;") + "', " + i++ + ");");
                        }

                        //Инициализируем recyclerView
                        recyclerViewInitialize();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    //Если проблемы с интернетом, показываем сообщение об этом
                    @Override
                    public void onFailure(@NonNull Call<ListOfItems> call, @NonNull Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Failure! Check your internet connection", Toast.LENGTH_SHORT);
                        toast.show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.putExtra("id", ((String) list.getItems().get(position).get("id")));
        startActivity(intent);
    }
}
