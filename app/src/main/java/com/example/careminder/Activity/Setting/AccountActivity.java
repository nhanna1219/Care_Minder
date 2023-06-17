package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

public class AccountActivity extends AppCompatActivity {
    TextView back;
    EditText height, weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        back = findViewById(R.id.settingsTextView);

        height = findViewById(R.id.textView12);
        weight = findViewById(R.id.textView11);
        // convert height and weight to double
        Double height_db = Double.parseDouble(height.getText().toString());
        Double weight_db = Double.parseDouble(weight.getText().toString());


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setBasicIn4(height_db, weight_db);
                Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setBasicIn4(Double height, Double weight){
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity writeBasicIn4 = new PermissionsRationaleActivity();
        writeBasicIn4.writeBasicInformation(healthConnectClient, height, weight);
    }
}