package com.example.tinkoffnews;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
        TextView title = findViewById(R.id.title);
        TextView text = findViewById(R.id.text);
        title.setText((String)getIntent().getExtras().get("title"));
        text.setText(Html.fromHtml((String)getIntent().getExtras().get("text")));

    }
}
