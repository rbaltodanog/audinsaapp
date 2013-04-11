package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class CuestionarioResultadoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//pantalla vertical
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cuestionario_resultado);
		
		TextView txtResultadoDescription = (TextView)findViewById(R.id.txtResultadoDescription);
		String strResultado=getIntent().getStringExtra("strResultado");	 
		txtResultadoDescription.setText(strResultado);
		
		
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present(pantalla horizontal).
		
		getMenuInflater().inflate(R.menu.activity_cuestionario_resultado, menu);		
		TextView txtResultadoDescription = (TextView)findViewById(R.id.txtResultadoDescription);
		String strResultado=getIntent().getStringExtra("strResultado");	 
		txtResultadoDescription.setText(strResultado);
				
		return true;
	}

}
