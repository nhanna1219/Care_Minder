package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

public class AccountActivity extends AppCompatActivity {
    TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back = findViewById(R.id.settingsTextView);
        setContentView(R.layout.activity_account);

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}