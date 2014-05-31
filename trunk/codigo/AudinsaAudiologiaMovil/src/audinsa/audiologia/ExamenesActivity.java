package audinsa.audiologia;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.capricorn.RayMenu;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import audinsa.audiologia.Adapters.TestItemAdapter;
import audinsa.audiologia.businessdomain.TipoExamen;
import audinsa.audiologia.datasources.TipoExamenDataSource;

public class ExamenesActivity extends Activity {
	private TipoExamenDataSource dataSource;
	private long perfil=0;
	private static final int[] ITEM_DRAWABLES = { R.drawable.audinsa_acerca_de_logo, R.drawable.audinsa_acerca_de_logo,
		R.drawable.audinsa_acerca_de_logo, R.drawable.audinsa_acerca_de_logo,R.drawable.audinsa_acerca_de_logo,R.drawable.audinsa_acerca_de_logo };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examenes);
		
	  ArcMenu arcMenu2 = (ArcMenu) findViewById(R.id.arc_menu_2);

        initArcMenu(arcMenu2, ITEM_DRAWABLES);

		//RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
        final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(ITEM_DRAWABLES[i]);

			final int position = i;
			}

		loadData();
		setOnListViewItemClickListener();
	}
    private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(ExamenesActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
	
	private void loadData()
	{
		dataSource = new TipoExamenDataSource(this);
		ArrayList<TipoExamen> tipoExamenes = dataSource.obtenerTodosLosTiposExamenes();

		TestItemAdapter adapter = new TestItemAdapter(this, 
		tipoExamenes);
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
		ListView lstView = (ListView) findViewById(R.id.listExamenes);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TipoExamen tipoExamenSeleccionado = null;
				TestItemAdapter adapter = (TestItemAdapter) parent.getAdapter();
				tipoExamenSeleccionado = adapter.getItem(position);

				String examen = tipoExamenSeleccionado.getNombreExamen();
				long idTipoExamen;
				perfil = getIntent().getLongExtra("idPerfil", 0);
				idTipoExamen = tipoExamenSeleccionado.getIdTipoExamen();

				if (examen.equals("Cuestionario")) {
					Intent intent = new Intent(view.getContext(),
							CuestionarioInstruccionesActivity.class);
					intent.putExtra("idPerfil", perfil);
					intent.putExtra("idTipoExamen", idTipoExamen);
					startActivity(intent);
				} else if (examen.equals("Sensibilidad de oído")) {
					Intent intent = new Intent(view.getContext(),
							SensibilidadDeOidoInstruccionesActivity.class);
					intent.putExtra("idPerfil", perfil);
					intent.putExtra("idTipoExamen", idTipoExamen);
					startActivity(intent);
				} else if (examen.equals("Habla en ruido")) {
					Intent intent = new Intent(view.getContext(),
							HablaEnRuidoInstruccionesActivity.class);
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
	        case R.id.menu_regresar:
	            finish();
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
                 
	        case R.id.menu_resultados:
	        	 perfil=getIntent().getLongExtra("idPerfil",0);
			     intent = new Intent();
	        	 intent.setClass(ExamenesActivity.this,ResultadoPerfilActivity.class);
	        	 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                 
	        	 intent.putExtra("idPerfil", perfil);
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
