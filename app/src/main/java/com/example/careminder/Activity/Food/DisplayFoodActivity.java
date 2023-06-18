package com.example.careminder.Activity.Food;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;
import androidx.health.connect.client.records.NutritionRecord;

import com.example.careminder.Activity.HealthConnect.HealthConnect;
import com.example.careminder.Activity.HealthConnect.HealthConnectManagement;
import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
//import com.example.careminder.Data.CustomListViewAdapter;
//import com.example.careminder.Data.DatabaseHandler;
import com.example.careminder.Data.CustomListViewAdapter;
import com.example.careminder.R;
import com.example.careminder.utils.DialogListener;
import com.example.careminder.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DisplayFoodActivity extends AppCompatActivity {
//    private DatabaseHandler db;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView listView;

    private Food myFood;
    private TextView totalFoods, totalCalories;
    HealthConnectClient healthConnectClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food);
        ImageButton back = (ImageButton)  findViewById(R.id.bigback);
        listView = (ListView) findViewById(R.id.listView);
        totalCalories = (TextView) findViewById(R.id.totalCalsTxt);
        totalFoods = (TextView) findViewById(R.id.totalItemsTxt);
        Button btn = findViewById(R.id.btnadd);
        healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
//        refreshData();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFoodDialog();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DisplayFoodActivity.this, HomeActivity.class));
            }
        });

    }
    public ArrayList<Food> getDbFoods() {
        // Truy cập danh sách dbFoods từ lớp FoodManagement.kt và trả về
        return dbFoods;
    }
    private void refreshData(){
        dbFoods.clear();
        PermissionsRationaleActivity readList = new PermissionsRationaleActivity();
        ArrayList<Food> db = readList.readListFood(healthConnectClient);
        foodAdapter = new CustomListViewAdapter(DisplayFoodActivity.this, R.layout.list_item, dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();
    }



//    private void refreshData() {
//
//        db = new DatabaseHandler(getApplicationContext());
//        //get all foods from DB
//        ArrayList<Food> foodsFromDB = db.getAllFoods();
//        //get total cals and number of foods
//        int calories = db.totalCalories();
//        int foodCount = db.getCount();
//        String formattedCals = Utils.formatNumber(calories);
//        String formattedItems = Utils.formatNumber(foodCount);
//        //set formatted values to TextViews
//        totalFoods.setText("Foods: " + formattedItems);
//        totalCalories.setText("Calories: " + formattedCals);
//        //loop through foods in database
//        for(int i = 0; i <foodsFromDB.size(); i++){
//            //get current food data
//            myFood = new Food(foodsFromDB.get(i).getName(),
//                    foodsFromDB.get(i).getCalories(),
//                    foodsFromDB.get(i).getRecordDate(),
//                    foodsFromDB.get(i).getId());
//            //add food to list
//            dbFoods.add(myFood);
//            Log.d("FOOD ID: ", String.valueOf(myFood.getId()));
//
//        }
//        db.close();
//
//        //setup food adapter
//        foodAdapter = new CustomListViewAdapter(DisplayFoodActivity.this,
//                R.layout.list_item, dbFoods);
//        listView.setAdapter(foodAdapter);
//        foodAdapter.notifyDataSetChanged();
//
//    }


    ///DIALOG
    private void showAddFoodDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_food, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Add Food")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText foodNameEditText = dialogView.findViewById(R.id.editTextFoodName);
                        EditText foodCaloriesEditText = dialogView.findViewById(R.id.editTextFoodDescription);
                        Spinner mealTypeSpinner = dialogView.findViewById(R.id.spinnerMealType);
                        EditText noteEditText = dialogView.findViewById(R.id.note);

                        String foodName = foodNameEditText.getText().toString();
                        double foodCalories = Double.parseDouble(foodCaloriesEditText.getText().toString());
                        String mealType = mealTypeSpinner.getSelectedItem().toString();
                        String note = noteEditText.getText().toString();

                        // Call a method to handle the entered information
                        handleEnteredFoodInfo(foodName, foodCalories, mealType, note);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void handleEnteredFoodInfo(String foodName, double foodCalories, String mealType, String note) {
        storeData(foodName, foodCalories, mealType, note);
    }

    public void storeData(String foodName, Double calo, String mealName, String note){
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity writeFood = new PermissionsRationaleActivity();
        Food f = new Food(foodName, calo, mealName, note);
        int mealType;
        if (mealName.equals("Breakfast")) {
            mealType = 1;
        } else if (mealName.equals("Bunch")) {
            mealType = 2;
        } else if (mealName.equals("Dinner")) {
            mealType = 3;
        } else if (mealName.equals("Snack")) {
            mealType = 4;
        } else {
            mealType = 0; // Hoặc giá trị mặc định khác nếu không khớp với bất kỳ bữa ăn nào
        }
        writeFood.writeFoodActivity(healthConnectClient, f, mealType,totalCalories );

    }




}
