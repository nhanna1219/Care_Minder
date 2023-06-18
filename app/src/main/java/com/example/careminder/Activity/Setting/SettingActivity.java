package com.example.careminder.Activity.Setting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.careminder.Activity.Home.HomeActivity;
import com.example.careminder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    TextView fullName, email, back, gender;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userID;

    BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView;
    private SettingAdapter settingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = findViewById(R.id.settingsTextView);
        fullName = findViewById(R.id.textView3);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        email = findViewById(R.id.textView4);
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // set text hi + name
                fullName.setText(value.getString("name"));
                email.setText(value.getString("email"));

            }
        });

        recyclerView = findViewById(R.id.recyclerView);

//        int numColumns = 2;
//        recyclerView.setLayoutManager(new GridLayoutManager(this, numColumns));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<SettingItem> settingItems = createSettingItems();


        settingAdapter = new SettingAdapter(settingItems);
        recyclerView.setAdapter(settingAdapter);
        ImageView editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        //Bottom Navigation

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.setting_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int HOME_NAV_ID = R.id.home_nav;
                final int SETTING_NAV_ID = R.id.setting_nav;
                switch (item.getItemId()){
                    case HOME_NAV_ID:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case SETTING_NAV_ID:
                        return true;
                }
                return false;
            }});
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    private List<SettingItem> createSettingItems() {
        List<SettingItem> settingItems = new ArrayList<>();

        settingItems.add(new SettingItem("Account",R.drawable.sic_account));
        settingItems.add(new SettingItem("Advanced Settings",R.drawable.sic_volume));
        settingItems.add(new SettingItem("Privacy & Security",R.drawable.sic_security));
        settingItems.add(new SettingItem("Log out", R.drawable.sic_logout));
        settingItems.add(new SettingItem("Help",R.drawable.sic_globe));
        return settingItems;
    }
}