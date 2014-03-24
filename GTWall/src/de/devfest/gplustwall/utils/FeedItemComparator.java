package de.devfest.gplustwall.utils;

import java.util.Comparator;

import de.devfest.gplustwall.FeedItem;

public class FeedItemComparator implements Comparator<FeedItem> {

	@Override
	public int compare(FeedItem lhs, FeedItem rhs) {
		if(lhs.getId().equals(rhs.getId())) {
			return 0;
		} else {
			int comp = lhs.getTimestamp().compareTo(rhs.getTimestamp());
			if(comp < 0) {
				return 1;
			} else {
				return -1;
			}
		}
		
	}

}
