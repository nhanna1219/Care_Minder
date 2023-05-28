package com.example.careminder.Activity.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.careminder.Data.DatabaseHandler;
import com.example.careminder.R;
import com.google.android.material.snackbar.Snackbar;

public class FoodActivity extends AppCompatActivity {
    private EditText foodName, foodCals;
    private Button submitBtn;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ImageButton back = (ImageButton)  findViewById(R.id.bigback);

        db = new DatabaseHandler(FoodActivity.this);
        foodName = (EditText) findViewById(R.id.foodEditTxt);
        foodCals = (EditText) findViewById(R.id.calEditTxt);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //only add food item if the list is not empty
                if(!foodName.getText().toString().isEmpty() &&
                        !foodCals.getText().toString().isEmpty()){
                        saveFoodToDB();

                } else {
                    Snackbar.make(v,
                            "Enter Food and Calories",
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodActivity.this, DisplayFoodActivity.class));
            }
        });

    }

    private void saveFoodToDB() {
        Food food = new Food();
        //set food name from input text
        food.setName(foodName.getText().toString().trim());
        //set food calories value from input
        food.setCalories(Integer.parseInt(foodCals.getText().toString().trim()));
        //add food to database
        db.addFood(food);
        db.close();

        //clear edit texts
        foodName.setText("");
        foodCals.setText("");

        //go to list activity
        startActivity(new Intent(FoodActivity.this, DisplayFoodActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}