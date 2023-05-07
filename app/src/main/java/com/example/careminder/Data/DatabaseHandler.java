package com.example.careminder.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.careminder.Activity.Food.Food;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<Food> foodList = new ArrayList<>();
    private Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create food table SQL
        String CREATE_FOOD_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.KEY_FOOD_NAME + " TEXT,"
                + Constants.KEY_CALORIES+ " INT," + Constants.KEY_DATE + " LONG" + ")";

        db.execSQL(CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete existing table
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        //recreate
        onCreate(db);
    }

    //get total number of items saved
    public int getCount(){
        int total = 0;
        //Select all table entries SQL
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        total = cursor.getCount();
        //close cursor and return count
        cursor.close();
        db.close();

        return total;
    }

    //get total calories user has entered
    public int totalCalories(){
        int calories = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        //query to get sum of all calories in table
        String query = "SELECT SUM( " + Constants.KEY_CALORIES + " ) "
                + "FROM " + Constants.TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        //fetch number if cursor has data
        if(cursor.moveToFirst()){
            //get sum of calories from food in table
            calories = cursor.getInt(0);
        }
        else{
            Log.d("Error: ", "Error: Database calorie sum failed, database may be empty");
        }

        cursor.close();
        db.close();

        return calories;
    }

    //delete food from DB
    public void deleteFood(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //delete food w/ passed id from table
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID + " =?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    //add food item to DB
    public void addFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        //key value pairs for food name, calories, and record date
        ContentValues foodValues = new ContentValues();
        foodValues.put(Constants.KEY_FOOD_NAME, food.getName());
        foodValues.put(Constants.KEY_CALORIES, food.getCalories());
        foodValues.put(Constants.KEY_DATE,System.currentTimeMillis());
        //insert and close
        db.insert(Constants.TABLE_NAME,null,foodValues);
        Log.d("Saved: ", "Saved food item to DB");
        db.close();
    }

    //get all foods from DB
    public ArrayList<Food> getAllFoods(){
        //clear list
        foodList.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        //Set cursor to get all food items, in order by date
        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID, Constants.KEY_FOOD_NAME,
                        Constants.KEY_CALORIES, Constants.KEY_DATE},
                null, null, null, null,
                Constants.KEY_DATE+" DESC ");
        //loop through food items, adding each to foodlist
        if(cursor.moveToFirst()){
            do{
                Food food = new Food();
                //set food id
                food.setId((Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(Constants.KEY_ID)))));
                //set food name
                food.setName(cursor.getString(cursor.getColumnIndex(Constants.KEY_FOOD_NAME)));
                //set food calories
                food.setCalories(cursor.getInt(cursor.getColumnIndex(Constants.KEY_CALORIES)));
                //set food record date, convert to readable format
                DateFormat dateFormat = DateFormat.getDateInstance();
                food.setRecordDate(dateFormat.format(
                        new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE)))
                                .getTime()));
                //add food item to list
                foodList.add(food);

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodList;
    }

//    //get food item data from DB
//    public Food getFood(int id){
//
//
//    }
}
