package com.example.android.newsfeed;

/**
 * An {@link NewsFeed} object contains information related to a single news feed.
 */

public class NewsFeed {

    /* Title of the News, otherwise known as headline*/
    private String mTitle;

    /* Section the News belong e.g Tech,Education, Sport, etc*/
    private String mSection;

    /* Author or Contributor of the News*/
    //private String mAuthor;

    /* Publication Date and Time of the News*/
    private String mTime;

    /* A link to read more about the news*/
    private String mFeedUrl;

    /**
     *
     *
     * Constructs a new {@link NewsFeed} object.
     *
     */
    public NewsFeed(String title, String section, String time, String newsUrl) {
        mTitle = title;
        mSection = section;
        //mAuthor = author;
        mTime = time;
        mFeedUrl = newsUrl;
    }

    /*
    * Returns the title of the News
    */
    public String getTitle() {
        return mTitle;
    }

    /*
     * Returns the section the News belong
     */
    public String getSection() {
        return mSection;
    }

    /*
     * Returns the News Author's name
     */
//    public String getAuthor() {
//        return mAuthor;
//    }

    /*
     * Returns the publication date and time of the news
     */
    public String getTime() {
        return mTime;
    }

    /*
     * Returns the URL of the news
     */
    public String getNewsUrl() {
        return mFeedUrl;
    }

}
