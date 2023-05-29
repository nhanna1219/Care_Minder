package com.example.careminder.Activity.Introduction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.careminder.Activity.HealthConnect.HC_Permission;
import com.example.careminder.Activity.Information.InformationActivity;
import com.example.careminder.R;

public class IntroductionActivity4 extends AppCompatActivity {
    ImageButton next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction4);

        next = (ImageButton) findViewById(R.id.intro_btn3);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HC_Permission.class));
            }
        });

    }
}