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
    private TextView textView2; // copyright box. maybe remove this?
    private String baseURL = "https://api.nytimes.com/svc/archive/v1/";
    private String urlEnding = ".json?api-key=dd1044029b4644999f1a7c225dafacca";
    private List<Article> completeArticleList = new ArrayList<>();
    private List<Article> currentArticleDisplayList = new ArrayList<>();
    private String headlineSearchInput;
    private String keywordSearchInput;
    private String url;
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
        url = baseURL + year + "/" + month + urlEnding;
        startApiCall(url);
        textView2.setTextSize(12);


        //Vasu, to implement the search function all you need to do is make two search boxes and a submit button that sets what
        //is in those boxes to the variables "headlineSearchInput" for a headline Search and "keywordSearchInput" for a keyword search
        //then you might have to do something like restart the activity to make only the wanted articles appear

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
            textView2.setText(response.getString("copyright"));
            takeInInfoForArticles(response.getJSONObject("response").getJSONArray("docs"));
            printInfoFromArticles();

        } catch (JSONException ignored) {}

    }

    private void takeInInfoForArticles(JSONArray articles){
        for (int i = 0; i < articles.length(); i++) {
            try {
                completeArticleList.add(new Article(articles.getJSONObject(i)));
            } catch (Exception e) {
                continue;
            }
        }
    }

    //this function I made to be called before printArticles to determine what currentArticleDisplayList will be
    private void printInfoFromArticles() {
        if (headlineSearchInput == null && keywordSearchInput == null) {
            currentArticleDisplayList = completeArticleList;
        } else if (headlineSearchInput == null) {
            currentArticleDisplayList = searchArticles();
        } else if (keywordSearchInput == null) {
            currentArticleDisplayList = keywordArticles();
        } else {
            currentArticleDisplayList = doubleSearchArticles();
        }
        printArticles();
    }

    private void printArticles() {

        // changed this from displaying all articles to whatever we decide "currentArticleDisplayList" is
        for (Article article : currentArticleDisplayList) {
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

    //currently I have different variables for searching headline and searching keywords.
    //this is the headline function. We can make them the same if we want
    private List<Article> searchArticles() {
        List<Article> returnList = new ArrayList<>();
        for (Article article : completeArticleList) {
            String[] wordsToSearch = headlineSearchInput.split(" ");  //splitting the search box input upon space to search for individual words
            for (String word : wordsToSearch) {                     //for each word to search
                if (article.headline.contains(word)) {              //if the headline contains the word
                    returnList.add(article);                        //add the article to return list
                }
            }
        }
        return returnList;
    }

    //this is the keyword function
    private List<Article> keywordArticles() {
        List<Article> returnList = new ArrayList<>();
        for (Article article : completeArticleList) {
            String[] wordsToSearch = keywordSearchInput.split(" ");//splitting the search box input upon space to search for individual words
            for (String word : wordsToSearch) {                     //for each word to search
                for(String keyword : article.keywords) {            //for each keyword
                    if (word.equalsIgnoreCase(keyword)              //if the keyword is equal to the word searched
                            || keywordSearchInput.equalsIgnoreCase(keyword)) { //or if it is equal to the whole keyword search
                        returnList.add(article);                    //add article to return list
                    }
                }
            }
        }
        return returnList;
    }

    //this is the function for if they have both a keyword and headline Search;
    private List<Article> doubleSearchArticles() {
        List<Article> returnList = new ArrayList<>();
        for (Article article : completeArticleList) {
            String[] headlineSearch = headlineSearchInput.split(" ");        //splitting the headline search box input upon space to search for individual words
            String[] keywordSearch = keywordSearchInput.split(" ");  //splitting the keyword search box input upon space to search
            boolean matchHeadline = false;
            boolean matchKeyword = false;
            for (String word : keywordSearch) {
                for(String keyword : article.keywords) {
                    if (word.equalsIgnoreCase(keyword)) {
                        matchKeyword = true;
                    }
                }
            }
            for (String word : headlineSearch) {                     //for each word to search
                if (article.headline.contains(word)) {
                    matchHeadline = true;
                }
            }
            if (matchHeadline && matchKeyword) {
                returnList.add(article);
            }
        }
        return returnList;
    }
}
