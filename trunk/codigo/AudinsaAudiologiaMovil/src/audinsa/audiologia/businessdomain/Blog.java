package audinsa.audiologia.businessdomain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Blog {
	public String message;
	public Date tweeted_date;

	public Blog(String message, Date tweetedDate) {
		this.message = message;
		this.tweeted_date = tweetedDate;
	}
	
	public Blog() {
		this.message = "";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
