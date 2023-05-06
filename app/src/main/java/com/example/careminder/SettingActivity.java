package com.example.careminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.os.Bundle;
import android.widget.ImageView;

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
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<SettingItem> settingItems = createSettingItems();


        settingAdapter = new SettingAdapter(settingItems);
        recyclerView.setAdapter(settingAdapter);
    }

    private List<SettingItem> createSettingItems() {
        List<SettingItem> settingItems = new ArrayList<>();


        settingItems.add(new SettingItem("Account",R.drawable.sic_account));
        settingItems.add(new SettingItem("Notification",R.drawable.ic_notification));
        settingItems.add(new SettingItem("Advanced Settings",R.drawable.sic_volume));
        settingItems.add(new SettingItem("Privacy & Security",R.drawable.sic_security));
        settingItems.add(new SettingItem("Log out", R.drawable.sic_logout));
        settingItems.add(new SettingItem("Help",R.drawable.sic_globe));

        return settingItems;
    }
}