package audinsa.audiologia;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
//import android.content.res.Resources;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import audinsa.audiologia.Adapters.ResultadosItemAdapter;
import audinsa.audiologia.businessdomain.CompartirResultado;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.businessdomain.Resultado;
import audinsa.audiologia.datasources.ExamenDataSource;
import audinsa.audiologia.datasources.PerfilDataSource;
import audinsa.audiologia.datasources.ResultadoDataSource;

public class ResultadoPerfilActivity extends Activity {
	private ResultadoDataSource resultadoDataSource;
	private ExamenDataSource examenDataSource;
	private ArrayList<Resultado> resultados;
	private CompartirResultado C;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_perfil);
		loadData();
		ListView listView = (ListView) findViewById(R.id.listResultados);
		listView.setEmptyView(findViewById(R.id.lblResultadosVacio));
		registerForContextMenu(listView);
		setOnListViewItemClickListener();	

	}


	private void loadData() {
		resultadoDataSource = new ResultadoDataSource(this);
		examenDataSource = new ExamenDataSource(this);
		long idPerfil=getIntent().getLongExtra("idPerfil",0);
		resultados = resultadoDataSource.obtenerTodosLosResultados(idPerfil);

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
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Resultado resultadoSeleccionado = resultados.get(info.position);

		long idPerfil = resultadoSeleccionado.getId_perfil();
		long idResultado = resultadoSeleccionado.getId_resultado();
		long idExamen = resultadoSeleccionado.getId_examen();
		boolean resultado= false;

		switch (item.getItemId()) {
		case R.id.menu_borrar:
			resultado = resultadoDataSource.borrarResultado(idResultado, idPerfil);
			if (resultado)
			{
				examenDataSource.borrarExamen(idExamen); 
			}
			mostrarMensaje(resultado);
			loadData();
			return true;
		case R.id.menu_compartir:
			 compartirInformacion(idResultado, idPerfil);
			return true;
		case R.id.menu_contactar:
			contactarClinica(idResultado, idPerfil);
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private void compartirInformacion(long idResultado, long idPerfil) {
		
		Resultado r = onGetResultado(idResultado,idPerfil);
		String estado= r.getValorResultado_examen();
		C.compartirInformacion(estado,this.getBaseContext());
	}
	
	private void contactarClinica(long idResultado, long idPerfil) {
		Perfil p = onGetPerfil(idPerfil);
		Resultado r = onGetResultado(idResultado,idPerfil);
		String estado= r.getValorResultado_examen();
		C.contactarClinica(estado, p);
	}
	
	private Perfil onGetPerfil(long idPerfil) {
		PerfilDataSource dataSource = new PerfilDataSource(this);
		Perfil p = new Perfil();
		try {
			p = dataSource.buscarPerfil(idPerfil);
		} catch (Exception ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de obtener el perfil.");
		}
		return p;
	}
	
	private Resultado onGetResultado(long idResultado, long idPerfil) {
		Resultado r = new Resultado();
		try {
			r = resultadoDataSource.buscarResultado(idResultado,idPerfil);
		} catch (Exception ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de obtener el perfil.");
		}
		return r;
	}

	public void mostrarMensaje(boolean resultado){
		AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
		TextView myMsg = new TextView(this);

		if (resultado == false){
			Toast.makeText(
					getBaseContext(),
					"No fue posible borrar el resultado",
					Toast.LENGTH_SHORT).show();
		}
		else{	
			Toast.makeText(
					getBaseContext(),
					"El resultado ha sido borrado",
					Toast.LENGTH_SHORT).show();
		}
		myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
		popupBuilder.setView(myMsg);
	}


}
