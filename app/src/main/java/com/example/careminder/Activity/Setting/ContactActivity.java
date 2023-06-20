package com.example.careminder.Activity.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.careminder.R;

public class ContactActivity extends AppCompatActivity {
    Button send;
    ImageButton back;
    EditText subject, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        send = findViewById(R.id.submit);

        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);

        // if send toast message "Thank you for your feedback"
        send.setOnClickListener(v -> {
            // send email to developer
            sendEmail();
            // toast message "Thank you for your feedback"
            Toast.makeText(ContactActivity.this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
            // activity to previous activity
            finish();
        });
        back = findViewById(R.id.back_nav);
        back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), HelpSettingActivity.class));
        });
    }
    private void sendEmail() {

        String mSubject = subject.getText().toString();
        String mMessage = message.getText().toString();
        String Recipient = "nqkdeveloper@gmail.com";
        String[] mRecipient = Recipient.split(",");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, mRecipient);
        intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        intent.putExtra(Intent.EXTRA_TEXT, mMessage);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }
}