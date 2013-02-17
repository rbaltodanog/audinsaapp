package audinsa.audiologia.datasources;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import android.util.Log;
import audinsa.audiologia.businessdomain.Tweet;

public class TwitterDataSource {
	public ArrayList<Tweet> getTweets(String searchTerm, int page) {
		String searchUrl = 
				"http://api.twitter.com/1/statuses/user_timeline.json?id=" 
						+ searchTerm 
						+ "&page="
						+ page;

		ArrayList<Tweet> tweets = 
				new ArrayList<Tweet>();

		// Usar este para propositos de prueba
		//String responseBody = "[{\"created_at\":\"Sat Feb 09 22:38:06 +0000 2013\",\"id\":300373033019928576,\"id_str\":\"300373033019928576\",\"text\":\"http://t.co/W4kI3jQv\",\"source\":\"web\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1164262639,\"id_str\":\"1164262639\",\"name\":\"aundinsaprueba\",\"screen_name\":\"aundinsaprueba\",\"location\":\"\",\"url\":null,\"description\":\"\",\"protected\":false,\"followers_count\":0,\"friends_count\":5,\"listed_count\":0,\"created_at\":\"Sat Feb 09 22:30:24 +0000 2013\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":2,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://si0.twimg.com/images/themes/theme1/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http://a0.twimg.com/profile_images/3231085659/05e0a5ca8b6c07a9c47e0617f6033bc8_normal.png\",\"profile_image_url_https\":\"https://si0.twimg.com/profile_images/3231085659/05e0a5ca8b6c07a9c47e0617f6033bc8_normal.png\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false},{\"created_at\":\"Sat Feb 09 22:37:11 +0000 2013\",\"id\":300372801611771904,\"id_str\":\"300372801611771904\",\"text\":\"los cuidados del oido\",\"source\":\"web\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1164262639,\"id_str\":\"1164262639\",\"name\":\"aundinsaprueba\",\"screen_name\":\"aundinsaprueba\",\"location\":\"\",\"url\":null,\"description\":\"\",\"protected\":false,\"followers_count\":0,\"friends_count\":5,\"listed_count\":0,\"created_at\":\"Sat Feb 09 22:30:24 +0000 2013\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":2,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http://a0.twimg.com/images/themes/theme1/bg.png\",\"profile_background_image_url_https\":\"https://si0.twimg.com/images/themes/theme1/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http://a0.twimg.com/profile_images/3231085659/05e0a5ca8b6c07a9c47e0617f6033bc8_normal.png\",\"profile_image_url_https\":\"https://si0.twimg.com/profile_images/3231085659/05e0a5ca8b6c07a9c47e0617f6033bc8_normal.png\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"favorited\":false,\"retweeted\":false}]";
		
		// Usar este para probar conexion a internet
		HttpClient client = new  DefaultHttpClient();
		HttpGet get = new HttpGet(searchUrl);

		ResponseHandler<String> responseHandler = 
				new BasicResponseHandler();
		
		//TODO: Al haber error de conexion, retornar al usuario ese mensaje
		String responseBody = null;
		try {
			responseBody = client.execute(get, responseHandler);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		JSONArray arr = null;
		JSONParser parser=new JSONParser();

		try {
			Object obj = parser.parse(responseBody);
			arr=(JSONArray)obj;
		}catch(Exception ex){
			Log.v("TEST","Excepcion: " + ex.getMessage());
		}

		for(Object t : arr) {
			Tweet tweet = new Tweet();
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.US);
				tweet = new Tweet(
						((org.json.simple.JSONObject)((JSONObject)t).get("user")).get("name").toString(),
						((JSONObject)t).get("text").toString(),
						((org.json.simple.JSONObject)((JSONObject)t).get("user")).get("profile_image_url").toString(),
						formatter.parse(((JSONObject)t).get("created_at").toString())
						);
			} catch (Exception e) {
				Log.v("Error parseando objeto tweet","Exception: " + e.getMessage());
				e.printStackTrace();
			}
			tweets.add(tweet);
		}
		return tweets;
	}
}
