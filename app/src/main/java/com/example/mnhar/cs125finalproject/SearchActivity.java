package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class SearchActivity extends AppCompatActivity {

    //An array of the name of the months. First element is not used so that index of element matches the month number.
    private String[] monthArray = new String[]{" ", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView textView; // the TextView box on this activity
    private String baseURL = "https://api.nytimes.com/svc/archive/v1/";
    private String urlEnding = ".json?api-key=<dd1044029b4644999f1a7c225dafacca>";
    List<URL> urlList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        String[] yearArray = intent.getStringArrayExtra("years_key"); // gets the year as a string from main activity
        String[] monthArray = intent.getStringArrayExtra("months_key"); // gets the month as a string from main activity
        //int monthInt = Integer.parseInt(month);
        //textView.setText(monthArray[monthInt] + ", " + year);
        for (String year : yearArray) {
            for (String month : monthArray) {
                //try catch for possible failed URL exception
                try {
                    urlList.add(new URL(baseURL + year + "/" + month + urlEnding));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
