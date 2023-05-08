package com.example.careminder.Activity.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.Activity.Notification.NotificationActivity;
import com.example.careminder.Activity.Statistic.StatisticActivity;
import com.example.careminder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

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
        ImageView editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        //Bottom Navigation

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.setting_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int HOME_NAV_ID = R.id.home_nav;
                final int STATISTIC_NAV_ID = R.id.statistic_nav;
                final int NOTIFY_NAV_ID = R.id.notify_nav;
                final int SETTING_NAV_ID = R.id.setting_nav;
                switch (item.getItemId()){
                    case HOME_NAV_ID:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case STATISTIC_NAV_ID:
                        startActivity(new Intent(getApplicationContext(), StatisticActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case NOTIFY_NAV_ID:
                        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case SETTING_NAV_ID:
                        return true;
                }
                return false;
            }});
    }


    private List<SettingItem> createSettingItems() {
        List<SettingItem> settingItems = new ArrayList<>();


        settingItems.add(new SettingItem("Account",R.drawable.sic_account));
        settingItems.add(new SettingItem("Notification",R.drawable.sic_notification));
        settingItems.add(new SettingItem("Advanced Settings",R.drawable.sic_volume));
        settingItems.add(new SettingItem("Privacy & Security",R.drawable.sic_security));
        settingItems.add(new SettingItem("Log out", R.drawable.sic_logout));
        settingItems.add(new SettingItem("Help",R.drawable.sic_globe));
        return settingItems;
    }
}