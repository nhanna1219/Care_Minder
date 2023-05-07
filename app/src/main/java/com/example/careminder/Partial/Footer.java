package com.example.careminder.Partial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.careminder.HomeActivity;
import com.example.careminder.R;
import com.example.careminder.SettingActivity;

public class Footer extends AppCompatActivity {
private ImageButton setting, home, notify, statistic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footer_partial_view);

        home = (ImageButton) findViewById(R.id.home_nav);
        setting = (ImageButton) findViewById(R.id.setting_nav);
        notify = (ImageButton) findViewById(R.id.notify_nav);
        statistic = (ImageButton) findViewById(R.id.statistic_nav);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Footer.this, HomeActivity.class));
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Footer.this, SettingActivity.class));
            }
        });


        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}