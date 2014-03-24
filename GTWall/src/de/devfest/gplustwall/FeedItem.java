package de.devfest.gplustwall;

import java.util.Date;

public class FeedItem {
	
	private String user, message, image;
	public Date timestamp;
	private String origin;
	private String id;

	public FeedItem(String userName, String msg, String img, Date timest, String orig, String _id) {
		this.user = userName;
		this.message = msg;
		this.image = img;
		this.timestamp = timest;
		this.origin = orig;
		this.id = _id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
