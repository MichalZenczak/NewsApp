package com.example.gosia.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class FeedLoader extends AsyncTaskLoader {

    private String mUrl;

    public FeedLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    public Object loadInBackground() {
        if (mUrl == null){
            return null;
        }

        List<Feed> feeds = QueryUtils.fetchFeedData(mUrl);
        return feeds;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
