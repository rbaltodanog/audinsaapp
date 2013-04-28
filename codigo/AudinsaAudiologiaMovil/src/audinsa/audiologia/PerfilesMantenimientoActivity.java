package audinsa.audiologia;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.datasources.PerfilDataSource;

public class PerfilesMantenimientoActivity extends Activity {
	private PerfilDataSource dataSource;
	EditText txtNombre = null;
	EditText txtFechaNacimiento= null;
	EditText txtCorreoElectronico= null;
	Button btnAceptar= null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_perfiles_mantenimiento);
		if(getIntent().getBooleanExtra("actualizacion",false)){
			long idPerfil=getIntent().getLongExtra("idPerfil",0);
			Perfil p=onGetPerfil(idPerfil);
			if (p.getNombre()!= null){
				onActualizar(p);		
			}
		}

	}

	private void onActualizar(Perfil p) {

		txtNombre =(EditText)findViewById(R.id.txtNombre);
		txtFechaNacimiento=(EditText)findViewById(R.id.txtFechaNacimiento);
		txtCorreoElectronico=(EditText)findViewById(R.id.txtCorreoElectronico);
		btnAceptar=(Button)findViewById(R.id.btnAceptar);
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
		try{
			Date fecha=p.getFechaNacimiento();
			String dates = df.format(fecha);
			txtFechaNacimiento.setText(dates);
			txtNombre.setText(p.getNombre().toString());			 
			txtCorreoElectronico.setText(p.getCorreoElectronico().toString());
			btnAceptar.setText(this.getResources().getString(R.string.btnAceptarModificar));
		}
		catch(android.net.ParseException ex){
			Toast.makeText(getBaseContext(), getBaseContext().getResources().getString(R.string.txtMantenimientoPerfilesToastErrorParsearDatos) + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
			this.finish();
		} 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_perfiles_mantenimiento, menu);
		return true;
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onAgregarClick(View view) {
		dataSource = new PerfilDataSource(this);
		Date fechaNacimiento = null;
		String nombre = ((EditText)findViewById(R.id.txtNombre)).getText().toString();
		String fechaNacimientoText = ((EditText)findViewById(R.id.txtFechaNacimiento)).getText().toString();
		String correoElectronico = ((EditText)findViewById(R.id.txtCorreoElectronico)).getText().toString();
		SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		try
		{
			fechaNacimiento = dtFormat.parse(fechaNacimientoText);
		}
		catch (ParseException ex)
		{
			Log.w(PerfilDataSource.class.getName(), "Error tratando de agregar la fecha de nacimiento al nuevo perfil: " + nombre + ". Seteando la fecha de nacimiento a la fecha mínima del sistema.");
			fechaNacimiento = new Date(Long.MIN_VALUE);
		}

		dataSource.crearPerfil(nombre, fechaNacimiento, correoElectronico);
		Toast.makeText(getBaseContext(), getBaseContext().getResources().getString(R.string.txtMantenimientoPerfilesToastPerfilAgregado), Toast.LENGTH_SHORT).show();
		this.finish();		
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onCancelarClick(View view) {
		this.finish();		
	}


	//Obtiene el perfil por actualizar
	public Perfil onGetPerfil(long idPerfil) {
		dataSource = new PerfilDataSource(this);
		Perfil p= new Perfil();
		try
		{
			p=dataSource.buscarPerfil(idPerfil);
		}
		catch (Exception ex)
		{
			Log.w(PerfilDataSource.class.getName(), "Error tratando de obtener el perfil.");
		}
		return p;
	}

}
