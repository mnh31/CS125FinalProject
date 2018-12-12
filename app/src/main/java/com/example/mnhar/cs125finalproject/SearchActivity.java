package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
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

import com.example.mnhar.cs125finalproject.Article;

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
    private LinearLayout linearLayout;
    private int monthInt;
    private String year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_search);

        linearLayout = findViewById(R.id.myLinearLayout);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        
        Intent intent = getIntent();
        year = intent.getStringExtra("year_key"); // gets the year as an array from main activity
        String month = intent.getStringExtra("month_key"); // gets the month as an array from main activity
        monthInt = Integer.parseInt(month);
        textView.setText(monthArray[monthInt] + ", " + year);
        /*for (String year : yearArray) {
            for (String month : monthArray) {
                //try catch for possible failed URL exception
                urlList.add(baseURL + year + "/" + month + urlEnding);
            }
        }*/
        url = baseURL + year + "/" + month + urlEnding;
        startApiCall(url);
        textView2.setTextSize(12);

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
                                } catch (Exception e) {
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

    private void apiCallDone(JSONObject response) {
        try {
            toPrint = response.get("copyright").toString();
            textView2.setText(toPrint);
            takeInInfoForArticles(response.getJSONObject("response").getJSONArray("docs"));
            printInfoFromAllArticles();

        } catch (JSONException ignored) {}

    }

    private void takeInInfoForArticles(JSONArray articles){
        for (int i = 0; i < articles.length(); i++) {
            try {
                articleList.add(new Article(articles.getJSONObject(i)));
                i += 2;
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void printInfoFromAllArticles() {

        for (Article article : articleList) {
            TextView current = new TextView(this);
            String date = monthArray[monthInt] + " " + article.date + ", " + year;
            current.setLinksClickable(true);
            current.setAutoLinkMask(Linkify.ALL);
            current.setLinkTextColor(Color.BLUE);
            current.setTextSize(15);
            current.setTextColor(Color.BLACK);
            current.setText(date + "\n" + article.snippet + "\n" + article.url + "\n");
            current.setGravity(Gravity.LEFT);
            current.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            TextView headline = new TextView(this);
            headline.setTextColor(Color.BLACK);
            headline.setTextSize(17);
            headline.setTypeface(null, Typeface.BOLD);
            headline.setText(article.headline);

            linearLayout.addView(headline);
            linearLayout.addView(current);
        }
    }
}
