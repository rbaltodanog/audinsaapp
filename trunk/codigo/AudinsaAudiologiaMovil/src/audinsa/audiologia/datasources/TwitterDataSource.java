package audinsa.audiologia.datasources;


import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import android.util.Log;
import audinsa.audiologia.businessdomain.Tweet;

public class TwitterDataSource {
	public TwitterDataSource()
	{
		@SuppressWarnings("unused")
		int i = 1 + 1;
	}
	
	public ArrayList<Tweet> getTweets(String searchTerm, int page) {
		String searchUrl = 
				"https://api.twitter.com/1/statuses/user_timeline.json?id=" 
						+ searchTerm 
						+ "&count=10&page="
						+ page;

		ArrayList<Tweet> tweets = 
				new ArrayList<Tweet>();

		HttpClient client = new  DefaultHttpClient();
		HttpGet get = new HttpGet(searchUrl);

		ResponseHandler<String> responseHandler = 
				new BasicResponseHandler();

		String responseBody = null;
		try {
			responseBody = client.execute(get, responseHandler);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		JSONObject jsonObject = null;
		JSONParser parser=new JSONParser();

		try {
			Object obj = parser.parse(responseBody);
			jsonObject=(JSONObject)obj;
		}catch(Exception ex){
			Log.v("TEST","Excepcion: " + ex.getMessage());
		}

		JSONArray arr = null;

		try {
			Object j = jsonObject.get("results");
			arr = (JSONArray)j;
		} catch(Exception ex){
			Log.v("TEST","Excepcion: " + ex.getMessage());
		}

		for(Object t : arr) {
			Tweet tweet = new Tweet();
			try {
				tweet = new Tweet(
						((JSONObject)t).get("name").toString(),
						((JSONObject)t).get("text").toString(),
						((JSONObject)t).get("profile_image_url").toString()
						);
			} catch (JSONException e) {
				Log.v("Error parseando objeto tweet","Exception: " + e.getMessage());
				e.printStackTrace();
			}
			tweets.add(tweet);
		}
		return tweets;
	}
}
