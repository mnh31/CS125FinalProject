package com.example.mnhar.cs125finalproject;

import org.json.JSONArray;
import org.json.JSONObject;

public class Article {
    public String url;
    public String snippet;
    public String leadParagraph;
    public String articleAbstract;
    //can possibly add multimedia (thumbnails pics etc) here if we have time
    public String headline;
    public JSONArray keywords;

    Article (String url, String snippet, String leadParagraph, String anAbstract, String articleHeadline) {

    }
}
