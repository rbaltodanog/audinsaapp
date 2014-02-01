package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
//import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import audinsa.audiologia.businessdomain.CompartirResultado;
import audinsa.audiologia.businessdomain.Perfil;
//mport audinsa.audiologia.businessdomain.Resultado;
import audinsa.audiologia.datasources.PerfilDataSource;

public class CuestionarioResultadoActivity extends Activity {

	private CompartirResultado C;
	private PerfilDataSource dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// pantalla vertical
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuestionario_resultado);

		TextView txtResultadoDescription = (TextView) findViewById(R.id.txtResultadoDescription);
		String strResultado = getIntent().getStringExtra("strResultado");
		txtResultadoDescription.setText(strResultado);
		// Cambia la imagen del sem�foro si esta aprobado o reprobado
		boolean aprobado = getIntent().getBooleanExtra("bolAprobado", true);
		ImageView img = (ImageView) findViewById(R.id.imgViewCuestionarioAprobado);
	 	if (aprobado) {
			img.setImageResource(R.drawable.animation_resultado_aprobado);
		} else {
			img.setImageResource(R.drawable.animation_resultado_reprobado);
		}
		
		TableRow rowContactarClinica = (TableRow)findViewById(R.id.row_contactar_clinica);			
		rowContactarClinica.setClickable(true);
		rowContactarClinica.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String estado=(getIntent().getBooleanExtra("bolAprobado", true)) ? "Aprobado" : "Contacte un especialista";
				long idPerfil = getIntent().getLongExtra("idPerfil", 0);
				Perfil p = onGetPerfil(idPerfil);
				C.contactarClinica(estado,p);
			}
		});		
		
		TableRow rowCompartirResultado = (TableRow)findViewById(R.id.row_compartir_resultado);			
		rowCompartirResultado.setClickable(true);
		rowCompartirResultado.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String estado=(getIntent().getBooleanExtra("bolAprobado", true)) ? "Aprobado" : "Contacte un especialista";
				C.compartirInformacion(estado,v.getContext());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present(pantalla horizontal).

		getMenuInflater().inflate(R.menu.activity_cuestionario_resultado, menu);
		@SuppressWarnings("unused")
		TextView txtResultadoDescription = (TextView) findViewById(R.id.txtResultadoDescription);

		return true;
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
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);

		ImageView img = (ImageView) findViewById(R.id.imgViewCuestionarioAprobado);
		AnimationDrawable frameAnimation = (AnimationDrawable) img
				.getDrawable();
		frameAnimation.setCallback(img);
		frameAnimation.setVisible(true, true);
		frameAnimation.start();
	}
	
	public void onRegresarClick(View view) {
		this.finish();
	}
	


	// Obtiene el perfil por compartir
	public Perfil onGetPerfil(long idPerfil) {
		dataSource = new PerfilDataSource(this);
		Perfil p = new Perfil();
		try {
			p = dataSource.buscarPerfil(idPerfil);
		} catch (Exception ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de obtener el perfil.");
		}
		return p;
	}

	
}
