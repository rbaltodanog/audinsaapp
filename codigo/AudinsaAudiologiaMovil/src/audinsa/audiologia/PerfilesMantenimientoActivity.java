package audinsa.audiologia;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.datasources.PerfilDataSource;
import android.widget.TextView;

public class PerfilesMantenimientoActivity extends Activity {
	private PerfilDataSource dataSource;
	private EditText txtNombre = null;
	private TextView txtFechaNacimiento= null;
	private EditText txtCorreoElectronico= null;
	private Button btnAceptar= null;
//Estas variables son para el date time picker
	private TextView mDateDisplay; 
	private Button mPickDate; 
	private int mYear; 
	private int mMonth; 
	private int mDay; 
	static final int DATE_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_perfiles_mantenimiento);
		
		// En caso de que el perfil se solicite mediante el context menu
		if(getIntent().getBooleanExtra("actualizacion",false)){
			long idPerfil=getIntent().getLongExtra("idPerfil",0);
			Perfil p=onGetPerfil(idPerfil);
			if (p!= null){
				onActualizar(p);		
			}
		}
		// componentes de la pantalla
		mDateDisplay=(TextView)findViewById(R.id.dateDisplay);
		mPickDate=(Button)findViewById(R.id.pickDate);
		//add click listener
		mPickDate.setOnClickListener(new View.OnClickListener()
		{
		     public void onClick(View v){
			showDialog(DATE_DIALOG_ID);
		}
		});
		//GET CURRENT DATE

		final Calendar c= Calendar.getInstance();
		mYear=c.get(Calendar.YEAR);
		mMonth=c.get(Calendar.MONTH);
		mDay=c.get(Calendar.DAY_OF_MONTH);


		updateDisplay();
	}
  

	   protected Dialog onCreateDialog(int id) {
	        switch (id) {
	 
	        case DATE_DIALOG_ID:
	            return new DatePickerDialog(this,
	                mDateSetListener,
	                mYear, mMonth, mDay);
	        }
	        return null;
	    }
			
   private DatePickerDialog.OnDateSetListener mDateSetListener =
	        new DatePickerDialog.OnDateSetListener() {
	 
	        public void onDateSet(DatePicker view, int year, int monthOfYear,
	                int dayOfMonth) {
	            mYear = year;
	            mMonth = monthOfYear;
	            mDay = dayOfMonth;
	            updateDisplay();
	        }
	    };
	    // actualiza la fecha del date time picker
	private void updateDisplay(){
		
		
		StringBuilder fecha=  new StringBuilder()
			.append(mDay).append("-") 
			.append(mMonth + 1).append("-") 
			.append(mYear).append(" "); 
	mDateDisplay.setText(fecha); 
mDateDisplay.setEnabled(false);
mPickDate.setText(fecha);
	}

	private void onActualizar(Perfil p) {
		

		txtNombre =(EditText)findViewById(R.id.txtNombre);
		txtFechaNacimiento=(TextView)findViewById(R.id.dateDisplay);
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
		String fechaNacimientoText = ((TextView)findViewById(R.id.dateDisplay)).getText().toString();
		String correoElectronico = ((EditText)findViewById(R.id.txtCorreoElectronico)).getText().toString();
		SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		long idPerfil=getIntent().getLongExtra("idPerfil",-1);

		
		
		try
		{
			fechaNacimiento = (Date) dtFormat.parse(fechaNacimientoText);
		}
		catch (ParseException ex)
		{
			Log.w(PerfilDataSource.class.getName(), "Error tratando de agregar la fecha de nacimiento al nuevo perfil: " + nombre + ". Seteando la fecha de nacimiento a la fecha mínima del sistema.");
			fechaNacimiento = new Date(Long.MIN_VALUE);
		}
		if(idPerfil != -1){

		   int resultado= dataSource.actualizarPerfil(nombre, fechaNacimiento, correoElectronico, idPerfil);
		 	if(resultado==1)
		 		Toast.makeText(getBaseContext(), getBaseContext().getResources().getString(R.string.txtMantenimientoPerfilesToastPerfilActualizado), Toast.LENGTH_SHORT).show();
		}
		else { 
		     dataSource.crearPerfil(nombre, fechaNacimiento, correoElectronico);
		 	Toast.makeText(getBaseContext(), getBaseContext().getResources().getString(R.string.txtMantenimientoPerfilesToastPerfilAgregado), Toast.LENGTH_SHORT).show();
    	}
	
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
