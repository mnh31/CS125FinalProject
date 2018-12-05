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
    private ArrayList<String> years = new ArrayList<>();
    private ArrayList<String> months = new ArrayList<>();
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

                //Validate input, check each for in range and then add to a String[]
                //Send string array to search activity and deal with there

                //Vasu, started work here on splitting up searches for multiple
                //months and years. Will be split on commas for now.
                String[] monthBefore = monthS.split(",");
                String[] yearBefore = yearS.split(",");
                for (String year : yearBefore) {
                    year = year.trim();
                    Integer intyear = Integer.parseInt(year);
                    if (intyear <= 2017 && intyear >= 1852) {
                        years.add(year);
                    }
                }
                for (String month : monthBefore) {
                    month = month.trim();
                    Integer monthInt = Integer.parseInt(month);
                    if (monthInt <= 12 && monthInt >= 1) {
                        months.add(month);
                    }
                }
                //check for if months or years (arraylists) are empty and display error message
                boolean invalidInput = (months.isEmpty() || years.isEmpty());

                if (invalidInput) {
                    //Vasu, do error message implementation here 
                }
                //Creating a list of all URLs we will need to send a GET request to
                List<URL> urlList = new ArrayList<>();
                for (String year : years) {
                    for (String month : months) {
                        //try catch in case of invalid URL
                        try {
                            urlList.add(new URL (baseURL + year + "/" +  month + urlEnding));
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
        intent.putExtra("months_key", months.toArray());
        intent.putExtra("years_key", years.toArray());
        startActivity(intent);
    }


}
