package com.example.careminder.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.careminder.Activity.Food.Food;

import java.util.ArrayList;

public class Note extends SQLiteOpenHelper {
    private Context context;
    private final ArrayList<Food> foodNoteList = new ArrayList<>();
    private static final String DATABASE_NAME = "Note.db";
    private static int DATABASE_VER = 1;
    private static final String TABLE_NAME = "Note";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOTE = "Note";
    public Note(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOTE + " TEXT);";

        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addNote(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTE, note);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

//    Cursor readData(){
//        String query = "SELECT * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = null;
//        if(db != null){
//            cursor = db.rawQuery(query, null);
//        }
//        return cursor;
//    }

    public ArrayList<Food> getAllFoods(){
        //clear list
        foodNoteList.clear();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        //Set cursor to get all food items, in order by date
        Cursor cursor = db.rawQuery(query, null);
        //loop through food items, adding each to foodlist
        if(cursor.moveToFirst()){
            do{
                Food food = new Food();
                //set food id
                food.setId((Integer.parseInt(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)))));
                //set food Note
                food.setNote(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));
                //add food item to list
                foodNoteList.add(food);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foodNoteList;
    }
}
