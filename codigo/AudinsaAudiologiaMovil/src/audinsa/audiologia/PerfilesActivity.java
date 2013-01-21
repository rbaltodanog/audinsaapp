package audinsa.audiologia;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.datasources.PerfilDataSource;

public class PerfilesActivity extends Activity {
	private PerfilDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfiles);
		loadData();
		setOnListViewItemClickListener();
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onAgregarPerfilClick(View view) {
		Intent intentCrearPerfil = new Intent(view.getContext(), PerfilesMantenimientoActivity.class);
		startActivity(intentCrearPerfil);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_perfiles, menu);
		return true;
	}

	private void loadData()
	{
		dataSource = new PerfilDataSource(this);
		dataSource.open();

		List<Perfil> perfiles = dataSource.obtenerTodosLosPerfiles();

		ArrayAdapter<Perfil> adapter = new ArrayAdapter<Perfil>(this,
				android.R.layout.simple_list_item_1, perfiles);

		ListView lstView = (ListView)findViewById(R.id.listPerfiles);

		lstView.setAdapter(adapter);

		dataSource.close();
	}

	private void setOnListViewItemClickListener() {
		ListView lstView = (ListView)findViewById(R.id.listPerfiles);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				//long idPerfil = 0;
				//idPerfil = ((Perfil)parent.getItemAtPosition(position)).getIdPerfil();
				Intent intent = new Intent(view.getContext(), ExamenesActivity.class);
				startActivity(intent);
				// TODO: Pasar id del perfil al activity de Examenes (ya esta hecho)
			}
		};
		lstView.setOnItemClickListener(listener );
	}
}
