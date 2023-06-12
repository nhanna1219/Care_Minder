package com.example.careminder.Activity.Login_Signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.Activity.Introduction.IntroductionActivity;
import com.example.careminder.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!emailInput.matches(emailPattern)) {
            textInputEmail.setError("Invalid email address");
            return false;
        }
        else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() < 6) {
            textInputPassword.setError("Password must be at least 6 characters");
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

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Confirm
                if (!validateEmail() | !validatePassword()) {
                    return;
                }


                String email = textInputEmail.getEditText().getText().toString().trim();
                String password = textInputPassword.getEditText().getText().toString().trim();
                // decrypt the password MD5
//                String pass = password;
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(com.google.firebase.auth.AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                // check first login of user
                                startActivity(new Intent(LoginActivity.this, IntroductionActivity.class));
                            }
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show());

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

//    // decrypt password MD5 password as above function encryptPassword
//    public String decryptPassword(String password) {
//        String decryptedPassword = "";
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] array = md.digest(password.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for (byte b : array) {
//                sb.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
//            }
//            decryptedPassword = sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return decryptedPassword;
//    }
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