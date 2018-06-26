package com.example.gosia.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Feed>> {

    private static final int FEED_LOADER_ID = 1;
    //private static final String REQUEST_URL = "https://content.guardianapis.com/search?order-by=newest&use-date=published&api-key=test";
    private static final String REQUEST_URL = "https://content.guardianapis.com/search";
    private FeedAdapter feedAdapter;
    private TextView emptyTv;
    private static final String API_KEY = "5e1fcff5-8897-4c40-a61b-74b9e0483639";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView feedListView = findViewById(R.id.feed_list);
        feedAdapter = new FeedAdapter(this, new ArrayList<Feed>());
        feedListView.setAdapter(feedAdapter);

        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Feed currentFeed = (Feed) feedAdapter.getItem(position);
                Uri uri = Uri.parse(currentFeed.getFeedUrl());
                Intent openUrl = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(openUrl);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        emptyTv = findViewById(R.id.empty_view);
        if (networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(FEED_LOADER_ID,null,this);
        }else{
            View loadingSpinner = findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.GONE);
            emptyTv.setText(getResources().getString(R.string.no_connection));
        }
        feedListView.setEmptyView(emptyTv);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String section = sharedPreferences.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default_value));

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default_value));

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if (section.isEmpty()){
            uriBuilder.appendQueryParameter("order-by",orderBy);
            uriBuilder.appendQueryParameter("api-key", API_KEY);
        }else {
            uriBuilder.appendQueryParameter("section", section);
            uriBuilder.appendQueryParameter("order-by",orderBy);
            uriBuilder.appendQueryParameter("api-key", API_KEY);
        }
        Log.i("onCreateLoader", uriBuilder.toString());
        return new FeedLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Feed>> loader, List<Feed> data) {
        View loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.GONE);
        emptyTv.setText(R.string.no_feeds);
        feedAdapter.clear();
        if (data != null && !data.isEmpty()){
            feedAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        feedAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
