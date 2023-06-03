package com.example.careminder.utils;

import  static androidx.recyclerview.widget.RecyclerView.*;

import com.example.careminder.*;
import com.example.careminder.Activity.Body.BodyActivity;
import com.example.careminder.Activity.Daily.DailyActivity;
import com.example.careminder.Activity.Exercises.ExercisesActivity;
import com.example.careminder.Activity.Food.DisplayFoodActivity;
import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Steps.StepActivity;
import com.example.careminder.Activity.Water.WaterActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.health.connect.client.HealthConnectClient;

import java.util.concurrent.CompletableFuture;

import kotlin.Unit;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.future.FutureKt;

public class Home_Adapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;

    public Home_Adapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_home_list_view, parent, false);

            Button dailyActivity = view.findViewById(R.id.daily_activity);
            TextView exercises = view.findViewById(R.id.exercise);
            Button steps = view.findViewById(R.id.steps);
            Button food = view.findViewById(R.id.food);
            Button water = view.findViewById(R.id.water);
            Button body = view.findViewById(R.id.body);

            // Display number of steps
            TextView stepCounting = view.findViewById(R.id.step_counting);
            HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(context);
            PermissionsRationaleActivity loadStepsData = new PermissionsRationaleActivity();
            CompletableFuture<Unit> suspendResult = FutureKt.future(
                    CoroutineScopeKt.CoroutineScope(EmptyCoroutineContext.INSTANCE),
                    EmptyCoroutineContext.INSTANCE,
                    CoroutineStart.DEFAULT,
                    (scope, continuation) -> loadStepsData.loadDailyData(healthConnectClient,stepCounting,continuation)
            );

            dailyActivity.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DailyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            exercises.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), ExercisesActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            steps.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), StepActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            food.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DisplayFoodActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            water.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), WaterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            body.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), BodyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }
}
