package audinsa.audiologia;
import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import audinsa.audiologia.Adapters.TestItemAdapter;
import audinsa.audiologia.businessdomain.TipoExamen;
import audinsa.audiologia.datasources.TipoExamenDataSource;

public class ExamenesActivity extends Activity {
	private TipoExamenDataSource dataSource;
	private long perfil=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examenes);
		loadData();
		setOnListViewItemClickListener();
	}
	
	private void loadData()
	{
		dataSource = new TipoExamenDataSource(this);
		ArrayList<TipoExamen> tipoExamenes = dataSource.obtenerTodosLosTiposExamenes();

		TestItemAdapter adapter = new TestItemAdapter(this, 
				R.layout.listview_examenes_item_row, tipoExamenes);
		ListView listView = (ListView) findViewById(R.id.listExamenes);
		listView.setAdapter(adapter);
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
				TipoExamen tipoExamenSeleccionado = null;
				TestItemAdapter adapter = (TestItemAdapter)parent.getAdapter();
				tipoExamenSeleccionado = adapter.getItem(position);
				
				String examen = tipoExamenSeleccionado.getNombreExamen();
				long idTipoExamen;
				perfil = getIntent().getLongExtra("idPerfil", 0);
				idTipoExamen = tipoExamenSeleccionado.getIdTipoExamen();
			
				if (examen.equals("Cuestionario"))
				{
					Intent intent = new Intent(view.getContext(), CuestionarioInstruccionesActivity.class);
					intent.putExtra("idPerfil", perfil);
					intent.putExtra("idTipoExamen", idTipoExamen);
					startActivity(intent);
				}
			}

			
		};
		lstView.setOnItemClickListener(listener);
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent=null;
		//	Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_salir:
	            this.finish();
	            // Indica que la aplicación debe cerrarse
	            return true;
	        case R.id.menu_articulos:
	        	intent = new Intent(this.getBaseContext(), ArticulosActivity.class);
				startActivity(intent);
	            return true;
	        case R.id.menu_acerca_de:
	        	intent = new Intent(this.getBaseContext(), AcercaDeActivity.class);
				startActivity(intent);
	            return true;
	        case R.id.menu_perfil:	        	
	        	 perfil=getIntent().getLongExtra("idPerfil",0);
			     intent = new Intent();
	        	 intent.setClass(ExamenesActivity.this,PerfilesMantenimientoActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 intent.putExtra("idPerfil", perfil);
                 intent.putExtra("actualizacion", true);
                 startActivity(intent);
                 
                 return true;
	        case R.id.menu_localizar:		        	
	        	 perfil=getIntent().getLongExtra("idPerfil",0);
			   try{
				   intent = new Intent();
		        	 intent.setClass(ExamenesActivity.this,MapaActivity.class);
		        	
	        		 startActivity(intent);
	        	 }
	        	 catch(Exception e){
	        		 
	        		 
	        	 }
	        	 
	        	  return true;
	        default:
	            return super.onOptionsItemSelected(item);
	            
	            
	    }
	}
}
