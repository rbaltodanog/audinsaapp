package clinicaaudinsa.audiologia;

import java.util.ArrayList;

import clinicaaudinsa.audiologia.Adapters.BlogItemAdapter;
import clinicaaudinsa.audiologia.businessdomain.Blog;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import clinicaaudinsa.audiologia.R;

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
		
		if (tweets.size() == 0)
		{
			new AlertDialog.Builder(this)
		    .setTitle("Error al extraer los artículos")
		    .setMessage("Ha ocurrido un error al extraer los artículos. Inténtelo de nuevo.")
		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // Close the activity
		        	finish();
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		    .show();
		}

		BlogItemAdapter adapter = new BlogItemAdapter(this, 
				R.layout.listview_articles_item_row, tweets);
		ListView listView = (ListView) findViewById(R.id.lstTweets);
		
		View header = (View)getLayoutInflater().inflate(R.layout.listview_articles_header_row, null);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		setOnListViewItemClickListener();
	}
	
	private void setOnListViewItemClickListener() {
		ListView lstView = (ListView)findViewById(R.id.lstTweets);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				String blogUrl = "";
				blogUrl = ((Blog)parent.getItemAtPosition(position)).getUrl();
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(blogUrl));
				startActivity(i);
			}
		};
		lstView.setOnItemClickListener(listener);
	}
}