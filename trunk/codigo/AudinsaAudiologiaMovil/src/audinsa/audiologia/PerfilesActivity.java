package audinsa.audiologia;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import audinsa.audiologia.datasources.PerfilDataSource;

public class PerfilesActivity extends Activity {
	private PerfilDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfiles);

		dataSource = new PerfilDataSource(this);
		dataSource.open();
		
		dataSource.crearPerfil("Roberto Baltodano", new Date(1987, 5, 16), "roberto.baltodano@fiserv.com");
		dataSource.crearPerfil("Marco Chacon", new Date(1984, 4, 7), "marco.chacon@fiserv.com");

		List<Perfil> perfiles = dataSource.obtenerTodosLosPerfiles();

		ArrayAdapter<Perfil> adapter = new ArrayAdapter<Perfil>(this,
				android.R.layout.simple_list_item_1, perfiles);

		ListView lstView = (ListView)findViewById(R.id.list);

		lstView.setAdapter(adapter);
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		ListView lstView = (ListView)findViewById(R.id.list);
		@SuppressWarnings("unchecked")
		ArrayAdapter<Perfil> adapter = (ArrayAdapter<Perfil>)lstView.getAdapter();
		switch (view.getId()) {
		case R.id.add:
			// Ir a mantenimientos de perfil
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
