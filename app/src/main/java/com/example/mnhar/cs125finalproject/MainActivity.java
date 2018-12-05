package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.*;
import com.android.volley.RequestQueue;
import com.android.volley.Request;


//https://api.nytimes.com/svc/archive/v1/1900/12.json?api-key=%3Cdd1044029b4644999f1a7c225dafacca%3E
//example URL

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText month;
    private String monthS;
    private EditText year;
    private String yearS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        month = findViewById(R.id.monthEdit);
        year = findViewById(R.id.yearEdit);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthS = month.getText().toString();
                yearS = year.getText().toString();

                //NEED TO HANDLE WITH INCORRECT INPUT HERE

                //Vasu, started work here on splitting up searches for multiple
                //months and years. Will be split on commas for now.
                String[] months = monthS.split(",");
                String[] years = yearS.split(",'");
                for (String year : years) {
                    year = year.trim();
                }
                for (String month : months) {
                    month = month.trim();
                }
                openSearchActivity();
            }
        });
    }

    private void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("year_key", yearS);
        intent.putExtra("month_key", monthS);
        startActivity(intent);
    }
}
