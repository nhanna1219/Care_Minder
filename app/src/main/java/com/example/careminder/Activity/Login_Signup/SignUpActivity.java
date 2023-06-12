package com.example.careminder.Activity.Login_Signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.careminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout textInputPassword;
    private String checkLogin;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        }
        else if (!emailInput.matches(emailPattern)) {
            textInputEmail.setError("Invalid email address");
            return false;
        }
        else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputName.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
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
        }
        else if (passwordInput.length() < 6) {
            textInputPassword.setError("Password must be at least 6 characters");
            return false;
        }
        else {
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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Button Signup = findViewById(R.id.btnSignup);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khanh code
                PerformForAuth();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
    public void PerformForAuth() {
        String email = textInputEmail.getEditText().getText().toString();
        String password = textInputPassword.getEditText().getText().toString();
        String name = textInputName.getEditText().getText().toString();
        String firstLogin = "true";
        if (validateEmail() & validateUsername() & validatePassword()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    User user = new User(name, firstLogin, email, password);
                    db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);
                    Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    // encrypt password by using MD5
//    private String encryptPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(password.getBytes());
//            byte byteData[] = md.digest();
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < byteData.length; i++) {
//                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
