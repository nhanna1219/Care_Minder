package com.example.careminder.Activity.Information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.Activity.Introduction.IntroductionActivity;
import com.example.careminder.Activity.Login_Signup.LoginActivity;
import com.example.careminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import kotlin.Unit;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.future.FutureKt;

public class InformationActivity extends AppCompatActivity {
    ImageButton male_check;
    ImageButton female_check;
    Slider height;
    Slider weight;
    ImageButton next;
    Animation.AnimationListener animationListener;

    String gender = "male";

    Double weight_db;

    Double height_db;

    private FirebaseFirestore db;
    private FirebaseUser user;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseFirestore.getInstance();

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.gender_check);
        anim.setAnimationListener(animationListener);

        male_check = findViewById(R.id.male);
        female_check = findViewById(R.id.female);

        height = findViewById(R.id.height_slider);
        weight = findViewById(R.id.weight_slider);

        next = (ImageButton) findViewById(R.id.next_btn);
        male_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set a delay of 2000 milliseconds (2 seconds)
                male_check.setBackgroundResource(R.drawable.check_rectangle);
                male_check.startAnimation(anim);
                female_check.setBackgroundResource(R.drawable.uncheck_rectangle);
                gender = "male";
            }
        });

        female_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female_check.setBackgroundResource(R.drawable.check_rectangle);
                female_check.startAnimation(anim);
                male_check.setBackgroundResource(R.drawable.uncheck_rectangle);
                gender = "female";
            }
        });
        height.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                height_db = (double) Math.round(value) / 100;
                return String.format("%.1f", value) + " cm";
            }
        });

        weight.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                weight_db = (double) Math.round(value * 100) / 100;
                return String.format("%.2f", value) + " kg";
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirstLoginStatus();
                setGender(gender);
                setBasicIn4(height_db,weight_db);
                startActivity(new Intent(InformationActivity.this, HomeActivity.class));
            }
        });


    }

    private void setFirstLoginStatus(){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (Objects.equals(task.getResult().get("firstLogin"), "true")) {
                    docRef.update("firstLogin", "false");
                }
            }}
        );
    }

    private void setGender(String gender){
        DocumentReference docRef = db.collection("users").document(uid);
        // Add a new field to the user document
        Map<String, Object> updates = new HashMap<>();
        updates.put("gender", gender);
        docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InformationActivity.this, "Information Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBasicIn4(Double height, Double weight){
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity writeBasicIn4 = new PermissionsRationaleActivity();
        writeBasicIn4.writeBasicInformation(healthConnectClient, height, weight);
    }
}