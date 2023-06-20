package com.example.careminder.Activity.Daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.health.connect.client.HealthConnectClient;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careminder.Activity.HealthConnect.HealthConnectManagement;
import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;
import com.example.careminder.utils.DatePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.future.FutureKt;

public class DailyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ImageButton back;
    private String[] motivationalPhrases = {
            "Success is not final, failure is not fatal: it is the courage to continue that counts.",
            "I have not failed. I've just found 10,000 ways that won't work.",
            "The greatest glory in living lies not in never falling, but in rising every time we fall.",
            "Success is not how high you have climbed, but how you make a positive difference to the world.",
            "You miss 100% of the shots you don't take.",
            "The only limit to our realization of tomorrow will be our doubts of today.",
            "Believe you can and you're halfway there."};
    TextView steps, distance, caloriesBurned, duration;
    TextView steps_month, steps_year, steps_toDay, steps_day_after_1, steps_day_after_2, steps_day_before_1, steps_day_before_2;
    HealthConnectClient healthConnectClient;
    PermissionsRationaleActivity management;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        back = findViewById(R.id.back_nav);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        steps_month = findViewById(R.id.steps_month);
        steps_year = findViewById(R.id.steps_year);
        steps_toDay = findViewById(R.id.steps_toDay);
        steps_day_after_1 = findViewById(R.id.steps_day_after_1);
        steps_day_after_2 = findViewById(R.id.steps_day_after_2);
        steps_day_before_1 = findViewById(R.id.steps_day_before_1);
        steps_day_before_2 = findViewById(R.id.steps_day_before_2);
        Calendar currentTime = Calendar.getInstance();
        setCalendar(currentTime);

        ImageButton calendar = findViewById(R.id.calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        steps = findViewById(R.id.step_counting);
        distance = findViewById(R.id.distance);
        caloriesBurned = findViewById(R.id.calories);
        duration = findViewById(R.id.duration);

        healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        management = new PermissionsRationaleActivity();

        loadSteps(healthConnectClient, steps, distance, caloriesBurned, duration);
        TextView motivation = findViewById(R.id.steps_content_highlights);
        Random random = new Random();
        int index = random.nextInt(motivationalPhrases.length);
        motivation.setText(motivationalPhrases[index]);
    }



    private void loadSteps(HealthConnectClient healthConnectClient, TextView steps,TextView distance, TextView caloriesBurned, TextView duration){

        management.loadDailyData(healthConnectClient,steps,distance,caloriesBurned,duration);
    }
    private void loadStepsCalendar(HealthConnectClient healthConnectClient, TextView steps,TextView distance, TextView caloriesBurned, TextView duration, ZonedDateTime today){
        management.loadDailyData(healthConnectClient,steps,distance,caloriesBurned,duration, today);
    }

    private String theNextDay(String[] date) {
        int day = Integer.parseInt(date[0]);
        int year = Integer.parseInt(date[2]);
        String month = date[1];
        switch (month) {
            case "January": case "March": case "May": case "July": case "August": case "October": case "December":
            {
                if (day < 31) {
                    day += 1;
                } else {
                    day = 1;
                }
                break;
            }

            case "April": case "June": case "September": case "November":
            {
                if (day < 30) {
                    day += 1;
                } else {
                    day = 1;
                }
                break;
            }

            case "February":
            {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (day < 29) {
                        day += 1;
                    } else {
                        day = 1;
                    }
                } else {
                    if (day < 28) {
                        day += 1;
                    } else {
                        day = 1;
                    }
                }
                break;
            }
        }
        return String.valueOf(day);
    }

    private String theNextTwoDays(String[] date) {
        String[] alterDate = new String[3];
        alterDate[0] = date[0];
        alterDate[1] = date[1];
        alterDate[2] = date[2];
        alterDate[0] = theNextDay(alterDate);
        return theNextDay(alterDate);
    }

    private String theDayBefore(String[] date) {
        int day = Integer.parseInt(date[0]);
        int year = Integer.parseInt(date[2]);
        String month = date[1];
        switch (month) {
            case "January": case "August":
            {
                if (day == 1) {
                    day = 31;
                } else {
                    day -= 1;
                }
                break;
            }

            case "May": case "July": case "October": case "December":
            {
                if (day == 1) {
                    day = 30;
                } else {
                    day -= 1;
                }
                break;
            }

            case "February": case "April": case "June": case "September": case "November":
            {
                if (day == 1) {
                    day += 31;
                } else {
                    day -= 1;
                }
                break;
            }

            case "March":
            {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (day == 1) {
                        day = 29;
                    } else {
                        day -= 1;
                    }
                } else {
                    if (day == 1) {
                        day = 28;
                    } else {
                        day -= 1;
                    }
                }
                break;
            }
        }
        return String.valueOf(day);
    }

    private String theTwoDaysBefore(String[] date) {
        date[0] = theDayBefore(date);
        return theDayBefore(date);
    }

    private void setCalendar(Calendar calendar) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MMMM/yyyy");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        String[] splitDate = dateTime.split("/");

        steps_month.setText(splitDate[1]);
        steps_year.setText(splitDate[2]);
        steps_toDay.setText(splitDate[0]);

        steps_day_after_1.setText(theNextDay(splitDate));
        steps_day_after_2.setText(theNextTwoDays(splitDate));

        steps_day_before_1.setText(theDayBefore(splitDate));
        steps_day_before_2.setText(theTwoDaysBefore(splitDate));
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Instant instant = Instant.ofEpochMilli(c.getTimeInMillis());
        ZoneId zoneId = ZoneId.systemDefault(); // or any other time zone of your choice
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId);
        LocalDate date = zonedDateTime.toLocalDate();
        LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);
        ZonedDateTime endOfDayInZone = endOfDay.atZone(zonedDateTime.getZone());
        setCalendar(c);
        loadStepsCalendar(healthConnectClient, steps, distance, caloriesBurned, duration, endOfDayInZone);
    }
}
