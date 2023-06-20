package com.example.careminder.Activity.Body;

import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.careminder.Activity.HealthConnect.HealthConnect;
import com.example.careminder.Activity.HealthConnect.HealthConnectManagement;
import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.R;

public class BodyReportActivity extends AppCompatActivity {
    TextView weight, height, BMI, result, resultContent, weightAVG;
    ImageButton back;
    HealthConnectClient healthConnectClient;
    PermissionsRationaleActivity management;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_report);

        initializeVariable();

        setAVGWeight();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BodyActivity.class));
            }
        });
    }

    private void setAVGWeight() {
        management.readAggregateWeight(healthConnectClient, weightAVG);
        management.readBasicInformation(healthConnectClient, weight, height, BMI, result, resultContent);
    }

    private void initializeVariable(){
        healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        management = new PermissionsRationaleActivity();

        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        BMI = findViewById(R.id.bmi_result);
        result = findViewById(R.id.result);
        resultContent = findViewById(R.id.rsContent);
        weightAVG = findViewById(R.id.weight_avg_content);

        back = findViewById(R.id.back_nav);
    }

}
