package audinsa.audiologia.businessdomain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tweet {
	public String username;
	public String message;
	public String image_url;
	public Date tweeted_date;

	public Tweet(String username, String message, String url, Date tweetedDate) {
		this.username = username;
		this.message = message;
		this.image_url = url;
		this.tweeted_date = tweetedDate;
	}
	
	public Tweet() {
		this.username = "";
		this.message = "";
		this.image_url = "";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
	public Date getTweeted_date() {
		return tweeted_date;
	}

	public void setTweeted_date(Date tweeted_date) {
		this.tweeted_date = tweeted_date;
	}
	
	public String getTweetedDateAsString()
	{
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		return df.format(getTweeted_date());
	}
	
	public String getTweetedHourAsString()
	{
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.US);
		return df.format(getTweeted_date());
	}
	
	/*@Override
	public String toString()
	{
		
	}*/
}
