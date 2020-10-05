package cs523.TwitterStream;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;



public class Tweeter {

	private long id;
	private String text;
	private String lang;
	private User user;
	private String source;
	//private String geo;
	//private String coordinate;
	private Place place;
	
	@SerializedName("created_at")
	private String createdAt;
	
	
	@SerializedName("retweet_count")
	private int retweetCount;
	
	@SerializedName("favorite_count")
	private int favoriteCount;

	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String toString() {
		JsonObject obj = new JsonObject();
		obj.addProperty("created_at", createdAt);
		obj.addProperty("tweet_id", id);	
		obj.addProperty("user", user.toString());	
		obj.addProperty("favorite_count", favoriteCount);
		obj.addProperty("text", text);
		obj.addProperty("lang", lang);
		obj.addProperty("source", source);
		obj.addProperty("retweet_count", retweetCount);
		obj.addProperty("place", place == null ? "{}" :place.toString());
		
		return obj.toString();
		
	}
	
	
	
}
