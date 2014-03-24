package de.devfest.gplustwall;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.TreeSet;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.devfest.gplustwall.utils.AuthUtils;
import de.devfest.gplustwall.utils.FeedItemComparator;

public class GTWall extends Activity implements
		LoaderCallbacks<TreeSet<FeedItem>> {

	private static final String TAG = GTWall.class.getName();

	private TreeSet<FeedItem> stream;

	private final int GPLUS_LOADER = 0;
	private final int TWITTER_LOADER = 1;
	private boolean twitterLoaded, gplusLoaded;
	private String query;
	private TextView mAuthorTv, mTimeTv, mContentTv;

	private ImageView mAuthorImage, mImageOrigion;

	private ImageDownloader mImageDownloader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		if (intent == null || !intent.hasExtra("token")) {
			SharedPreferences settings = this.getSharedPreferences(
					AuthUtils.PREFS_NAME, 0);
			String accessToken = settings.getString(AuthUtils.PREF_TOKEN, "");
			if (accessToken.equals("")) {
				AuthUtils.refreshAuthToken(this);
				finish();
				return;
			}
		}

		mImageDownloader = new ImageDownloader();
		setContentView(R.layout.activity_gtwall2);
		mAuthorImage = (ImageView) findViewById(R.id.icon);
		mAuthorTv = (TextView) findViewById(R.id.name);
		mTimeTv = (TextView) findViewById(R.id.time);
		mContentTv = (TextView) findViewById(R.id.description);
		mImageOrigion = (ImageView) findViewById(R.id.origionImage);
		
		twitterLoaded = false;
		gplusLoaded = false;
		query = null;

		stream = new TreeSet<FeedItem>(new FeedItemComparator());

		// Get the intent, verify the action and get the query
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		} else {
			onSearchRequested();
		}
	}

	private void doMySearch(String query) {
		this.query = query;
		LoaderManager lm = getLoaderManager();
		if (lm != null) {
			lm.initLoader(GPLUS_LOADER, null, this).forceLoad();
			lm.initLoader(TWITTER_LOADER, null, this).forceLoad();
		}
	}

	@Override
	public void onLoadFinished(Loader<TreeSet<FeedItem>> loader,
			TreeSet<FeedItem> feed) {
		if (feed != null) {
			stream.addAll(feed);
		}
		if (loader.getId() == TWITTER_LOADER) {
			twitterLoaded = true;
		} else if (loader.getId() == GPLUS_LOADER) {
			gplusLoaded = true;
		}
		if (twitterLoaded && gplusLoaded) {
			Log.d(TAG, "start Counter");
			new MyCountDownTimer(60000, 6000).start();
		}
	}

	@Override
	public Loader<TreeSet<FeedItem>> onCreateLoader(int id, Bundle args) {
		if (query != null) {
			switch (id) {
			case TWITTER_LOADER:
				return new TwitterLoader(this, query);
			case GPLUS_LOADER:
				return new ActivitiesLoader(this, query);
			default:
				return null;
			}
		} else {
			Log.d(TAG, "Null query. Not performing any search.");
			return null;
		}

	}

	@Override
	public void onLoaderReset(Loader<TreeSet<FeedItem>> loader) {
	}

	private class MyCountDownTimer extends CountDownTimer {

		SimpleDateFormat sdf;

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			sdf = new SimpleDateFormat("HH:mm - dd.MM.");
		}

		@Override
		public void onFinish() {
			Log.d(TAG, "onFinish");
			stream.clear();
			twitterLoaded = false;
			gplusLoaded = false;
			LoaderManager lm = getLoaderManager();
			lm.restartLoader(GPLUS_LOADER, null, GTWall.this).forceLoad();
			lm.restartLoader(TWITTER_LOADER, null, GTWall.this).forceLoad();
		}

		@Override
		public void onTick(long arg0) {

			if (!stream.isEmpty() && mImageDownloader != null) {
				FeedItem item = stream.pollLast();
				mImageDownloader.download(item.getImage(), mAuthorImage);
				mAuthorTv.setText(item.getUser());
				mTimeTv.setText(sdf.format(item.getTimestamp()));
				mContentTv.setText(item.getMessage());
				if (item.getOrigin().equals("twitter")) {
					mImageOrigion.setImageDrawable(getResources().getDrawable(
							R.drawable.twittericon));
				} else {
					mImageOrigion.setImageDrawable(getResources().getDrawable(
							R.drawable.googleplusicon));
				}
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_gtwall, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			onSearchRequested();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
