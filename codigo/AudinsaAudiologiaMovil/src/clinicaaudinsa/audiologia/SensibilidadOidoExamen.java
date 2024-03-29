package clinicaaudinsa.audiologia;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.media.*;
import clinicaaudinsa.audiologia.R;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.pheelicks.visualizer.VisualizerView;
import com.pheelicks.visualizer.renderer.LineRenderer;

import clinicaaudinsa.audiologia.businessdomain.ResultadoSensibilidadOido;
import clinicaaudinsa.audiologia.datasources.ResultadoDataSource;


public class SensibilidadOidoExamen extends Activity {
	private ArrayList<ResultadoSensibilidadOido> _sonidos;
	private ArrayList<ResultadoSensibilidadOido> _sonidosFinales;
	private ResultadoSensibilidadOido _currentSound;
	private MediaPlayer _mPlayer;
	private VisualizerView mVisualizerView;
	private DateTime _soundStarted;
	private Duration _timeToBeHeardDuration;
	int puntaje = 8;
	int contadorSonidos = 1;
	private ResultadoDataSource dataSource;
	private DateTime fechaInicioExamen;
	private ProgressDialog progress;

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
	protected void onCreate(Bundle savedInstanceState) {
		fechaInicioExamen = DateTime.now();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensibilidad_oido_examen);
		_sonidos = new ArrayList<ResultadoSensibilidadOido>();
		_sonidosFinales = new ArrayList<ResultadoSensibilidadOido>();
		_mPlayer = new MediaPlayer();
		FillSoundsArray();
		//initTunnelPlayerWorkaround();
		//init();
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
			Sound(_currentSound.getResId(),_currentSound.isDerecho(), _currentSound.getFrecuencia());
			_soundStarted = DateTime.now();
		}

	}

	private void FillSoundsArray()
	{
		Field[] fields=R.raw.class.getFields();
		for(int i=0; i < fields.length; i++){
			String filename = fields[i].getName();
			int frecuencia = Integer.parseInt(filename.substring(filename.indexOf("_") + 1, filename.indexOf("hz")));
			try {
				_sonidos.add(new ResultadoSensibilidadOido(false, true, fields[i].getInt(fields[i]), frecuencia, 0, false));
				_sonidos.add(new ResultadoSensibilidadOido(true, false, fields[i].getInt(fields[i]), frecuencia, 0, false));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Sound(int soundID, boolean derecho, int frequency){
		_mPlayer = MediaPlayer.create(getBaseContext(), soundID);
		TextView txtSonidoId = (TextView) findViewById(R.id.txtSonidoId);
		if (derecho)
		{
			_mPlayer.setVolume(0.0f, 1.0f);			
		}
		else
		{
			_mPlayer.setVolume(1.0f, 0.0f);
		}
		
		_mPlayer.start();
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
		    @Override
		    public void run() {
		    	progress.dismiss();
		    }
		};
		
		_mPlayer.start();

		//Show Progress dialog
		progress = ProgressDialog.show(this, "Sonido en progreso",
				  "Escuche el sonido", false);
		
		timer.schedule(task, 2000);
		
		txtSonidoId.setText(Integer.toString(frequency) + "hz (" + Integer.toString(contadorSonidos) + ")");
		mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
		mVisualizerView.setBackgroundColor(Color.rgb(227, 227, 227));
		mVisualizerView.link(_mPlayer);
		contadorSonidos++;
		// Start with just line renderer
		addLineRenderer();
	}

	private void addLineRenderer()
	{
		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(5f);
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.RED);

		Paint lineFlashPaint = new Paint();
		lineFlashPaint.setStrokeWidth(5f);
		lineFlashPaint.setAntiAlias(true);
		lineFlashPaint.setColor(Color.RED);
		LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, false);
		mVisualizerView.addRenderer(lineRenderer);
	}

	public void onIzquierdoClick(View view) {
		_mPlayer.stop();
		_timeToBeHeardDuration = new Duration(_soundStarted, DateTime.now());
		if (_currentSound.isIzquierdo())
		{
			_currentSound.setEquivocado(false);
		}
		else
		{
			_currentSound.setEquivocado(true);
			puntaje--;
		}
		_currentSound.setTiempoParaSerOido(_timeToBeHeardDuration.getMillis());
		_sonidosFinales.add(_currentSound);
		if (_sonidos.size() == 0)
		{
			guardarResultado(view);
		}
		else
		{
			PlayRandomSound();
		}
	}

	public void onDerechoClick(View view) {
		_mPlayer.stop();
		_timeToBeHeardDuration = new Duration(_soundStarted, DateTime.now());
		if (_currentSound.isDerecho())
		{
			_currentSound.setEquivocado(false);
		}
		else
		{
			_currentSound.setEquivocado(true);
			puntaje--;
		}
		_currentSound.setTiempoParaSerOido(_timeToBeHeardDuration.getMillis());
		_sonidosFinales.add(_currentSound);
		if (_sonidos.size() == 0)
		{
			guardarResultado(view);
		}
		else
		{
			PlayRandomSound();
		}
	}

	private void guardarResultado(View view) {
		long idPerfil, idTipoExamen;
		int valor_examen = 0;
		idPerfil = getIntent().getLongExtra("idPerfil", 0);
		idTipoExamen = getIntent().getLongExtra("idTipoExamen", 0);

		Intent intent = new Intent(view.getContext(),
				ResultadoActivity.class);
		if (puntaje == 8) {
			String positivo = this.getString(R.string.strResultadoPositivoExamen);
			intent.putExtra("strResultado", positivo);
			intent.putExtra("bolAprobado", true);
			valor_examen = 1;
		} else {
			String negativo = this.getString(R.string.strResultadoNegativoExamen);
			intent.putExtra("strResultado", negativo);
			intent.putExtra("bolAprobado", false);
		}

		Duration duracionExamen = new Duration(fechaInicioExamen, DateTime.now());

		dataSource = new ResultadoDataSource(this);
		dataSource.open();
		long idResultado=dataSource.crearResultado(idPerfil, idTipoExamen, valor_examen, fechaInicioExamen);
		dataSource.close();

		intent.putExtra("idPerfil", idPerfil);
		intent.putExtra("idResultado", idResultado);
		intent.putExtra("idTipoExamen", idTipoExamen);
		intent.putExtra("duracionExamen", duracionExamen.getStandardSeconds());

		startActivity(intent);
		this.finish();
	}
}
