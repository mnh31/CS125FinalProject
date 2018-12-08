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
    public JSONArray keywords;

    Article (JSONObject individualArticle) throws JSONException {
        url = individualArticle.getString("web_url");
        snippet = individualArticle.getString("snippet");
        leadParagraph = individualArticle.getString("lead_paragraph");
        articleAbstract = individualArticle.getString("abstract");
        //can possibly add multimedia (thumbnails pics etc) here if we have time
        JSONObject headline1 = individualArticle.getJSONObject("headline");
        headline = headline1.getString("main");
        keywords = individualArticle.getJSONArray("keywords");
    }
}
