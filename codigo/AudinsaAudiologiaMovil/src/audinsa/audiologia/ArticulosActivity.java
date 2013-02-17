package audinsa.audiologia;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import audinsa.audiologia.Adapters.BlogItemAdapter;
import audinsa.audiologia.businessdomain.Blog;

public class ArticulosActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articulos);
		ActualizarArticulosTask actualizarArticulosTask = new ActualizarArticulosTask(this);
		actualizarArticulosTask.execute("saludauditivaaudinsa");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_articulos, menu);
		return true;
	}

	public void showArticles(ArrayList<Blog> tweets)
	{
		findViewById(R.id.lblCargandoArticulos).setVisibility(View.INVISIBLE);
		findViewById(R.id.prgCargaArticulos).setVisibility(View.INVISIBLE);
		findViewById(R.id.lstTweets).setVisibility(View.VISIBLE);

		BlogItemAdapter adapter = new BlogItemAdapter(this, 
				R.layout.listview_articles_item_row, tweets);
		ListView listView = (ListView) findViewById(R.id.lstTweets);
		View header = (View)getLayoutInflater().inflate(R.layout.listview_articles_header_row, null);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
	}
}