package audinsa.audiologia;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import audinsa.audiologia.businessdomain.Tweet;
import audinsa.audiologia.datasources.TwitterDataSource;

public class ArticulosActivity extends Activity {
	private TwitterDataSource dataSource;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articulos);
		
		dataSource = new TwitterDataSource();
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		
		tweets = dataSource.getTweets("aundinsaprueba", 1);

		ListView listView = (ListView)findViewById(R.id.listArticulos);
		listView.setAdapter(new TweetItemAdapter(this, android.R.layout.simple_list_item_1, tweets));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_articulos, menu);
		return true;
	}

}
