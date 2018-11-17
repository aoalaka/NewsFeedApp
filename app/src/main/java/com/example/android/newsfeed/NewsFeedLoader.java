package com.example.android.newsfeed;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of news feeds by using a Loader to perform the
 * network request to the given URL.
 */

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeed>> {
    /** Tag for log messages */
    private static final String LOG_TAG = NewsFeedLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewsFeedLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsFeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsFeed> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<NewsFeed> newsFeeds = QueryUtils.fetchNewsFeedData(mUrl);
        Log.i(LOG_TAG, "fetchNewsFeedData() called ...");
        return newsFeeds;
    }
}
