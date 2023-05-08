package com.example.careminder.Activity.Notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.Activity.Setting.SettingActivity;
import com.example.careminder.Activity.Statistic.StatisticActivity;
import com.example.careminder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NotificationActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.notify_nav);

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