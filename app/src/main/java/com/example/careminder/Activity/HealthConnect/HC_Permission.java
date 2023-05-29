package com.example.careminder.Activity.HealthConnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

public class HC_Permission extends AppCompatActivity {
    Button btn_continue, btn_notNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc_permission);

        btn_continue = findViewById(R.id.btn_continue_HC);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HealthConnect.class));
            }
        });

        btn_notNow = findViewById(R.id.btn_not_now_HC);
        btn_notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });


    }
}