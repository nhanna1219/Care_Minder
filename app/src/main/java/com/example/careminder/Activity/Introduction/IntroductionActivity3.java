package com.example.careminder.Activity.Introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.careminder.R;

public class IntroductionActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction3);

        ImageButton intro_btn = findViewById(R.id.intro_btn2);

        intro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroductionActivity3.this, IntroductionActivity4.class);
                startActivity(intent);
            }
        });
    }
}