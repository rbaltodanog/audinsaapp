package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.businessdomain.Resultado;
import audinsa.audiologia.datasources.PerfilDataSource;

public class CuestionarioResultadoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// pantalla vertical
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuestionario_resultado);

		TextView txtResultadoDescription = (TextView) findViewById(R.id.txtResultadoDescription);
		String strResultado = getIntent().getStringExtra("strResultado");
		txtResultadoDescription.setText(strResultado);
		// Cambia la imagen del semaforo si esta aprobado o reprobado
		boolean aprobado = getIntent().getBooleanExtra("bolAprobado", true);
		ImageView img = (ImageView) findViewById(R.id.imgViewCuestionarioAprobado);
		if (aprobado) {
			img.setImageResource(R.drawable.animation_resultado_aprobado);
		} else {
			img.setImageResource(R.drawable.animation_resultado_reprobado);
		}
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
	
	private PerfilDataSource dataSource;
	

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
	public void onCompartir(View view) {
          	Intent contactIntent = new Intent(Intent.ACTION_SEND);
			contactIntent.setType("text/plain");
			//contactIntent.setType("message/rfc822"); //set the email recipient
			long idPerfil = getIntent().getLongExtra("idPerfil", 0);
			Perfil p = onGetPerfil(idPerfil);
			String shareBody = "Estoy interesado en obtener una cita médica mis datos son:"+p.toString();
			contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
			contactIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
			String[] mail = { "daniulate@example.com"," " };
			contactIntent.putExtra(Intent.EXTRA_EMAIL, mail);   
            startActivity(Intent.createChooser(contactIntent, "Compartir usando"));
  	
	}
	
	public void borrarResultado(){
		
		long idPerfil = getIntent().getLongExtra("idPerfil", 0);
		Perfil p = onGetPerfil(idPerfil);
	    
		
		
	} 
	
}
