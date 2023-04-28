package com.example.careminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.careminder.utils.Home_Adapter;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class HomeActivity extends AppCompatActivity {
    ListView listView;
    Home_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = findViewById(R.id.home_listview);
        adapter = new Home_Adapter(getApplicationContext());
        listView.setAdapter(adapter);
    }
}