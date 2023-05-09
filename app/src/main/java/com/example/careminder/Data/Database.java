package com.example.careminder.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.careminder.Activity.Water.Water;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Database extends SQLiteOpenHelper {

    private final ArrayList<Water> waterList = new ArrayList<>();
    private Context context;

    public Database(@Nullable Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create water table SQL
        String CREATE_water_TABLE = "CREATE TABLE " + Constant.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constant.KEY_ML+ " INT,"
                + Constants.KEY_DATE + " LONG" + ")";

        db.execSQL(CREATE_water_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete existing table
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_NAME);
        //recreate
        onCreate(db);
    }

    //get total number of items saved
    public int getCount(){
        int total = 0;
        //Select all table entries SQL
        String countQuery = "SELECT * FROM " + Constant.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        total = cursor.getCount();
        //close cursor and return count
        cursor.close();
        db.close();

        return total;
    }

    //get total calories user has entered
    public int totalMl(){
        int calories = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        //query to get sum of all calories in table
        String query = "SELECT SUM( " + Constant.KEY_ML + " ) "
                + "FROM " + Constant.TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        //fetch number if cursor has data
        if(cursor.moveToFirst()){
            //get sum of calories from water in table
            calories = cursor.getInt(0);
        }
        else{
            Log.d("Error: ", "Error: Database ml sum failed, database may be empty");
        }

        cursor.close();
        db.close();

        return calories;
    }

    //delete water from DB
    public void deleteWater(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        //delete water w/ passed id from table
        db.delete(Constant.TABLE_NAME,Constant.KEY_ID + " =?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    //add water to DB
    public void addMl(Water water){
        SQLiteDatabase db = this.getWritableDatabase();
        //key value pairs for water ml and record date
        ContentValues waterValues = new ContentValues();
        waterValues.put(Constant.KEY_ML, water.getMl());
        waterValues.put(Constant.KEY_DATE,System.currentTimeMillis());
        //insert and close
        db.insert(Constant.TABLE_NAME,null,waterValues);
        Log.d("Saved: ", "Saved to DB");
        db.close();
    }

    //get all waters from DB
    public ArrayList<Water> getAll(){
        //clear list
        waterList.clear();

        SQLiteDatabase db = this.getReadableDatabase();
        //Set cursor to get all water items, in order by date
        Cursor cursor = db.query(Constant.TABLE_NAME,
                new String[]{Constant.KEY_ID, Constant.KEY_ML, Constant.KEY_DATE},
                null, null, null, null,
                Constant.KEY_DATE+" DESC ");
        //loop through water items, adding each to water list
        if(cursor.moveToFirst()){
            do{
                Water water = new Water();
                //set water id
                water.setId((Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(Constant.KEY_ID)))));
                //set water calories
                water.setMl(cursor.getInt(cursor.getColumnIndex(Constant.KEY_ML)));
                //set water record date, convert to readable format
                DateFormat dateFormat = DateFormat.getDateInstance();
                water.setRecordDate(dateFormat.format(
                        new Date(cursor.getLong(cursor.getColumnIndex(Constant.KEY_DATE)))
                                .getTime()));
                //add water item to list
                waterList.add(water);

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return waterList;
    }

    //get water item data from DB
//    public water getwater(int id){
//
//
//    }
}