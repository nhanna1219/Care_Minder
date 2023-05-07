package com.example.careminder.Activity.Login_Signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.careminder.R;

public class Login_Signup extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooses_login_signup);
        Button btn1 = findViewById(R.id.btn_lgin);
        Button btn2 = findViewById(R.id.btn_sgup);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Login_Signup.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Login_Signup.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

}
