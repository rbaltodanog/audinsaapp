package audinsa.audiologia;

import java.util.ArrayList;

import android.os.AsyncTask;
import audinsa.audiologia.businessdomain.Blog;
import audinsa.audiologia.datasources.BlogDataSource;

public class ActualizarArticulosTask extends AsyncTask<String, Integer, ArrayList<Blog>>
{
	private BlogDataSource dataSource;
	private ArticulosActivity host;
	
	public ActualizarArticulosTask(ArticulosActivity host)
	{
		this.host = host;
	}

	@Override
	protected ArrayList<Blog> doInBackground(String... params) {
		ArrayList<Blog> result = new ArrayList<Blog>();
		result = getTweetsFromUser(params[0]);
		return result;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Blog> result)
	{
		super.onPostExecute(result);
		host.showArticles(result);
	}

	private ArrayList<Blog> getTweetsFromUser(String searchTerm)
	{
		dataSource = new BlogDataSource();
		return dataSource.getTweets(searchTerm, 1);
	}
}