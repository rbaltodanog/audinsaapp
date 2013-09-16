package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class CuestionarioInstruccionesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuestionario_instrucciones);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_cuestionario_instrucciones,
				menu);
		return true;
	}

	public void onCancelarClick(View view) {
		this.finish();
	}

	public void onEmpezarClick(View view) {

		long perfil = getIntent().getLongExtra("idPerfil", 0);
		long tipoExamen = getIntent().getLongExtra("idTipoExamen", 0);

		Intent intent = new Intent(view.getContext(),
				CuestionarioExamenActivity.class);
		intent.putExtra("idPerfil", perfil);
		intent.putExtra("idTipoExamen", tipoExamen);
		this.finish();
		startActivity(intent);
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

}
