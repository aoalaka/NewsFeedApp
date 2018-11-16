package com.example.android.newsfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsfeed.QueryUtils.LOG_TAG;

public class NewsFeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsFeed>> {
    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    /**
     * Adapter for the list of newsfeeds
     */
    private NewsFeedAdapter mAdapter;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWSFEED_LOADER_ID = 1;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyStateTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView newsfeedListView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_state_text_view);
        newsfeedListView.setEmptyView(emptyStateTextView);

        // Create a new adapter that takes an empty list of newsfeeds as input
        mAdapter = new NewsFeedAdapter(this, new ArrayList<NewsFeed>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsfeedListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected newsfeed.
        newsfeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current newsfeed that was clicked on
                NewsFeed currentNewsFeed = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsfeedUri = Uri.parse(currentNewsFeed.getNewsUrl());

                // Create a new intent to view the newsfeed URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsfeedUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWSFEED_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
        Log.i(LOG_TAG, "Earthquake Activity onCreate() called ...");

    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<NewsFeed>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default));
        String tag = sharedPrefs.getString(
                getString(R.string.settings_tag_key),
                getString(R.string.settings_tag_default));
        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));
        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `section=technology`
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("tag", tag);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", "78aa14a6-b3b2-48a8-bf4e-1f85088ff7dd");



        // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new NewsFeedLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> newsfeeds) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No news found."
        emptyStateTextView.setText(R.string.no_newsfeeds_found);
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link NewsFeed}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsfeeds != null && !newsfeeds.isEmpty()) {
            mAdapter.addAll(newsfeeds);
        }
        Log.i(LOG_TAG, "OnLoadFinished() called ...");
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        Log.i(LOG_TAG, "OnLoaderReset() called ...");
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
