package com.example.careminder.Activity.Setting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.Activity.Information.InformationActivity;
import com.example.careminder.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {
    TextView back, textView, gender;
    EditText height, weight;
    // button
    Button save;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();


        back = findViewById(R.id.settingsTextView);

        height = findViewById(R.id.textView12);
        weight = findViewById(R.id.textView11);

        save = (Button) findViewById(R.id.save);

        Spinner genderSpinner;
        String[] genders = {"Male", "Female"};
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        textView = findViewById(R.id.textView9);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = genders[position];
                textView.setText(selectedGender);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có giá trị được chọn
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // convert height and weight to double
                Double height_db = Double.parseDouble(height.getText().toString());
                Double weight_db = Double.parseDouble(weight.getText().toString());
                height_db = (double) (Math.round(height_db * 100) / 10000); // Divide 10000 cause we want the value in meters
                weight_db = (double) (Math.round(weight_db * 100) / 100);
                setBasicIn4(height_db, weight_db);

//                // update gender to firestore
                setGender(textView.getText().toString());

                Intent intent = new Intent(AccountActivity.this, SettingActivity.class);
                startActivity(intent);
                // notify user
                Toast.makeText(AccountActivity.this, "Saved Changes", Toast.LENGTH_SHORT).show();



            }
        });
    }
    private void setBasicIn4(Double height, Double weight){
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity writeBasicIn4 = new PermissionsRationaleActivity();
        writeBasicIn4.writeBasicInformation(healthConnectClient, height, weight);
    }
    private void setGender(String gender){
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.update("gender", gender);
    }
}