package com.example.careminder.Activity.Information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

public class InformationActivity extends AppCompatActivity {
    ImageButton male_check;
    ImageButton female_check;
    Slider height;
    Slider weight;
    ImageButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        male_check = findViewById(R.id.male);
        female_check = findViewById(R.id.female);

        height = findViewById(R.id.height_slider);
        weight = findViewById(R.id.weight_slider);

        next = (ImageButton) findViewById(R.id.next_btn);
        male_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_check.setBackgroundResource(R.drawable.check_rectangle);
                female_check.setBackgroundResource(R.drawable.uncheck_rectangle);
            }
        });

        female_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female_check.setBackgroundResource(R.drawable.check_rectangle);
                male_check.setBackgroundResource(R.drawable.uncheck_rectangle);
            }
        });
        height.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f", value) + " cm";
            }
        });

        weight.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value) + " kg";
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformationActivity.this, HomeActivity.class));
            }
        });
    }
}