package audinsa.audiologia;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
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

		dataSource = new PerfilDataSource(this);
		dataSource.open();

		List<Perfil> perfiles = dataSource.obtenerTodosLosPerfiles();

		ArrayAdapter<Perfil> adapter = new ArrayAdapter<Perfil>(this,
				android.R.layout.simple_list_item_1, perfiles);

		ListView lstView = (ListView)findViewById(R.id.list);

		lstView.setAdapter(adapter);
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onAgregarPerfilClick(View view) {
		ListView lstView = (ListView)findViewById(R.id.list);
		@SuppressWarnings("unchecked")
		ArrayAdapter<Perfil> adapter = (ArrayAdapter<Perfil>)lstView.getAdapter();
		switch (view.getId()) {
		case R.id.add:
			Intent intentCrearPerfil = new Intent(view.getContext(), PerfilesMantenimientoActivity.class);
			startActivity(intentCrearPerfil);
			break;
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_perfiles, menu);
		return true;
	}

}
