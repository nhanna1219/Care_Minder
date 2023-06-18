package com.example.careminder.Activity.Body;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

public class BodyActivity extends AppCompatActivity {

    ImageButton back;
    Button bodyReport, noteHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        bodyReport = findViewById(R.id.bodyReport);
        noteHistory = findViewById(R.id.note);

        bodyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BodyReportActivity.class));
            }
        });

        noteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NoteHistoryActivity.class));
            }
        });


        back = (ImageButton) findViewById(R.id.back_nav);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }
}