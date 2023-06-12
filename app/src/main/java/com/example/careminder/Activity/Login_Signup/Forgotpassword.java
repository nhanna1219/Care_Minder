package com.example.careminder.Activity.Login_Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {

    private TextInputLayout textEmailReset;

    ImageButton back;
    FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private boolean validateEmail() {
        String emailInput = textEmailReset.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textEmailReset.setError("Field can't be empty");
            return false;
        } else if (!emailInput.matches(emailPattern)) {
            textEmailReset.setError("Invalid email address");
            return false;
        } else {
            textEmailReset.setError(null);
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword2);
        textEmailReset = findViewById(R.id.textEmailReset);
        back = findViewById(R.id.back_nav);
        mAuth = FirebaseAuth.getInstance();

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
                String email = textEmailReset.getEditText().getText().toString().trim();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(Forgotpassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(Forgotpassword.this, LoginActivity.class));
                       }else{
                           Toast.makeText(Forgotpassword.this, "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
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