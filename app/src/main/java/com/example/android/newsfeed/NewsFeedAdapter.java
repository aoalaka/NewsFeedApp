package com.example.android.newsfeed;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
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

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable newsIdCircle = (GradientDrawable) newsIdView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int newsIdColor = getNewsIdColor(currentNewsFeed.getTitle());

        // Set the color on the magnitude circle
        newsIdCircle.setColor(newsIdColor);

        //Find the TextView with view ID primary_location
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentNewsFeed.getSection());

        //Find the TextView with view ID primary_location
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(currentNewsFeed.getAuthor());

        //Find the TextView with view ID location_offset
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNewsFeed.getTitle());

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
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

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
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
    public char formatNewsId(String newsTitle) {
        char newsId = newsTitle.charAt(0);
        return newsId;
    }

    private int getNewsIdColor(String newsTitle) {
        int newsIdColorResourceId;
        char newsId = formatNewsId(newsTitle);
        String newsIdString = "" + newsId;
        switch (newsIdString) {
            case "A":
                newsIdColorResourceId = R.color.A;
                break;
            case "B":
                newsIdColorResourceId = R.color.B;
                break;
            case "C":
                newsIdColorResourceId = R.color.C;
                break;
            case "D":
                newsIdColorResourceId = R.color.D;
                break;
            case "E":
                newsIdColorResourceId = R.color.E;
                break;
            case "F":
                newsIdColorResourceId = R.color.F;
                break;
            case "G":
                newsIdColorResourceId = R.color.G;
                break;
            case "H":
                newsIdColorResourceId = R.color.H;
                break;
            case "I":
                newsIdColorResourceId = R.color.I;
                break;
            case "J":
                newsIdColorResourceId = R.color.J;
                break;
            case "K":
                newsIdColorResourceId = R.color.K;
                break;
            case "L":
                newsIdColorResourceId = R.color.L;
                break;
            case "M":
                newsIdColorResourceId = R.color.M;
                break;
            case "N":
                newsIdColorResourceId = R.color.N;
                break;
            case "O":
                newsIdColorResourceId = R.color.O;
                break;
            case "P":
                newsIdColorResourceId = R.color.P;
                break;
            case "Q":
                newsIdColorResourceId = R.color.Q;
                break;
            case "R":
                newsIdColorResourceId = R.color.R;
                break;
            case "S":
                newsIdColorResourceId = R.color.S;
                break;
            case "T":
                newsIdColorResourceId = R.color.T;
                break;
            case "U":
                newsIdColorResourceId = R.color.U;
                break;
            case "V":
                newsIdColorResourceId = R.color.V;
                break;
            case "W":
                newsIdColorResourceId = R.color.W;
                break;
            case "X":
                newsIdColorResourceId = R.color.X;
                break;
            case "Y":
                newsIdColorResourceId = R.color.Y;
                break;
            default:
                newsIdColorResourceId = R.color.Z;
                break;
        }
        return ContextCompat.getColor(getContext(), newsIdColorResourceId);
    }
}

