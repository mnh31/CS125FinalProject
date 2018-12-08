package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

public class SearchActivity extends AppCompatActivity {

    //An array of the name of the months. First element is not used so that index of element matches the month number.
    private String[] monthArray = new String[]{" ", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView textView; // the TextView box on this activity
    private TextView textView2;
    private String baseURL = "https://api.nytimes.com/svc/archive/v1/";
    private String urlEnding = ".json?api-key=dd1044029b4644999f1a7c225dafacca";
    List<String> urlList = new ArrayList<>();
    List<JSONObject> jsonObjects = new ArrayList<>();
    private static RequestQueue requestQueue;
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
                urlList.add(baseURL + year + "/" + month + urlEnding);
            }
        }
        for (String url : urlList) {
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        (String) null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                apiCallDone(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Log.e("JSON request error", error.toString());
                    }
                });
                jsonObjectRequest.setShouldCache(false);
                requestQueue.add(jsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void apiCallDone(JSONObject response) {
        jsonObjects.add(response);
    }
}
