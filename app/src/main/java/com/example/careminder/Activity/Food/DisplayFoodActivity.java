package com.example.careminder.Activity.Food;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.health.connect.client.HealthConnectClient;

import com.example.careminder.Activity.HealthConnect.PermissionsRationaleActivity;
import com.example.careminder.Activity.Home.HomeActivity;
//import com.example.careminder.Data.CustomListViewAdapter;
//import com.example.careminder.Data.DatabaseHandler;
import com.example.careminder.R;

import java.util.ArrayList;

public class DisplayFoodActivity extends AppCompatActivity {
//    private DatabaseHandler db;
    private ArrayList<Food> dbFoods = new ArrayList<>();
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
        refreshData();
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
//        dbFoods.clear();
        PermissionsRationaleActivity readList = new PermissionsRationaleActivity();
        readList.readListFood(healthConnectClient, listView, DisplayFoodActivity.this);
        readList.readFood(healthConnectClient,totalCalories);
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

                        String foodName = foodNameEditText.getText().toString();
                        double foodCalories = Double.parseDouble(foodCaloriesEditText.getText().toString());
                        String mealType = mealTypeSpinner.getSelectedItem().toString();


                        // Call a method to handle the entered information
                        handleEnteredFoodInfo(foodName, foodCalories, mealType);

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

    private void handleEnteredFoodInfo(String foodName, double foodCalories, String mealType) {
        storeData(foodName, foodCalories, mealType);

    }

    public void storeData(String foodName, Double calo, String mealName){
//
//
        HealthConnectClient healthConnectClient = HealthConnectClient.getOrCreate(getApplicationContext());
        PermissionsRationaleActivity writeFood = new PermissionsRationaleActivity();
        Food f = new Food(foodName, calo, mealName);
        int mealType;
        switch (mealName) {
            case "Breakfast":
                mealType = 1;
                break;
            case "Bunch":
                mealType = 2;
                break;
            case "Dinner":
                mealType = 3;
                break;
            case "Snack":
                mealType = 4;
                break;
            default:
                mealType = 0; // Hoặc giá trị mặc định khác nếu không khớp với bất kỳ bữa ăn nào

                break;
        }
        writeFood.writeFoodActivity(healthConnectClient, f, mealType, totalCalories,listView,DisplayFoodActivity.this);

    }




}
