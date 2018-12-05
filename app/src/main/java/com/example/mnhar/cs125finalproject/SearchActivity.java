package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {


    private String[] monthArray = new String[]{" ", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String year = intent.getStringExtra("year_key");
        String month = intent.getStringExtra("month_key");
        int monthInt = Integer.parseInt(month);
        textView.setText(monthArray[monthInt] + ", " + year);

    }
}
