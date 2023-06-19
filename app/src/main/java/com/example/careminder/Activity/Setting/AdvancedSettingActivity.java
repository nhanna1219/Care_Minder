package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

public class AdvancedSettingActivity extends AppCompatActivity {

    TextView back, deleteData, checkUpdate, aboutCareMinder, policy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_setting);
        back = findViewById(R.id.settingsTextView);
        checkUpdate = findViewById(R.id.textView4);
        deleteData = findViewById(R.id.textView2);
        aboutCareMinder = findViewById(R.id.textView5);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvancedSettingActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvancedSettingActivity.this, DeleteData.class);
                startActivity(intent);
            }
        });

        checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvancedSettingActivity.this, CheckUpdateActivity.class);
                startActivity(intent);
            }
        });

        aboutCareMinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvancedSettingActivity.this, AboutCareMinderActivity.class);
                startActivity(intent);
            }
        });
    }
}