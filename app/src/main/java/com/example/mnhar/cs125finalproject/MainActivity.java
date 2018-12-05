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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//https://api.nytimes.com/svc/archive/v1/1900/12.json?api-key=%3Cdd1044029b4644999f1a7c225dafacca
//example URL                           year  month    (example for December 1900)

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText month;
    private String monthS;
    private EditText year;
    private String yearS;
    private String baseURL = "https://api.nytimes.com/svc/archive/v1/";
    private String urlEnding = ".json?api-key=<dd1044029b4644999f1a7c225dafacca>";
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
                if (turnMonthToInt(monthS) == "-1") {
                    //then month in invalid
                }

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

                //Creating a list of all URLs we will need to send a GET request to
                List<URL> urlList = new ArrayList<>();
                for (String year : years) {
                    for (String month : months) {
                        String monthAsInt = turnMonthToInt(month);
                        //try catch in case of invalid URL
                        try {
                            urlList.add(new URL (baseURL + year + "/" +  monthAsInt + urlEnding));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
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

    //made method for converting the months to int representation in a string so I can add to URL
    private String turnMonthToInt(String month) {
        month = month.toLowerCase().trim();
        switch (month) {
            case "january":
                return "1";
            case "february":
                return "2";
            case "march":
                return "3";
            case "april":
                return "4";
            case "may":
                return "5";
            case "june":
                return "6";
            case "july":
                return "7";
            case "august":
                return "8";
            case "september":
                return "9";
            case "october":
                return "10";
            case "november":
                return "11";
            case "december":
                return "12";
            default:
                return "-1";
        }
    }
}
