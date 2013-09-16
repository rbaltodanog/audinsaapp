package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SensibilidadDeOidoInstruccionesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensibilidad_de_oido_instrucciones);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.activity_sensibilidad_de_oido_instrucciones, menu);
		return true;
	}

	public void onCancelarClick(View view) {
		this.finish();
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

	public void onEmpezarClick(View view) {

		long perfil = getIntent().getLongExtra("idPerfil", 0);
		long tipoExamen = getIntent().getLongExtra("idTipoExamen", 0);

		Intent intent = new Intent(view.getContext(),
				SensibilidadOidoExamen.class);
		intent.putExtra("idPerfil", perfil);
		intent.putExtra("idTipoExamen", tipoExamen);
		startActivity(intent);
	}
}
