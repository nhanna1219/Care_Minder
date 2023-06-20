package com.example.careminder.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class Note extends SQLiteOpenHelper {
    private Context context;
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
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NOTE + "TEXT);";
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
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
//            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            Log.d("Note", "Thêm dữ liệu thất bại");
        }
        else {
//            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
            Log.d("Note", "Dữ liệu được thêm thành côn g");
        }
    }
}
