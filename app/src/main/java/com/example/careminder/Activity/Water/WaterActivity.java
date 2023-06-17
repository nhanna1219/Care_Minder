package com.example.careminder.Activity.Water;

import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;
import androidx.health.connect.client.time.TimeRangeFilter;
import androidx.health.connect.client.units.Volume;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.careminder.Activity.Food.DisplayFoodActivity;
import com.example.careminder.Activity.Food.Food;
import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.Data.CustomListViewAdapter;
import com.example.careminder.Data.Database;
import com.example.careminder.R;
import com.example.careminder.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.future.FutureKt;

public class WaterActivity extends AppCompatActivity {

    private ImageButton addBtn;
    private ArrayList<Water> dbMl = new ArrayList<>();
    private Database db;
    private EditText ml;
    private TextView water_con;

    private ImageButton back_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        ml = (EditText) findViewById(R.id.water_add);
        water_con = (TextView) findViewById((R.id.water_consumed));
        addBtn = (ImageButton) findViewById(R.id.imageButton);
        back_nav = (ImageButton) findViewById(R.id.back_nav);

        // Captures how much water a user drank in a single drink
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveWater(Double.parseDouble(ml.getText().toString().trim()));

            }
        });


//        refreshData();
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //only add water item if the list is not empty
//                if (!ml.getText().toString().isEmpty()) {
////                    saveMLToDB();
//
//                } else {
//                    Snackbar.make(v,
//                            "Add the amount of water you drunk",
//                            Snackbar.LENGTH_SHORT).show();
//                }
//            }
//        });
        back_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }

    private void saveWater(Double mlwater) {
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity writeMLWater = new PermissionsRationaleActivity();

        // Pass steps textview to a suspendResult to call a suspend function in kotlin
        CompletableFuture<Unit> suspendResult = FutureKt.future(
                CoroutineScopeKt.CoroutineScope(EmptyCoroutineContext.INSTANCE),
                EmptyCoroutineContext.INSTANCE,
                CoroutineStart.DEFAULT,
                (scope, continuation) -> writeMLWater.writeWaterActivity(healthConnectClient, mlwater, continuation)
        );
        ml.setText("");
        Refresh();
    }

    private void Refresh() {

        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity total = new PermissionsRationaleActivity();

        CompletableFuture<Unit> suspendResult = FutureKt.future(
                CoroutineScopeKt.CoroutineScope(EmptyCoroutineContext.INSTANCE),
                EmptyCoroutineContext.INSTANCE,
                CoroutineStart.DEFAULT,
                (scope, continuation) -> total.readWater(healthConnectClient, water_con, continuation)
        );


    }
//    private void saveMLToDB() {
//    Water water = new Water();
//    //set water value from input
//            water.setMl(Integer.parseInt(ml.getText().toString().trim()));
//    //add water to database
//            db.addMl(water);
//            db.close();
//
//    //clear edit texts
//            ml.setText("");
//
//    //go to activity
//    startActivity(new Intent(WaterActivity.this, WaterActivity.class));
//    }
//    private void refreshData() {
//        dbMl.clear();
//        db = new Database(getApplicationContext());
//        //get total ml of water
//        int ml1 = db.totalMl();
//        String formattedML = Utils.formatNumber(ml1);
//        //set formatted values to TextViews
//        water_con.setText(formattedML);
//        int count = db.getCount();
//        db.close();
//    }

}