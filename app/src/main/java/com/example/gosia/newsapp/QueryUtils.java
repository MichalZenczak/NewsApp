package com.example.gosia.newsapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils(){}

    public static List<Feed> fetchFeedData(String requestUrl){
        URL url = createURL(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with http request", e);
        }
        return extractFeedFromJson(jsonResponse);
    }

    /**
     *Return a new URL object from the given string URL
     * @param requestUrl
     * @return url object
     */
    private static URL createURL(String requestUrl){
            URL url;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error while creating URL object.", e);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with retrieving response from server", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder jsonResponse = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                jsonResponse.append(line);
                line = reader.readLine();
            }
        }
        return jsonResponse.toString();
    }

    /**
     * Return a list of {@link Feed} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Feed> extractFeedFromJson(String jsonResponse){

        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<Feed> feeds = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONObject response = baseJsonObject.getJSONObject("response");
            JSONArray feedArray = response.getJSONArray("results");

            for (int i = 0; i < feedArray.length(); i++){
                JSONObject feed = feedArray.getJSONObject(i);
                String topic = feed.getString("sectionName");
                String date = feed.getString("webPublicationDate");
                String title = feed.getString("webTitle");
                String url = feed.getString("webUrl");
                Feed feedItem = new Feed(title, topic, date, url);
                feeds.add(feedItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error while parsing JSON results", e);
        }
        return feeds;
    }
}

