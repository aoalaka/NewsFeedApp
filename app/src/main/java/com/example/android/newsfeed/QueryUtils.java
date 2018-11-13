package com.example.android.newsfeed;

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

/**
 * Helper methods related to requesting and receiving newsfeed data from Guardian.
 */
public final class QueryUtils {
    public static final String LOG_TAG = NewsFeedActivity.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
         * Query the Guardian dataset and return a list of {@link NewsFeed} objects.
         */
        public static List<NewsFeed> fetchNewsFeedData(String requestUrl) {
            /*try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link NewsFeed}s
        List<NewsFeed> newsfeeds = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link NewsFeed}s
        return newsfeeds;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the newsfeed JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsFeed} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<NewsFeed> extractFeatureFromJson(String newsfeedJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsfeedJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding newsfeeds to
        List<NewsFeed> newsfeeds = new ArrayList<>();

        // Try to parse the newsfeedJSON. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of NewsFeed objects with the corresponding data.

            //Convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject baseJsonResponse = new JSONObject(newsfeedJSON);
            //Access "response" JSONObject
            JSONObject enclosedResponse = baseJsonResponse.getJSONObject("response");
            //Extract “results” JSONArray
            JSONArray newsfeedArray = enclosedResponse.optJSONArray("results");
            //Loop through each result in the array
            for (int i = 0; i < newsfeedArray.length(); i++) {
                //Get newsfeed JSONObject at position i
                JSONObject currentNewsFeed = newsfeedArray.optJSONObject(i);
                //Extract News section name
                String section = currentNewsFeed.getString("sectionName");
                //Extract News Title
                String title = currentNewsFeed.getString("webTitle");
                //Extract News Publication date and time
                String date = currentNewsFeed.getString("webPublicationDate");
                //Extarct News URL
                String newsUrl = currentNewsFeed.getString("webUrl");
                //Extract "tags" JSONArray
                JSONArray tagsArray = enclosedResponse.optJSONArray("tags");
                //Loop through each tag in the array
                ArrayList<String> nameArray = new ArrayList<>();
                String author = "";
                for (int j = 0; j < tagsArray.length(); j++) {
                    //Get tag JSONObject at position j
                    JSONObject currentTag = tagsArray.optJSONObject(j);
                    //Extract name' details
                    String name = currentTag.getString("webTitle");
                    nameArray.add(name);
                    author += nameArray.get(j).concat(" ");
                }

                NewsFeed newsfeed = new NewsFeed(title, section, author, date, newsUrl);
                //Add newsfeed to list of newsfeeds
                newsfeeds.add(newsfeed);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the newsfeed JSON results", e);
        }

        // Return the list of newsfeeds
        return newsfeeds;
    }
}