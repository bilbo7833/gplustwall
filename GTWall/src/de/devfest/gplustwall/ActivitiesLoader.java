package de.devfest.gplustwall;

import java.io.IOException;
import java.util.Date;
import java.util.TreeSet;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;

import de.devfest.gplustwall.utils.FeedItemComparator;

public class ActivitiesLoader extends AsyncTaskLoader<TreeSet<FeedItem>> {

    private static final String TAG = ActivitiesLoader.class.getName();
    private static final Long MAX_RESULTS_PER_REQUEST = 20L;
    private String mNextPageToken;
    TreeSet<FeedItem> mFeeds;
    private boolean mIsLoading;
    private String query;

    public ActivitiesLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    public TreeSet<FeedItem> loadInBackground() {
         try {
              Plus plus = new PlusWrap(getContext()).get();
              ActivityFeed activities = null;
              activities = plus.activities().search(query).setPageToken(mNextPageToken).setMaxResults(MAX_RESULTS_PER_REQUEST).execute();
              mNextPageToken = activities.getNextPageToken();
              castToFeeds(activities);
              return mFeeds;
            } catch (IOException e) {
              Log.e(TAG, "Unable to list activities", e);
            }
            return null;
    }

    private void castToFeeds(ActivityFeed activities) {
        if (mFeeds == null) {
            mFeeds = new TreeSet<FeedItem>(new FeedItemComparator());
        }
        for (Activity item : activities.getItems()) {
            mFeeds.add(new FeedItem(item.getActor().getDisplayName(), item
                    .getTitle(),
                    item.getActor().getImage().getUrl().toString(), new Date(
                            item.getPublished().getValue()), "G+", item.getId()));
        }
    }

    @Override
    protected void onStopLoading() {
        mIsLoading = false;
        cancelLoad();
    }

     public boolean isLoading() {
         return mIsLoading;
     }


     public boolean hasMoreResults(){
         if (mNextPageToken != null)
            return true;
        else
            return false;
     }

}
