package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.careminder.Activity.Login_Signup.LoginActivity;
import com.example.careminder.R;
import com.google.firebase.auth.FirebaseAuth;
public class LogoutActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    public static void logoutUser(Context context) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        mAuth = FirebaseAuth.getInstance();
        logoutUser(this);
    }
}