package com.example.careminder.Activity.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.careminder.Activity.Notification.NotificationActivity;
import com.example.careminder.Activity.Setting.SettingActivity;
import com.example.careminder.Activity.Statistic.StatisticActivity;
import com.example.careminder.R;
import com.example.careminder.utils.Home_Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class HomeActivity extends AppCompatActivity {
    ListView listView;
    Home_Adapter adapter;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = findViewById(R.id.home_listview);
        adapter = new Home_Adapter(getApplicationContext());
        listView.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int HOME_NAV_ID = R.id.home_nav;
                final int STATISTIC_NAV_ID = R.id.statistic_nav;
                final int NOTIFY_NAV_ID = R.id.notify_nav;
                final int SETTING_NAV_ID = R.id.setting_nav;
                switch (item.getItemId()){
                    case HOME_NAV_ID:
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
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}