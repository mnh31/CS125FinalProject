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
import com.android.volley.toolbox.Volley;

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
    List<Article> articleList = new ArrayList<>();
    List<JSONObject> jsonObjects = new ArrayList<>();
    private String url;
    private String toPrint;
    private static RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_search);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        
        Intent intent = getIntent();
        String year = intent.getStringExtra("year_key"); // gets the year as an array from main activity
        String month = intent.getStringExtra("month_key"); // gets the month as an array from main activity
        int monthInt = Integer.parseInt(month);
        textView.setText(monthArray[monthInt] + ", " + year);
        /*for (String year : yearArray) {
            for (String month : monthArray) {
                //try catch for possible failed URL exception
                urlList.add(baseURL + year + "/" + month + urlEnding);
            }
        }*/
        url = baseURL + year + "/" + month + urlEnding;
        startApiCall(url);
        textView2.setTextSize(50);

    }
    private void startApiCall(String url) {
            try {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        (JSONObject) null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                try {
                                    apiCallDone(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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

    private void apiCallDone(JSONObject response) throws JSONException {
        //jsonObjects.add(response);
        JSONObject blankObject = new JSONObject();
        if (response.equals(blankObject)) {
            toPrint = "BLANK OBJECT";
        }
        try {
            toPrint = response.get("copyright").toString();
            textView2.setText("This " + toPrint);
        } catch (JSONException ignored) {}
        takeInInfoForArticles(response.getJSONArray("docs"));
        printInfoFromAllArticles();
    }

    private void takeInInfoForArticles(JSONArray articles) throws JSONException {
        for (int i = 0; i < articles.length(); i++) {
            articleList.add(new Article(articles.getJSONObject(i)));
        }
    }

    private void printInfoFromAllArticles() {
        for (Article article : articleList) {
            toPrint = article.headline;
        }
    }

}
