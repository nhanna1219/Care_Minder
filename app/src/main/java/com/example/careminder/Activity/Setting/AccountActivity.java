package com.example.careminder.Activity.Setting;

import androidx.annotation.NonNull;
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
import com.example.careminder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {
    TextView back, textView;
    EditText height, weight;
    // button
    Button save;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String userID;

    String genderValue;
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
        textView = findViewById(R.id.textView9);

        save = (Button) findViewById(R.id.save);

        Spinner genderSpinner;
        String[] genders = {"Male", "Female"};
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        getBasicIn4();

        getGender(genderSpinner);
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
                Double height_db = Double.parseDouble(height.getText().toString());
                Double weight_db = Double.parseDouble(weight.getText().toString());
                setGender(textView.getText().toString());
                setBasicIn4(height_db, weight_db);
                Toast.makeText(AccountActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
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

    private void getGender(Spinner genderSpinner) {
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        genderValue = document.getString("gender");
                        assert genderValue != null;
                        if (genderValue.equals("Male")){
                            genderSpinner.setSelection(0);
                        }
                        else {
                            genderSpinner.setSelection(1);
                        }
                    }
            }
        }});
    }
    private void getBasicIn4() {
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity readBasicIn4 = new PermissionsRationaleActivity();
        readBasicIn4.readBasicInformation(healthConnectClient, weight, height);
    }
}