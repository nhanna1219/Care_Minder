package com.example.careminder.Activity.Body;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.careminder.Data.MyDatabaseHelper;
import com.example.careminder.R;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class addNote extends AppCompatActivity {

    EditText note_input, title_input;
    TextView date_input;

    Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnote);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        title_input = findViewById(R.id.editTextTitle);
        note_input = findViewById(R.id.editTextContent);
        date_input = findViewById(R.id.date);
        date_input.setText(currentDate);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(addNote.this);
                myDB.addNote(title_input.getText().toString().trim(),
                        note_input.getText().toString().trim(),
                        date_input.getText().toString().trim());
            }
        });
    }
}
