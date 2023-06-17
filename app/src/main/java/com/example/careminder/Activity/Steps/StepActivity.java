package com.example.careminder.Activity.Steps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;
import com.example.careminder.utils.StepCounterDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.io.FilesKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.future.FutureKt;

public class StepActivity extends AppCompatActivity implements SensorEventListener {

    ImageButton back;

    ImageButton instruction;

    private SensorManager sensorManager;
    private boolean running = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    private int actualSteps = 0;

    TextView stepsKcal, stepsKm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        instruction = findViewById(R.id.instruction);
        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepCounterDialog.showInstructionsDialog(StepActivity.this);
            }
        });
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

        stepsKcal = findViewById(R.id.steps_kcal);
        stepsKm = findViewById(R.id.steps_km);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        resetSteps();
    }

    public void setDistanceCalories(int steps) {
        double calsPerSteps = 0.04;
        double avgStrideLength = 0.67;
        double caloriesBurned = Math.round((calsPerSteps * steps) * 100) / 100.0;
        double distance = Math.round((steps * avgStrideLength) * 100) / 100.0;
        String caloriesBurnedString = String.valueOf(caloriesBurned) + " kcal";
        String distanceString = String.valueOf(distance) + " m";
        stepsKcal.setText(caloriesBurnedString);
        stepsKm.setText(distanceString);
    }

    public void insertSteps(){
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity management = new PermissionsRationaleActivity();
        management.writeSteps(healthConnectClient,actualSteps);
    }

    public void resetSteps() {
        LinearLayout steps_circle = findViewById(R.id.steps_circle);
        TextView steps_counting = findViewById(R.id.steps_counting);
        steps_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StepActivity.this, "Long tap to reset steps", Toast.LENGTH_SHORT).show();
            }
        });

        steps_circle.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onLongClick(View v) {
                insertSteps();
                previousTotalSteps = totalSteps;
                actualSteps = 0;
                steps_counting.setText("0");
                stepsKm.setText("0 m");
                stepsKcal.setText("0 kcal");
                saveData();
                Toast.makeText(StepActivity.this, "Reset Successfully!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

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
        TextView steps_counting = findViewById(R.id.steps_counting);
        if (running) {
            loadData();
            totalSteps = event.values[0];
            int currentSteps = (int) (totalSteps - previousTotalSteps);
            actualSteps = actualSteps + currentSteps;
            steps_counting.setText(String.valueOf(actualSteps));
            setDistanceCalories(actualSteps);
            Log.d("Counting:",  String.valueOf(currentSteps));
            previousTotalSteps = totalSteps;
            saveData();
        }
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
        insertSteps();
    }

    private String theNextDay(String[] date) {
        Integer day = Integer.parseInt(date[0]);
        Integer year = Integer.parseInt(date[2]);
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
        Integer day = Integer.parseInt(date[0]);
        Integer year = Integer.parseInt(date[2]);
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
}