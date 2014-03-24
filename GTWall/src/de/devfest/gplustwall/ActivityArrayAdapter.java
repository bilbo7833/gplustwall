package de.devfest.gplustwall;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public final class ActivityArrayAdapter extends ArrayAdapter<FeedItem> {

	private ImageDownloader cache;
	
	public ActivityArrayAdapter(final Context context, List<FeedItem> people) {
	    super(context, R.layout.row_layout, people);
	    cache = new ImageDownloader();
	}
	
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	    return getDropDownView(position, convertView, parent);
	 }
	 
	 @Override
	  public View getDropDownView(int position, View view, ViewGroup parent) {
	    if (view == null) {
	      final LayoutInflater inflater = (LayoutInflater) getContext()
	          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      view = inflater.inflate(R.layout.row_layout, parent, false);
	    }

	    final FeedItem item = getItem(position);
	    if (null == item) {
	      return view;
	    }

	    ((TextView) view.findViewById(R.id.link)).setText(item.getOrigin());
	    ((TextView) view.findViewById(R.id.name)).setText(item.getUser());
	    ((TextView) view.findViewById(R.id.description)).setText(item.getMessage());
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd.MM.");
	    ((TextView) view.findViewById(R.id.time)).setText(sdf.format(item.getTimestamp()));
	    cache.download(item.getImage(), (ImageView) view.findViewById(R.id.icon));
	    
	    return view;
	  }
	 
	
}
