package com.example.careminder.Activity.Food;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.careminder.Data.DatabaseHandler;
import com.example.careminder.R;

public class FoodItemDetailActivity extends AppCompatActivity {
    private TextView detFoodName, detCalories, detDate;
    private Button detShareBtn;
    private Food myFood;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_detail);
        //setup toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.detToolbar));

        //set up textviews and button
        detFoodName = (TextView) findViewById(R.id.detFoodTxt);
        detCalories = (TextView) findViewById(R.id.detCaloriesValueTxt);
        detDate = (TextView) findViewById(R.id.detDateTxt);

        detShareBtn = (Button) findViewById(R.id.detShareBtn);
        //get serialized food object from intent
        Intent prevIntent = getIntent();
        myFood = (Food) prevIntent.getExtras().getSerializable("userObj");

        //set TextViews to food data
        detFoodName.setText(myFood.getName());
        detDate.setText(myFood.getRecordDate());
        detCalories.setText(String.valueOf(myFood.getCalories()));

        //get food ID so we can delete it
        foodId = myFood.getId();

        //set calories text size
        detCalories.setTextSize(34.9f);
        detCalories.setTextColor(Color.RED);


        detShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFoodRecord();
            }
        });

    }

    private void shareFoodRecord() {
        //prepare data string to use as body of email
        StringBuilder dataString = new StringBuilder();

        dataString.append(" Food: " + detFoodName.getText().toString() + "\n");
        dataString.append(" Calories: " + detCalories.getText().toString() + "\n");
        dataString.append(" Eaten on: " + detDate.getText().toString() + "\n");
        //prepare send email intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "I Ate This Food: Caloric Report");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"recipient@example.com"});
        intent.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try{
            startActivity(intent.createChooser(intent,"Send Email"));

        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),
                    "Install Email client application to share",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteItem){
            //alert dialog for deleting item
            deleteAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAlertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FoodItemDetailActivity.this);
        alertBuilder.setTitle("Delete?");
        alertBuilder.setMessage("Are you sure you wish to delete this item?");
        alertBuilder.setNegativeButton("No", null);
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //delete food item from database
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.deleteFood(foodId);

                Toast.makeText(getApplicationContext(),
                        "Food Item Deleted", Toast.LENGTH_SHORT).show();

                //go back to food list activity
                startActivity(new Intent(
                        FoodItemDetailActivity.this, DisplayFoodActivity.class));

                //remove this activity from activity stack
                FoodItemDetailActivity.this.finish();

                db.close();

            }
        });
        alertBuilder.show();
    }
}
