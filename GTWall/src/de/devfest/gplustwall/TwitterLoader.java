package de.devfest.gplustwall;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import de.devfest.gplustwall.utils.FeedItemComparator;

public class TwitterLoader extends AsyncTaskLoader<TreeSet<FeedItem>> {

    private static final String TAG = TwitterLoader.class.getName();
    private String queryText = "";

    public TwitterLoader(Context context, String queryText) {
        super(context);
        this.queryText = queryText;
    }

    @Override
    public TreeSet<FeedItem> loadInBackground() {
        Twitter twitter = new TwitterFactory().getInstance();
//		StringBuilder feed = new StringBuilder();
        TreeSet<FeedItem> items = new TreeSet<FeedItem>(new FeedItemComparator());
        try {
            Query query = new Query(queryText);
            //query.rpp( 20 );
            QueryResult result;
            result = twitter.search(query);
            ArrayList<Tweet> tweets = (ArrayList<Tweet>) result.getTweets();
            for (int i = 0; i < tweets.size(); i++) {
                Tweet t = tweets.get(i);
                String user = t.getFromUser();
                String msg = t.getText();
                Date d = t.getCreatedAt();
                String img = t.getProfileImageUrl();
                String id = Long.toString(t.getId());
                FeedItem item = new FeedItem(user, msg, img, d, "twitter", id);
                items.add(item);
            }

        } catch (TwitterException te) {
            te.printStackTrace();
            Log.e("TwitterSearcher",
                    "Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        return items;
    }
}