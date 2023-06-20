package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

public class HelpSettingActivity extends AppCompatActivity {
    TextView back, linkTo, contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_setting);
        back = findViewById(R.id.settingsTextView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpSettingActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        linkTo = findViewById(R.id.TextView1);
        // link to new tab web on google
        linkTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "https://developer.android.com/guide/health-and-fitness/health-connect/frequently-asked-questions";
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));ai
//                startActivity(i);
                // go to new activity
                Intent intent = new Intent(HelpSettingActivity.this, QuestionAnswerActivity.class);
                startActivity(intent);

            }
        });
        contact = findViewById(R.id.TextView2);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to new activity
                Intent intent = new Intent(HelpSettingActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
    }
}