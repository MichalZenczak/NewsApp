package com.example.gosia.newsapp;

public class Feed {

    private String feedTitle;
    private String feedSection;
    private String feedDate;
    private String feedUrl;
    private String feedAuthor;

    public Feed(String title, String section, String date, String url, String author){
        feedTitle = title;
        feedSection = section;
        feedDate = date;
        feedUrl = url;
        feedAuthor = author;
    }

    public String getFeedTitle(){
        return feedTitle;
    }

    public String getFeedSection() {
        return feedSection;
    }

    public String getFeedDate() {
        return feedDate;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public String getFeedAuthor(){return feedAuthor; }
}
