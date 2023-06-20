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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class DeleteData extends AppCompatActivity {
    ImageButton back;
    Button delete;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;


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
                LocalDateTime endOfDay = LocalDateTime.of(endDate, LocalTime.MAX).withSecond(59).withNano(0);
                // Chuyển đổi LocalDate thành Instant (nếu cần)
                Instant startInstant = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
                Instant endInstant = endOfDay.toInstant(ZoneOffset.UTC);

                deleteData(startInstant, endInstant);
                startActivity(new Intent(getApplicationContext(), AdvancedSettingActivity.class));
                Toast.makeText(getApplicationContext(), "Delete data successfully", Toast.LENGTH_SHORT).show();
            }
        });

        startDatePicker = findViewById(R.id.startDatePicker);
        endDatePicker = findViewById(R.id.endDatePicker);

        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Giới hạn DatePicker không cho phép chọn thời điểm trong tương lai
        startDatePicker.setMaxDate(calendar.getTimeInMillis());
        endDatePicker.setMaxDate(calendar.getTimeInMillis());

        // Thiết lập ngày hiện tại làm giá trị mặc định
        startDatePicker.init(currentYear, currentMonth, currentDay, null);
        endDatePicker.init(currentYear, currentMonth, currentDay, null);
    }
    private void deleteData(Instant start, Instant end) {
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity delData = new PermissionsRationaleActivity();
        delData.deletePersonalData(healthConnectClient, start, end);
    }
}