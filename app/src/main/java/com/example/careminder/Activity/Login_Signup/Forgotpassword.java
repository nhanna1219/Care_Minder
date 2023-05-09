package com.example.careminder.Activity.Login_Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.Activity.Introduction.IntroductionActivity;
import com.example.careminder.R;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class Forgotpassword extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    ImageButton back;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword2);

        back = findViewById(R.id.back_nav);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        Button btnSend = findViewById(R.id.Send);
        TextView lgin = findViewById(R.id.alreadyHaveAccount2);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Confirm
                if (!validateEmail() ) {
                    return;
                }
//
//                String input = "Email: " + textInputEmail.getEditText().getText().toString();
//                input += "\n";
//                startActivity(new Intent(LoginActivity.this, IntroductionActivity.class));
//                Toast.makeText(this, input, Toast.LENGTH_LONG).show();

            }
        });
        lgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Forgotpassword.this, LoginActivity.class));
            }
        });




    }
}