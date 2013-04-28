package audinsa.audiologia;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.datasources.PerfilDataSource;

public class PerfilesMantenimientoActivity extends Activity {
	private PerfilDataSource dataSource;
	EditText txtNombre = null;
	EditText txtFechaNacimiento= null;
	EditText txtCorreoElectronico= null;	


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
		//this..setText(cuestionario.getPreguntas().get(0));
		//lblPregunta = (TextView)findViewById(R.id.lblPregunta);
		 
		 txtNombre =(EditText)findViewById(R.id.txtNombre);
		 txtFechaNacimiento=(EditText)findViewById(R.id.txtFechaNacimiento);
		 txtCorreoElectronico=(EditText)findViewById(R.id.txtCorreoElectronico);		
		 String dates="270387";
		
		 try{
	 txtFechaNacimiento.setText(dates);
		 }
		catch(android.net.ParseException d){
			 
			 
			 
		 }
		 txtNombre.setText(p.getNombre().toString());			 
		txtCorreoElectronico.setText(p.getCorreoElectronico().toString());
		 
		
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
		dataSource.open();
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
			Log.w(PerfilDataSource.class.getName(), "Error tratando de agregar la fecha de nacimiento al nuevo perfil: " + nombre + ". Seteando la fecha de nacimiento a la fecha m�nima del sistema.");
			fechaNacimiento = new Date(Long.MIN_VALUE);
		}
		
		dataSource.crearPerfil(nombre, fechaNacimiento, correoElectronico);
		dataSource.close();
		
		//TODO: Agregar pop up de creado de perfil exitoso
		
		this.finish();		
	}
	
	
	//obtiene el perfil por actualizar
		public Perfil onGetPerfil(long idPerfil) {
				
			dataSource = new PerfilDataSource(this);
			dataSource.open();
			Perfil p= new Perfil();
			try
			{
				p=dataSource.buscarPerfil(idPerfil);
			}
			catch (Exception ex)
			{
				Log.w(PerfilDataSource.class.getName(), "Error tratando de obtener el perfil.");
			}
			
			dataSource.close();
			
		return p;
		}

}