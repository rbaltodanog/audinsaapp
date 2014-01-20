package audinsa.audiologia.datasources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import audinsa.audiologia.businessdomain.Perfil;
 
public class PerfilDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.TABLA_PERFIL_COLUMNA_ID,
			MySQLiteHelper.TABLA_PERFIL_COLUMNA_NOMBRE,
			MySQLiteHelper.TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO,
			MySQLiteHelper.TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO};

	public PerfilDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void crearPerfil(String nombre, Date fechaNacimiento, String correoElectronico) {
		 
		open();
		ContentValues values = new ContentValues();	    
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_NOMBRE, nombre);
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO, df.format(fechaNacimiento));
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO, correoElectronico);

		database.insert(MySQLiteHelper.TABLA_PERFIL, null,
				values);
		close();
	}
	public int actualizarPerfil(String nombre, Date fechaNacimiento, String correoElectronico,long id) {
		int resultado=0; 
		open();
		ContentValues values = new ContentValues();	    
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_NOMBRE, nombre);
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO, df.format(fechaNacimiento));
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO, correoElectronico);

		resultado= database.update(MySQLiteHelper.TABLA_PERFIL,values,MySQLiteHelper.TABLA_PERFIL_COLUMNA_ID+ " = " + id,null);
		close();
		
		return resultado; 
	}
	public Perfil buscarPerfil(long insertId){
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLA_PERFIL,
				allColumns, MySQLiteHelper.TABLA_PERFIL_COLUMNA_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Perfil newPerfil = cursorAPerfil(cursor);
		cursor.close();
		close();
		return newPerfil;

	}

	public boolean borrarPerfil(long idPerfil, Context context) {
		boolean resultadoEliminarPerfil = false;
		
		try {
		
			if(borrarDependenciasPerfil(idPerfil,context)){	
				 // si ya elimina las dependencias debe de intentar eliminar el perfil
				resultadoEliminarPerfil= eliminarPerfil(idPerfil);
			 }
		}
		catch(Exception ex){		
			resultadoEliminarPerfil = false;			
		}
		
		return resultadoEliminarPerfil;
}
	private boolean borrarDependenciasPerfil(long idPerfil,Context context) {
		boolean resultado = false;
		ResultadoDataSource dataSourceResultado= new ResultadoDataSource(context);
	    dataSourceResultado.borrarExamen(idPerfil,context);      	
		resultado = dataSourceResultado.borrarTodosResultado(idPerfil);

		return resultado;
	}

	public boolean  eliminarPerfil(long idPerfil) {
		boolean r= false;  	
		 open();
		 r= database.delete(MySQLiteHelper.TABLA_PERFIL, MySQLiteHelper.TABLA_PERFIL_COLUMNA_ID + " = " + idPerfil, null) > 0;
		 close();
		 
		 return r;
	}
	
	public ArrayList<Perfil> obtenerTodosLosPerfiles() {
		open();
		ArrayList<Perfil> perfiles = new ArrayList<Perfil>();

		Cursor cursor = database.query(MySQLiteHelper.TABLA_PERFIL,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Perfil perfil = cursorAPerfil(cursor);
			perfiles.add(perfil);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return perfiles;
	}

	private Perfil cursorAPerfil(Cursor cursor) {
		Perfil perfil = new Perfil();
		SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		Date dtFechaNacimiento = new Date();
		perfil.setIdPerfil(cursor.getInt(0));
		perfil.setNombre(cursor.getString(1));
		
		try
		{
			dtFechaNacimiento = dtFormat.parse(cursor.getString(2));
		}
		catch (ParseException ex)
		{
			Log.w(PerfilDataSource.class.getName(), "Error tratando de recuperar la fecha de nacimiento del perfil: " + perfil.getNombre() + ". Seteando la fecha de nacimiento a la fecha mínima del sistema.");
			dtFechaNacimiento = new Date(Long.MIN_VALUE);
		}

		perfil.setFechaNacimiento(dtFechaNacimiento);
		perfil.setCorreoElectronico(cursor.getString(3));

		return perfil;
	}
}
