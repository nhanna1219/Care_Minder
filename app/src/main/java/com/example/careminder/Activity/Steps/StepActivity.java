package com.example.careminder.Activity.Steps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.careminder.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        TextView steps_month = findViewById(R.id.steps_month);
        TextView steps_year = findViewById(R.id.steps_year);
        TextView steps_toDay = findViewById(R.id.steps_toDay);
        TextView steps_day_after_1 = findViewById(R.id.steps_day_after_1);
        TextView steps_day_after_2 = findViewById(R.id.steps_day_after_2);
        TextView steps_day_before_1 = findViewById(R.id.steps_day_before_1);
        TextView steps_day_before_2 = findViewById(R.id.steps_day_before_2);

        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/MMMM/yyyy");
        String dateTime = simpleDateFormat.format(currentTime.getTime());
        String[] splitDate = dateTime.split("/");

        steps_month.setText(splitDate[1]);
        steps_year.setText(splitDate[2]);
        steps_toDay.setText(splitDate[0]);

        steps_day_after_1.setText(theNextDay(splitDate));
        steps_day_after_2.setText(theNextTwoDays(splitDate));

        steps_day_before_1.setText(theDayBefore(splitDate));
        steps_day_before_2.setText(theTwoDaysBefore(splitDate));
    }

    private String theNextDay(String[] date) {
        Integer day = Integer.parseInt(date[0]);
        Integer year = Integer.parseInt(date[2]);
        String month = date[1];
        switch (month) {
            case "January": case "March": case "May": case "July": case "August": case "October": case "December":
            {
                if (day < 31) {
                    day += 1;
                } else {
                    day = 1;
                }
                break;
            }

            case "April": case "June": case "September": case "November":
            {
                if (day < 30) {
                    day += 1;
                } else {
                    day = 1;
                }
                break;
            }

            case "February":
            {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (day < 29) {
                        day += 1;
                    } else {
                        day = 1;
                    }
                } else {
                    if (day < 28) {
                        day += 1;
                    } else {
                        day = 1;
                    }
                }
                break;
            }
        }
        return String.valueOf(day);
    }

    private String theNextTwoDays(String[] date) {
        String[] alterDate = new String[3];
        alterDate[0] = date[0];
        alterDate[1] = date[1];
        alterDate[2] = date[2];
        alterDate[0] = theNextDay(alterDate);
        return theNextDay(alterDate);
    }

    private String theDayBefore(String[] date) {
        Log.d("myLog", date[0]);
        Integer day = Integer.parseInt(date[0]);
        Integer year = Integer.parseInt(date[2]);
        String month = date[1];
        switch (month) {
            case "January": case "August":
            {
                if (day == 1) {
                    day = 31;
                } else {
                    day -= 1;
                }
                break;
            }

            case "May": case "July": case "October": case "December":
            {
                if (day == 1) {
                    day = 30;
                } else {
                    day -= 1;
                }
                break;
            }

            case "February": case "April": case "June": case "September": case "November":
            {
                if (day == 1) {
                    day += 31;
                } else {
                    day -= 1;
                }
                break;
            }

            case "March":
            {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (day == 1) {
                        day = 29;
                    } else {
                        day -= 1;
                    }
                } else {
                    if (day == 1) {
                        day = 28;
                    } else {
                        day -= 1;
                    }
                }
                break;
            }
        }
        return String.valueOf(day);
    }

    private String theTwoDaysBefore(String[] date) {
        date[0] = theDayBefore(date);
        return theDayBefore(date);
    }
}