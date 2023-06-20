package com.example.careminder.Activity.Body;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.careminder.Data.CustomAdapter;
import com.example.careminder.Data.MyDatabaseHelper;
import com.example.careminder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NoteHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
   ImageButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    MyDatabaseHelper myDB;
    ArrayList<String> note_id, title, note, date;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_history);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteHistoryActivity.this, addNote.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(NoteHistoryActivity.this);
        note_id = new ArrayList<>();
        title = new ArrayList<>();
        note = new ArrayList<>();
        date = new ArrayList<>();


        customAdapter = new CustomAdapter(NoteHistoryActivity.this,this, note_id, title, note, date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoteHistoryActivity.this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        storeDataInArrays();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                note_id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                note.add(cursor.getString(2));
                date.add(cursor.getString(3));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.delete_all){
//            confirmDialog();
//        }
        return super.onOptionsItemSelected(item);
    }

//    void confirmDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Delete All?");
//        builder.setMessage("Are you sure you want to delete all Data?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                MyDatabaseHelper myDB = new MyDatabaseHelper(NoteHistoryActivity.this);
//                myDB.deleteAllData();
//                //Refresh Activity
//                Intent intent = new Intent(NoteHistoryActivity.this, NoteHistoryActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.create().show();
//    }
}