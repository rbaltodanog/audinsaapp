package audinsa.audiologia;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class SensibilidadDeOidoInstruccionesActivity extends Activity {
	private HeadSetReceiver _headSetReceiver;
	private boolean _headphoneIsConnected;
	private boolean _volumeFull;
	private AudioManager audioManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensibilidad_de_oido_instrucciones);
		// Check if the headphones are connected
		_headSetReceiver = new HeadSetReceiver();
		inicializarVolumenSeekBar();
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
		
		if (!_volumeFull)
		{
			Toast.makeText(
					getBaseContext(),
					getBaseContext()
							.getResources()
							.getString(
									R.string.txtSensibOidosTercerCheck)
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
	
	public void checkVolumeInstructions()
	{
		CheckBox chkPaso3InstrSensibilidad = (CheckBox)findViewById(R.id.chkPaso3InstrSensibilidad);
		chkPaso3InstrSensibilidad.setChecked(true);
		_volumeFull = true;
	}
	
	public void uncheckVolumeInstructions()
	{
		CheckBox chkPaso3InstrSensibilidad = (CheckBox)findViewById(R.id.chkPaso3InstrSensibilidad);
		chkPaso3InstrSensibilidad.setChecked(false);
		_volumeFull = false;
	}
	
	public void uncheckHeadphoneInstructions()
	{
		CheckBox chkPaso2InstrSensibilidad = (CheckBox)findViewById(R.id.chkPaso2InstrSensibilidad);
		chkPaso2InstrSensibilidad.setChecked(false);
		_headphoneIsConnected = false;
	}
	
	private void inicializarVolumenSeekBar()
    {
        try
        {
        	SeekBar volumeSeekbar = (SeekBar)findViewById(R.id.volumeBarSensibility);
        	audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            int volume = audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeSeekbar.setProgress(volume);
            if (volume == 15) //15 is max
			{
				checkVolumeInstructions();
			}
			else
			{
				uncheckVolumeInstructions();
			}
            
            volumeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
					if (progress == 15) //15 is max
					{
						checkVolumeInstructions();
					}
					else
					{
						uncheckVolumeInstructions();
					}
				}
			});
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}
