package com.example.careminder.Activity.Introduction.Login_Signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.careminder.Activity.Introduction.IntroductionActivity;
import com.example.careminder.R;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textInputEmail = findViewById(R.id.textnamelogin);
        textInputPassword = findViewById(R.id.textpasslogin);

        TextView btn = findViewById(R.id.textViewSignUp);
        Button login = findViewById(R.id.btnLogin);
        TextView forgot = findViewById(R.id.forgotpw);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Confirm
                if (!validateEmail() | !validatePassword()) {
                    return;
                }

                String input = "Email: " + textInputEmail.getEditText().getText().toString();
                input += "\n";
                input += "Password: " + textInputPassword.getEditText().getText().toString();
                startActivity(new Intent(getApplicationContext(), IntroductionActivity.class));
                //Toast.makeText(this, input, Toast.LENGTH_LONG).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Forgotpassword.class));
            }
        });

    }
}