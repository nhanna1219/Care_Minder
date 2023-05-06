package com.example.careminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
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

    private boolean validateUsername() {
        String usernameInput = textInputName.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputName.setError("Username too long");
            return false;
        } else {
            textInputName.setError(null);
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView btn=findViewById(R.id.alreadyHaveAccount);
        textInputEmail = findViewById(R.id.textemailSignup);
        textInputName = findViewById(R.id.textnameSignup);
        textInputPassword = findViewById(R.id.textpassSignup);

        Button Signup = findViewById(R.id.btnSignup);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateEmail() | !validateUsername() | !validatePassword()) {
                    return;
                }
                String input = "Email: " + textInputEmail.getEditText().getText().toString();
                input += "\n";
                input += "Username: " + textInputName.getEditText().getText().toString();
                input += "\n";
                input += "Password: " + textInputPassword.getEditText().getText().toString();

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUpActivity.this, com.example.careminder.LoginActivity.class));
            }
        });
    }
}