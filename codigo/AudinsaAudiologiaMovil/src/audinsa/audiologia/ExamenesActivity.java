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
	private long perfil=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examenes);

		dataSource = new TipoExamenDataSource(this);
		dataSource.open();

		SimpleAdapter adapter = new SimpleAdapter(
				this, dataSource.obtenerTodosLosTiposExamenesTwoLineItems(),
				android.R.layout.simple_list_item_2,
				new String[] {"Nombre", "Instrucciones","idTipoExamen"},
				new int[] {android.R.id.text1, android.R.id.text2});

		ListView lstView = (ListView)findViewById(R.id.listExamenes);
		lstView.setAdapter(adapter);
		setOnListViewItemClickListener();
		dataSource.close();
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
				HashMap<String, String> hashMap = ((HashMap<String, String>)parent.getItemAtPosition(position));
				String examen = hashMap.get("Nombre").toString();
				long idTipoExamen;
				perfil=getIntent().getLongExtra("idPerfil",0);
				idTipoExamen =Long.valueOf(hashMap.get("idTipoExamen").toString());
			
				if (examen.equals("Cuestionario"))
				{
					Intent intent = new Intent(view.getContext(), CuestionarioInstruccionesActivity.class);
					intent.putExtra("idPerfil", perfil);
					intent.putExtra("idTipoExamen", idTipoExamen);
				//	Date fechaInicio= COLOCARLE FECHA DE SISTEMA
					// PASARSELA PARA QUE AL TERMINAR EL EXAM LA CALCULE
					//INSERTAR  PRIMERO EN EXAM LUEGOP EN RESULTADO;
					//intent.putExtra("fechaInicio",)
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
	        case R.id.menu_perfil:	        	
	        	perfil=getIntent().getLongExtra("idPerfil",0);
			     intent = new Intent();
	        	 intent.setClass(ExamenesActivity.this,PerfilesMantenimientoActivity.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 intent.putExtra("idPerfil", perfil);
                 intent.putExtra("actualizacion", true);
                 startActivity(intent);
                 
                 return true;
				
	        default:
	            return super.onOptionsItemSelected(item);
	            
	            
	    }
	}
}
