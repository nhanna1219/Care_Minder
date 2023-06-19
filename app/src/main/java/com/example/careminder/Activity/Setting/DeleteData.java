package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class DeleteData extends AppCompatActivity {
    ImageButton back;
    Button delete;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_data);

        back = findViewById(R.id.back_nav);
        delete = findViewById(R.id.deleteButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdvancedSettingActivity.class));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker startDatePicker = findViewById(R.id.startDatePicker);
                DatePicker endDatePicker = findViewById(R.id.endDatePicker);

                int startYear = startDatePicker.getYear();
                int startMonth = startDatePicker.getMonth();
                int startDayOfMonth = startDatePicker.getDayOfMonth();

                int endYear = endDatePicker.getYear();
                int endMonth = endDatePicker.getMonth();
                int endDayOfMonth = endDatePicker.getDayOfMonth();

                LocalDate startDate = LocalDate.of(startYear, startMonth + 1, startDayOfMonth);
                LocalDate endDate = LocalDate.of(endYear, endMonth + 1, endDayOfMonth);

                // Chuyển đổi LocalDate thành Instant (nếu cần)
                Instant startInstant = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                Instant endInstant = endDate.atStartOfDay(ZoneOffset.UTC).toInstant();

                deleteData(startInstant, endInstant);
                startActivity(new Intent(getApplicationContext(), AdvancedSettingActivity.class));
                Toast.makeText(getApplicationContext(), "Delete data successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteData(Instant start, Instant end) {
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity delData = new PermissionsRationaleActivity();
        delData.deletePersonalData(healthConnectClient, start, end);
    }
}