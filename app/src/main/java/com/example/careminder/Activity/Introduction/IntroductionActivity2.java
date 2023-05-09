package com.example.careminder.Activity.Introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.careminder.Activity.Information.InformationActivity;
import com.example.careminder.R;

public class IntroductionActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction2);

        ImageButton intro_btn = findViewById(R.id.intro_btn);

        Button skip = (Button) findViewById(R.id.intro_skip_btn);

        intro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroductionActivity2.this, IntroductionActivity3.class);
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InformationActivity.class));
            }
        });
    }
}