package audinsa.audiologia;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import audinsa.audiologia.businessdomain.Cuestionario;
import audinsa.audiologia.datasources.ResultadoDataSource;

public class CuestionarioExamenActivity extends Activity {
	private Cuestionario cuestionario;
	private ResultadoDataSource dataSource;
	int puntaje = 0;
	TextView lblPregunta = null;
	DateTime fechaInicioExamen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String[] preguntas = null;
		fechaInicioExamen = DateTime.now();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuestionario_examen);
		cuestionario = new Cuestionario();
		lblPregunta = (TextView) findViewById(R.id.lblPregunta);
		Resources res = this.getResources();
		preguntas = res.getStringArray(R.array.Preguntas);
		for (int i = 0; i < preguntas.length; i++) {
			cuestionario.getPreguntas().add(preguntas[i]);
		}

		// Mostrar primera pregunta
		obtenerSiguientePregunta();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_cuestionario_examen, menu);
		return true;
	}

	private void guardarResultado(View view) {
		long idPerfil, idTipoExamen;
		int valor_examen = 0;
		idPerfil = getIntent().getLongExtra("idPerfil", 0);
		idTipoExamen = getIntent().getLongExtra("idTipoExamen", 0);

		Intent intent = new Intent(view.getContext(),
				ResultadoActivity.class);
		if (puntaje >= 8) {
			String positivo = this.getString(R.string.strResultadoPositivo);
			intent.putExtra("strResultado", positivo);
			intent.putExtra("bolAprobado", true);
			valor_examen = 1;
		} else {
			String negativo = this.getString(R.string.strResultadoNegativo);
			intent.putExtra("strResultado", negativo);
			intent.putExtra("bolAprobado", false);
		}
		
		dataSource = new ResultadoDataSource(this);
		dataSource.open();
		Duration duracionExamen = new Duration(fechaInicioExamen, DateTime.now());
		long idResultado=dataSource.crearResultado(idPerfil, idTipoExamen, valor_examen,fechaInicioExamen);
		dataSource.close();
			
		intent.putExtra("idPerfil", idPerfil);
		intent.putExtra("idResultado", idResultado);
		intent.putExtra("idTipoExamen", idTipoExamen);
		intent.putExtra("duracionExamen", duracionExamen.getStandardSeconds());
		
		startActivity(intent);
		this.finish();
	}

	public void btnSiClick(View view) {

		if (cuestionario.getPreguntas().size() == 0) {
			// TODO:guardar resultado e Ir a pantalla de resultados
			guardarResultado(view);
		} else {
			obtenerSiguientePregunta();

		}
	}

	public void btnNoClick(View view) {
		if (cuestionario.getPreguntas().size() == 0) {
			guardarResultado(view);
		} else {
			puntaje = cuestionario.getPuntaje();
			puntaje++;
			cuestionario.setPuntaje(puntaje);
			obtenerSiguientePregunta();
		}
	}

	// Obtiene la siguiente pregunta valida del arreglo y la elimina del mismo.
	public void obtenerSiguientePregunta() {
		lblPregunta.setText(cuestionario.getPreguntas().get(0));
		cuestionario.getPreguntas().remove(0);

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
