package com.example.android.newsfeed;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {


    public NewsFeedAdapter(Activity context, ArrayList<NewsFeed> newsFeeds) {
        super(context, 0, newsFeeds);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.newsfeed_list_item, parent, false);
        }

        // Find the newsfeed at the given position in the list of newsFeeds
        NewsFeed currentNewsFeed = getItem(position);

        // Find the TextView with view ID news_id
        TextView newsIdView = (TextView) listItemView.findViewById(R.id.news_id);
        // Format the newsId to show 1 decimal place
        char formattedNewsId = formatNewsId(currentNewsFeed.getTitle());
        // Display the newsId of the current newsfeed in that TextView
        newsIdView.setText("" + formattedNewsId);


        //Find the TextView with view ID primary_location
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentNewsFeed.getSection());

        //Find the TextView with view ID location_offset
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNewsFeed.getTitle());

        /*TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        titleView.setText(currentNewsFeed.getAuthor());*/

        // Create a new Date object from the time in milliseconds of the newsfeed
        String dateString = currentNewsFeed.getTime();

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateString);
        // Display the date of the current newsfeed in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateString);
        // Display the time of the current newsfeed in that TextView
        timeView.setText(formattedTime);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return newDateFormat.format(date);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(String dateString) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(date);
    }

    /**
     * Return the formatted newsId string showing 1 decimal place (i.e. "3.2")
     * from a decimal newsId value.
     */
    private char formatNewsId(String newsTitle) {
        char newsId = newsTitle.charAt(0);
        return newsId;
    }
}

