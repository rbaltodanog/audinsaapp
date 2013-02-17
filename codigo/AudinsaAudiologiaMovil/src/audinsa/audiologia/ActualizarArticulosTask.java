package audinsa.audiologia;

import java.util.ArrayList;

import android.os.AsyncTask;
import audinsa.audiologia.businessdomain.Tweet;
import audinsa.audiologia.datasources.TwitterDataSource;

public class ActualizarArticulosTask extends AsyncTask<String, Integer, ArrayList<Tweet>>
{
	private TwitterDataSource dataSource;
	private ArticulosActivity host;
	
	public ActualizarArticulosTask(ArticulosActivity host)
	{
		this.host = host;
	}

	@Override
	protected ArrayList<Tweet> doInBackground(String... params) {
		ArrayList<Tweet> result = new ArrayList<Tweet>();
		result = getTweetsFromUser(params[0]);
		return result;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Tweet> result)
	{
		super.onPostExecute(result);
		host.showArticles(result);
	}

	private ArrayList<Tweet> getTweetsFromUser(String searchTerm)
	{
		dataSource = new TwitterDataSource();
		return dataSource.getTweets(searchTerm, 1);
	}
}