package audinsa.audiologia;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.datasources.PerfilDataSource;

public class PerfilesMantenimientoActivity extends Activity {
	private PerfilDataSource dataSource;
	private EditText txtNombre = null;
	private EditText txtFechaNacimiento = null;
	private EditText txtCorreoElectronico = null;
	private Button btnAceptar = null;
	// Estas variables son para el date time picker
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfiles_mantenimiento);

		// Componentes de la pantalla
		txtFechaNacimiento = (EditText) findViewById(R.id.txtFechaNacimiento);
		// add click listener
		txtFechaNacimiento.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		// En caso de que el perfil se solicite mediante el context menu
		final Calendar c = Calendar.getInstance();
		if (getIntent().getBooleanExtra("actualizacion", false)) {
			long idPerfil = getIntent().getLongExtra("idPerfil", 0);
			Perfil p = onGetPerfil(idPerfil);
			DateFormat year = new SimpleDateFormat("yyyy", Locale.US);
			if (p != null) {
				onActualizar(p);
				mYear = Integer.valueOf(year.format(p.getFechaNacimiento()));
				mMonth = p.getFechaNacimiento().getMonth();
				mDay = p.getFechaNacimiento().getDate();
			}
		} else {
			// GET CURRENT DATE
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	// Actualiza la fecha del date time picker
	private void updateDisplay() {
		StringBuilder fecha = new StringBuilder().append(mDay).append("/")
				.append(mMonth + 1).append("/").append(mYear);
		txtFechaNacimiento.setText(fecha);
	}

	private void onActualizar(Perfil p) {
		txtNombre = (EditText) findViewById(R.id.txtNombre);
		txtCorreoElectronico = (EditText) findViewById(R.id.txtCorreoElectronico);
		txtFechaNacimiento = (EditText) findViewById(R.id.txtFechaNacimiento);
		btnAceptar = (Button) findViewById(R.id.btnAceptarPerfilMantenimiento);
		DateFormat df = new SimpleDateFormat("d/M/yyyy", Locale.US);
		try {
			Date fecha = p.getFechaNacimiento();
			String dates = df.format(fecha);
			txtFechaNacimiento.setText(dates);
			txtNombre.setText(p.getNombre().toString());
			txtCorreoElectronico.setText(p.getCorreoElectronico().toString());
			btnAceptar.setText(this.getResources().getString(
					R.string.btnAceptarModificar));
		} catch (android.net.ParseException ex) {
			Toast.makeText(
					getBaseContext(),
					getBaseContext()
							.getResources()
							.getString(
									R.string.txtMantenimientoPerfilesToastErrorParsearDatos)
							+ ": " + ex.getMessage(), Toast.LENGTH_SHORT)
					.show();
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
		String nombre = ((EditText) findViewById(R.id.txtNombre)).getText()
				.toString();
		String fechaNacimientoText = txtFechaNacimiento.getText().toString();
		String correoElectronico = ((EditText) findViewById(R.id.txtCorreoElectronico))
				.getText().toString();
		SimpleDateFormat dtFormat = new SimpleDateFormat("d/M/yyyy", Locale.US);

		long idPerfil = getIntent().getLongExtra("idPerfil", -1);

		try {
			fechaNacimiento = (Date) dtFormat.parse(fechaNacimientoText);
		} catch (ParseException ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de agregar la fecha de nacimiento al nuevo perfil: "
							+ nombre
							+ ". Estableciendo la fecha de nacimiento a la fecha mínima del sistema.");
			fechaNacimiento = new Date(Long.MIN_VALUE);
		}
		
		if(validarPerfil(nombre,correoElectronico)){
		if (idPerfil != -1) {

			int resultado = dataSource.actualizarPerfil(nombre,
					fechaNacimiento, correoElectronico, idPerfil);
			if (resultado == 1)
				Toast.makeText(
						getBaseContext(),
						getBaseContext()
								.getResources()
								.getString(
										R.string.txtMantenimientoPerfilesToastPerfilActualizado),
						Toast.LENGTH_SHORT).show();
			
			
		} else {
			dataSource.crearPerfil(nombre, fechaNacimiento, correoElectronico);
			Toast.makeText(
					getBaseContext(),
					getBaseContext()
							.getResources()
							.getString(
									R.string.txtMantenimientoPerfilesToastPerfilAgregado),
					Toast.LENGTH_SHORT).show();
			
		}
		finish();
		}
		else{			
			
			Toast.makeText(
						getBaseContext(),
						"Debe de completar la información solicitada",
						Toast.LENGTH_SHORT).show();
		}
		
	}
		
	
	private boolean validarPerfil(String nombre,String correoElectronico) {
		boolean bandera=true;
		
		if(nombre.trim().length()<= 0  || correoElectronico.trim().length()<= 0 )
		   bandera=false;
				
		return bandera;
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onCancelarClick(View view) {
		this.finish();
	}

	// Obtiene el perfil por actualizar
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_salir:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	

}