package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText year;
    private String yearS; // the year entered into the EditText box as a String
    private EditText month;
    private String monthS; // the month entered into the EditText box as a String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //year and month EditText boxes.
        year = findViewById(R.id.yearEdit);
        month = findViewById(R.id.monthEdit);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearS = year.getText().toString();
                monthS = month.getText().toString();
                openSearchActivity();
            }
        });
    }

    private void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("year_key", yearS); // sends the year as a string to search activity
        intent.putExtra("month_key", monthS); // sends the month as a string to search activity
        startActivity(intent);
    }
}
