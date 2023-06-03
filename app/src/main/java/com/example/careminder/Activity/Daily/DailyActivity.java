package com.example.careminder.Activity.Daily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careminder.Activity.HealthConnect.HealthConnectManagement;
import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.future.FutureKt;

public class DailyActivity extends AppCompatActivity implements SensorEventListener {
    ImageButton back;
    private SensorManager sensorManager;
    private boolean running = false;
    private TextView steps;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
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

        TextView steps_month = findViewById(R.id.steps_month);
        TextView steps_year = findViewById(R.id.steps_year);
        TextView steps_toDay = findViewById(R.id.steps_toDay);
        TextView steps_day_after_1 = findViewById(R.id.steps_day_after_1);
        TextView steps_day_after_2 = findViewById(R.id.steps_day_after_2);
        TextView steps_day_before_1 = findViewById(R.id.steps_day_before_1);
        TextView steps_day_before_2 = findViewById(R.id.steps_day_before_2);

        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MMMM/yyyy");
        String dateTime = simpleDateFormat.format(currentTime.getTime());
        String[] splitDate = dateTime.split("/");

        steps_month.setText(splitDate[1]);
        steps_year.setText(splitDate[2]);
        steps_toDay.setText(splitDate[0]);

        steps_day_after_1.setText(theNextDay(splitDate));
        steps_day_after_2.setText(theNextTwoDays(splitDate));

        steps_day_before_1.setText(theDayBefore(splitDate));
        steps_day_before_2.setText(theTwoDaysBefore(splitDate));

//        Counting step
//        loadData();
//        resetSteps();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        steps = findViewById(R.id.step_counting);
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity loadStepsData = new PermissionsRationaleActivity();

        // Pass steps textview to a suspendResult to call a suspend function in kotlin
        CompletableFuture<Unit> suspendResult = FutureKt.future(
                CoroutineScopeKt.CoroutineScope(EmptyCoroutineContext.INSTANCE),
                EmptyCoroutineContext.INSTANCE,
                CoroutineStart.DEFAULT,
                (scope, continuation) -> loadStepsData.loadDailyData(healthConnectClient,steps,continuation)
        );
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
        Log.d("myLog", date[0]);
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
    @Override
    protected void onResume() {
        super.onResume();
        running = true;

        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show();
            Log.d("DailyActivity", String.valueOf("No sensor"));

        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        sensorManager.unregisterListener(this);
    }

    // RESET STEPS FUNCTION
//    public void resetSteps() {
//        TextView step_counting = findViewById(R.id.step_counting);
//        step_counting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DailyActivity.this, "Long tap to reset steps", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        step_counting.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                previousTotalSteps = totalSteps;
//                step_counting.setText("0");
//
//                saveData();
//
//                return true;
//            }
//        });
//    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key1", previousTotalSteps);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        float savedNumber = sharedPreferences.getFloat("key1", 0f);

        Log.d("DailyActivity", String.valueOf(savedNumber));

        previousTotalSteps = savedNumber;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We do not have to write anything in this function for this app
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView step_counting = findViewById(R.id.step_counting);

        if (running) {
            totalSteps = event.values[0];

            int currentSteps = (int) (totalSteps - previousTotalSteps);

            step_counting.setText(String.valueOf(currentSteps));
            Log.d("DailyActivity",  String.valueOf(step_counting));
        }
    }
}
