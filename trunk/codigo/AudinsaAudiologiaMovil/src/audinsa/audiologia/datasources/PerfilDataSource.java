package audinsa.audiologia.datasources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
		ContentValues values = new ContentValues();	    
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_NOMBRE, nombre);
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO, df.format(fechaNacimiento));
		values.put(MySQLiteHelper.TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO, correoElectronico);

		

		database.insert(MySQLiteHelper.TABLA_PERFIL, null,
				values);
	}

	public Perfil buscarPerfil(long insertId){

		Cursor cursor = database.query(MySQLiteHelper.TABLA_PERFIL,
				allColumns, MySQLiteHelper.TABLA_PERFIL_COLUMNA_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Perfil newPerfil = cursorAPerfil(cursor);
		cursor.close();
		return newPerfil;

	}

	public void borrarPerfil(Perfil perfil) {
		long id = perfil.getIdPerfil();
		database.delete(MySQLiteHelper.TABLA_PERFIL, MySQLiteHelper.TABLA_PERFIL_COLUMNA_ID
				+ " = " + id, null);
	}

	public List<Perfil> obtenerTodosLosPerfiles() {
		List<Perfil> perfiles = new ArrayList<Perfil>();

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

		return perfil;
	}
}
