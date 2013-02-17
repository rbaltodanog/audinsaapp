package audinsa.audiologia.datasources;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import audinsa.audiologia.businessdomain.Blog;

public class BlogDataSource {
	public ArrayList<Blog> getTweets(String searchTerm, int page) {
		String searchUrl = 
				"http://" + searchTerm + ".blogspot.com/feeds/posts/default?alt=json";

		ArrayList<Blog> blogs = 
				new ArrayList<Blog>();

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

		JSONObject jObj = null;
		JSONArray entries = null;
		JSONParser parser=new JSONParser();

		try {
			Object obj = parser.parse(responseBody);
			jObj=(JSONObject)obj;
		}catch(Exception ex){
			Log.v("Error parseando el response a un objeto JSON","Excepcion: " + ex.getMessage());
		}

		try
		{
			entries = (JSONArray)((JSONObject)jObj.get("feed")).get("entry");
		}
		catch(Exception ex)
		{
			Log.v("Error obteniendo los entries del blog","Excepcion: " + ex.getMessage());
		}

		for(Object t : entries) {
			Blog blog = new Blog();
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

				String titulo=((JSONObject)((JSONObject)t).get("title")).get("$t").toString();
				Date fecha=formatter.parse(((JSONObject)((JSONObject)t).get("published")).get("$t").toString());

				if(titulo.trim().length()!=0){

					blog = new Blog(titulo,fecha);
					blogs.add(blog);
				}


			}


			catch (Exception e) {
				Log.v("Error parseando objeto tweet","Exception: " + e.getMessage());
				e.printStackTrace();
			}
		}


		return blogs;
	}
}
