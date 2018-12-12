package com.example.mnhar.cs125finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class WordSearchActivity extends AppCompatActivity {

    //An array of the name of the months. First element is not used so that index of element matches the month number.
    private String[] monthArray = new String[]{" ", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private TextView textView; // the TextView box on this activity
    private TextView textView2;
    private String baseURL = "https://api.nytimes.com/svc/archive/v1/";
    private String urlEnding = ".json?api-key=dd1044029b4644999f1a7c225dafacca";
    public List<Article> completeArticleList = new ArrayList<>();
    List<JSONObject> jsonObjects = new ArrayList<>();
    public List<Article> currentArticleDisplayList;
    private String url;
    private String toPrint;
    private String headlineSearchInput;
    private String keywordSearchInput;
    private static RequestQueue requestQueue;
    private LinearLayout linearLayout;
    private int monthInt;
    private String year;
    private Button searchButton;
    private EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_word_search);

        linearLayout = findViewById(R.id.LinearLayout2);
        textView = findViewById(R.id.textViewnew);
        textView2 = findViewById(R.id.textView2new);

        Intent intent = getIntent();
        year = intent.getStringExtra("year_key"); // gets the year as an array from main activity
        String month = intent.getStringExtra("month_key"); // gets the month as an array from main activity
        monthInt = Integer.parseInt(month);
        textView.setText(monthArray[monthInt] + ", " + year);
        headlineSearchInput = intent.getStringExtra("headline");
        headlineSearchInput = headlineSearchInput.toLowerCase();
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

    private void printArticles() {
        for (int i = 0; i < currentArticleDisplayList.size(); i++) {
            Article article = currentArticleDisplayList.get(i);
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
            if (currentArticleDisplayList.equals(completeArticleList)) {
                i += 3;
            }
        }
    }

    //this function I made to be called before printArticles to determine what currentArticleDisplayList will be
    private void printInfoFromArticles() {
        if (headlineSearchInput == null) {
            currentArticleDisplayList = completeArticleList;
        } else {
            List<Article> healineArticles = searchArticles();
            List<Article> keywordAricles = keywordArticles();      //returns list of articles with words from headline search input in headline
            List<Article> wantToDisplay = new ArrayList<>();
            wantToDisplay.addAll(healineArticles);
            wantToDisplay.addAll(keywordAricles);
            currentArticleDisplayList  = wantToDisplay;
        }
        printArticles(); //prints currentArticleDisplayList;
    }

    //currently I have different variables for searching headline and searching keywords.
//this is the headline function. We can make them the same if we want
    private List<Article> searchArticles() {
        List<Article> returnList = new ArrayList<>();
        for (Article article : completeArticleList) {
            String[] wordsToSearch = headlineSearchInput.split(" ");  //splitting the search box input upon space to search for individual words
            for (String word : wordsToSearch) {                     //for each word to search
                String headlineToSearch = article.headline.toLowerCase();
                if (headlineToSearch.contains(word)) {
                    returnList.add(article);
                }
            }
        }
        return returnList;
    }


    //this is the keyword function
    private List<Article> keywordArticles() {
        List<Article> returnList = new ArrayList<>();
        for (Article article : completeArticleList) {
            String[] wordsToSearch = headlineSearchInput.split(" ");//splitting the search box input upon space to search for individual words
            for (String word : wordsToSearch) {                     //for each word to search
                for(String keyword : article.keywords) {            //for each keyword
                    if (keyword.contains(word)                      //if the keyword is equal to the word searched
                            || headlineSearchInput.equalsIgnoreCase(keyword)) { //or if it is equal to the whole keyword search
                        returnList.add(article);                    //add article to return list
                    }
                }
            }
        }
        return returnList;
    }

    private void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("year_key", year);
        intent.putExtra("month_key", monthArray[monthInt]);
        intent.putExtra("headline", headlineSearchInput);
        startActivity(intent);
    }



}
