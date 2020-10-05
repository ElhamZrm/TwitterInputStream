package cs523.TwitterStream;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Place {
	
	@SerializedName("|place_type")
	private String placeType;
	
	private String name;
	
	@SerializedName("|full_name")
    private String fullName;
	
	private String city;
    private String country;
    
	@Override
	public String toString() {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", name);
		obj.addProperty("full_name", fullName);	
		obj.addProperty("place_type", placeType);	
		obj.addProperty("city" , city);
		obj.addProperty("country", country);
		
		return obj.toString();
		
	}
   
}
