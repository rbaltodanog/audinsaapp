package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import audinsa.audiologia.businessdomain.Cuestionario;

public class CuestionarioExamenActivity extends Activity {
	private Cuestionario cuestionario;
	TextView lblPregunta = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String[] preguntas = null;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuestionario_examen);
		cuestionario = new Cuestionario();
		lblPregunta = (TextView)findViewById(R.id.lblPregunta);
		Resources res = this.getResources();
		preguntas = res.getStringArray(R.array.Preguntas);
		for (int i = 0; i < preguntas.length; i++)
		{
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

	public void btnSiClick(View view)
	{

		int puntaje=0;
		if (cuestionario.getPreguntas().size() == 0)
		{
			//TODO: Ir a pantalla de resultados
		}
		else
		{
			puntaje=cuestionario.getPuntaje();
			puntaje++;
			cuestionario.setPuntaje(puntaje);
			obtenerSiguientePregunta();	

		}
	}

	public void btnNoClick(View view)
	{
		if (cuestionario.getPreguntas().size() == 0)
		{
			//TODO: Ir a pantalla de resultados
		}
		else
		{
			obtenerSiguientePregunta();	
		}
	}

	//Obtiene la siguiente pregunta válida del arreglo y la elimina del mismo.
	public void obtenerSiguientePregunta(){
		lblPregunta.setText(cuestionario.getPreguntas().get(0));
		cuestionario.getPreguntas().remove(0);

	}
}
