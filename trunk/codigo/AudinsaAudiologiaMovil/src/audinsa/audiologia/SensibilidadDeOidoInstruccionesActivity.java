package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class SensibilidadDeOidoInstruccionesActivity extends Activity {
	private HeadSetReceiver _headSetReceiver;
	private boolean _headphoneIsConnected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensibilidad_de_oido_instrucciones);
		// Check if the headphones are connected
		_headSetReceiver = new HeadSetReceiver();
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
		if (!_headphoneIsConnected)
		{
			Toast.makeText(
					getBaseContext(),
					getBaseContext()
							.getResources()
							.getString(
									R.string.txtSensibOidosSegundoCheck)
							, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		
		long perfil = getIntent().getLongExtra("idPerfil", 0);
		long tipoExamen = getIntent().getLongExtra("idTipoExamen", 0);		
		Intent intent = new Intent(view.getContext(),
				SensibilidadOidoExamen.class);
		intent.putExtra("idPerfil", perfil);
		intent.putExtra("idTipoExamen", tipoExamen);
		startActivity(intent);
	}
	
	public void onResume() {
	    IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
	    registerReceiver(_headSetReceiver, filter);
	    super.onResume();
	}
	
	public void checkHeadphoneInstructions()
	{
		CheckBox chkPaso2InstrSensibilidad = (CheckBox)findViewById(R.id.chkPaso2InstrSensibilidad);
		chkPaso2InstrSensibilidad.setChecked(true);
		_headphoneIsConnected = true;
	}
	
	public void uncheckHeadphoneInstructions()
	{
		CheckBox chkPaso2InstrSensibilidad = (CheckBox)findViewById(R.id.chkPaso2InstrSensibilidad);
		chkPaso2InstrSensibilidad.setChecked(false);
		_headphoneIsConnected = false;
	}
}
