package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    //An array of the name of the months. First element is not used so that index of element matches the month number.
    private String[] monthArray = new String[]{" ", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView textView; // the TextView box on this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String year = intent.getStringExtra("year_key"); // gets the year as a string from main activity
        String month = intent.getStringExtra("month_key"); // gets the month as a string from main activity
        int monthInt = Integer.parseInt(month);
        textView.setText(monthArray[monthInt] + ", " + year);

    }
}
