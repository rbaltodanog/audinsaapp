package audinsa.audiologia;

import java.util.HashMap;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import audinsa.audiologia.datasources.TipoExamenDataSource;

public class ExamenesActivity extends Activity {
	private TipoExamenDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examenes);

		dataSource = new TipoExamenDataSource(this);
		dataSource.open();

		SimpleAdapter adapter = new SimpleAdapter(
				this, dataSource.obtenerTodosLosTiposExamenesTwoLineItems(),
				android.R.layout.simple_list_item_2,
				new String[] {"Nombre", "Instrucciones"},
				new int[] {android.R.id.text1, android.R.id.text2});

		ListView lstView = (ListView)findViewById(R.id.listExamenes);
		lstView.setAdapter(adapter);
		setOnListViewItemClickListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_examenes, menu);
		return true;
	}

	private void setOnListViewItemClickListener() {
		ListView lstView = (ListView)findViewById(R.id.listExamenes);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				@SuppressWarnings("unchecked")
				String examen = ((HashMap<String, String>)parent.getItemAtPosition(position)).get("Nombre").toString();
				if (examen.equals("Cuestionario"))
				{
					Intent intent = new Intent(view.getContext(), CuestionarioInstruccionesActivity.class);
					startActivity(intent);
				}
			}
		};
		lstView.setOnItemClickListener(listener);
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_salir:
	            // TODO: Averiguar como salir de aplicacion por completo
	        	//System.exit(0);
	            return true;
	        case R.id.menu_articulos:
	        	Intent intent = new Intent(this.getBaseContext(), ArticulosActivity.class);
				startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
