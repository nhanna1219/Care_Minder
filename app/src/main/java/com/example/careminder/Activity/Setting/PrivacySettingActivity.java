package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;

public class PrivacySettingActivity extends AppCompatActivity {
    TextView back, linkTo, managerPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_setting);
        back = findViewById(R.id.settingsTextView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivacySettingActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        linkTo = findViewById(R.id.textView);
        // link to new tab web on google
        linkTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://support.google.com/googleplay/android-developer/answer/12991134?hl=en";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        managerPermission = findViewById(R.id.textView2);
        managerPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = "com.google.android.apps.healthdata";

                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "The app is not available on your device. Please download Health Connect (Beta).", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}