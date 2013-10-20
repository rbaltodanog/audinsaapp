package audinsa.audiologia;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.media.*;
import audinsa.audiologia.businessdomain.ResultadoSensibilidadOido;


public class SensibilidadOidoExamen extends Activity {
	private ArrayList<ResultadoSensibilidadOido> _sonidos;
	public ResultadoSensibilidadOido _currentSound;
	//private SoundPool _soundPool;
	private MediaPlayer _mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensibilidad_oido_examen);
		_sonidos = new ArrayList<ResultadoSensibilidadOido>();
		//_soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		_mPlayer = new MediaPlayer();
		FillSoundsArray();
		PlayRandomSound();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sensibilidad_oido_examen,
				menu);
		return true;
	}

	private void PlayRandomSound()
	{
		long seed = System.nanoTime();
		Collections.shuffle(_sonidos, new Random(seed));
		if (_sonidos.size() != 0)
		{
			_currentSound = _sonidos.get(0);
			_sonidos.remove(0);
			Sound(_currentSound.getResId(),_currentSound.isDerecho());
		}
		else
		{
			// Redireccionar a resultado
		}

	}

	private void FillSoundsArray()
	{
		Field[] fields=R.raw.class.getFields();
		for(int i=0; i < fields.length; i++){
			String filename = fields[i].getName();
			int frecuencia = Integer.parseInt(filename.substring(filename.indexOf("_") + 1, filename.indexOf("hz")));
			try {
				_sonidos.add(new ResultadoSensibilidadOido(false, true, fields[i].getInt(fields[i]), frecuencia, 0));
				_sonidos.add(new ResultadoSensibilidadOido(true, false, fields[i].getInt(fields[i]), frecuencia, 0));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Sound(int soundID, boolean derecho){
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		_mPlayer = MediaPlayer.create(getBaseContext(), soundID);
		_mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			public void onCompletion(MediaPlayer player) {
				player.stop();
				player.release();
				PlayRandomSound();
			}
		});
		float volume = (float)audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		if (derecho)
		{
			_mPlayer.setVolume(0.0f, volume);
			_mPlayer.start();
			//_soundPool.play(soundID, 0, volume, 1, 0, 1f);
		}
		else
		{
			_mPlayer.setVolume(volume, 0.0f);
			_mPlayer.start();
			//_soundPool.play(soundID, volume, 0, 1, 0, 1f);
		}
	};
}
