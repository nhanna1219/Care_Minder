package com.example.careminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.careminder.utils.SettingAdapter;
import com.example.careminder.utils.SettingItem;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SettingAdapter settingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        recyclerView = findViewById(R.id.recyclerView);


//        int numColumns = 2;
//        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<SettingItem> settingItems = createSettingItems();


        settingAdapter = new SettingAdapter(settingItems);
        recyclerView.setAdapter(settingAdapter);
    }

    private List<SettingItem> createSettingItems() {
        List<SettingItem> settingItems = new ArrayList<>();


        settingItems.add(new SettingItem("Account"));
        settingItems.add(new SettingItem("Notification"));
        settingItems.add(new SettingItem("Advanced Settings"));
        settingItems.add(new SettingItem("Privacy & Security"));
        settingItems.add(new SettingItem("Log out"));
        settingItems.add(new SettingItem("Help"));

        return settingItems;
    }
}