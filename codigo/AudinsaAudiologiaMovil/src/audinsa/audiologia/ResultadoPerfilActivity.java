package audinsa.audiologia;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import audinsa.audiologia.Adapters.ResultadosItemAdapter;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.businessdomain.Resultado;
import audinsa.audiologia.datasources.ResultadoDataSource;

public class ResultadoPerfilActivity extends Activity {
	private ResultadoDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_perfil);
		loadData();
		ListView listView = (ListView) findViewById(R.id.listResultados);
		registerForContextMenu(listView);
		setOnListViewItemClickListener();	
		
		}

	
	private void loadData() {
		dataSource = new ResultadoDataSource(this);			    
     	long idPerfil=getIntent().getLongExtra("idPerfil",0);
		ArrayList<Resultado> resultados = dataSource.obtenerTodosLosResultados(idPerfil);

		ResultadosItemAdapter adapter = new ResultadosItemAdapter(this,
				R.layout.listview_resultados_item_row,resultados);
		ListView listView = (ListView) findViewById(R.id.listResultados);
		listView.setAdapter(adapter);
	
		
		}
	private void setOnListViewItemClickListener() {
		ListView lstView = (ListView) findViewById(R.id.listResultados);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				long idPerfil,idResultado,idExamen = 0;
				idPerfil = ((Resultado) parent.getItemAtPosition(position))
						.getId_perfil();
				idResultado = ((Resultado) parent.getItemAtPosition(position))
						.getId_resultado();
				idExamen = ((Resultado) parent.getItemAtPosition(position))
						.getId_examen();
				Intent intent = new Intent(view.getContext(),
						ExamenesActivity.class);
				intent.putExtra("idPerfil", idPerfil);
				intent.putExtra("idResultado", idResultado);
				intent.putExtra("idExamen", idExamen);
				
				startActivity(intent);
			
			
			}
		};
		lstView.setOnItemClickListener(listener);

	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_resultado_perfil, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_regresar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    super.onCreateContextMenu(menu, v, menuInfo);  
        
		getMenuInflater().inflate(R.menu.activity_contextual_resultado, menu);
	  
		 menu.setHeaderTitle(R.string.menu_titulo);  
  }  
  
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
     
    	long idPerfil = getIntent().getLongExtra("idPerfil", 0);
		long idResultado = getIntent().getLongExtra("idResultado", 0);
		long idExamen = getIntent().getLongExtra("idExamen", 0);
		//TODO FALTA QUE ELIMINE PRIMERO DE EXAMEN.
		boolean resultado= false;
		
	switch (item.getItemId()) {
	case R.id.menu_borrar:
		resultado= dataSource.borrarResultado(idResultado, idPerfil);
		mostrarMensaje(resultado);
		finish();
		return true;
	case R.id.menu_compartir:
		finish();
		return true;
	case R.id.menu_contactar:
		finish();
		return true;

	default:
		return super.onOptionsItemSelected(item);

	}
}
    

	
public void mostrarMensaje(boolean resultado){
	AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
	TextView myMsg = new TextView(this);
	
	if (resultado == false){
		myMsg.setText("No fue posible borrar el resultado");
	}
	else{			
		myMsg.setText("El resultado ha sido borrado");			
	}
	myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
	popupBuilder.setView(myMsg);
	
	
	
}
    
	
}
