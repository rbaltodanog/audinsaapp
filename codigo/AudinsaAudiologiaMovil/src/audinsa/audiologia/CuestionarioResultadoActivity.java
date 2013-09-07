package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

}
