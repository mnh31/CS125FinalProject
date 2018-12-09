package com.example.mnhar.cs125finalproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Article {
    public String url;
    public String snippet;
    public String leadParagraph;
    public String articleAbstract;
    //can possibly add multimedia (thumbnails pics etc) here if we have time
    public String headline;
    public String[] keywords;
    private String date1;
    public String author;
    public String newsDesk;
    private String[] dateArray;
    public String date;

    Article (JSONObject individualArticle) throws JSONException {
        url = individualArticle.getString("web_url");
        snippet = individualArticle.getString("snippet");
        leadParagraph = individualArticle.getString("lead_paragraph");
        articleAbstract = individualArticle.getString("abstract");
        //can possibly add multimedia (thumbnails pics etc) here if we have time
        JSONObject headline1 = individualArticle.getJSONObject("headline");
        headline = headline1.getString("main");

        //dealing with keywords array
        JSONArray keywords1 = individualArticle.getJSONArray("keywords");
        String[] keywords2 = new String[keywords1.length()];
        for (int i = 0; i < keywords1.length(); i++) {
            JSONObject keywordObj = keywords1.getJSONObject(i);
            keywords2[i] = keywordObj.getString("value");
        }
        keywords = keywords2;

        //getting only the day from the date string
        date1 = individualArticle.getString("pub_date");
        dateArray = date1.split("T");
        dateArray = dateArray[0].split("-");
        date = dateArray[2];

        JSONObject byline = individualArticle.getJSONObject("byline");
        author = byline.getString("original");

        newsDesk = individualArticle.getString("news_desk");
        
    }
}
