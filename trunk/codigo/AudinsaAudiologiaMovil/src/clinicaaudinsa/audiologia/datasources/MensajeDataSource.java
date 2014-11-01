package clinicaaudinsa.audiologia.datasources;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

import clinicaaudinsa.audiologia.businessdomain.Mensaje;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MensajeDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.TABLA_MENSAJE_COLUMNA_ID,
			MySQLiteHelper.TABLA_MENSAJE_COLUMNA_LEIDO,
			MySQLiteHelper.TABLA_MENSAJE_COLUMNA_FECHA_MENSAJE,
			MySQLiteHelper.TABLA_MENSAJE_COLUMNA_TEXTO};

	public MensajeDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ArrayList<Mensaje> obtenerTodosLosMensajes() {
		open();
		ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();

		Cursor cursor = database.query(MySQLiteHelper.TABLA_MENSAJE,
				allColumns, null, null, null, null, null);

		if (cursor.getCount() == 0)
		{
			return mensajes;
		}
		
		cursor.moveToLast();
		while (!cursor.isFirst()) {
			Mensaje mensaje = cursorAMensaje(cursor);
			mensajes.add(mensaje);
			cursor.moveToPrevious();
		}
		// We still need to add the first one
		Mensaje mensaje = cursorAMensaje(cursor);
		mensajes.add(mensaje);
		// Make sure to close the cursor
		cursor.close();
		close();
		return mensajes;
	}
	
	private Mensaje cursorAMensaje(Cursor cursor) {
		Mensaje mensaje = new Mensaje();
		
		mensaje.setId_mensaje(cursor.getLong(0));
		SimpleDateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		
		mensaje.setLeido(cursor.getInt(1) == 1);
		
		Date dtFechaMensaje = new Date();
		
		try
		{
			dtFechaMensaje = dtFormat.parse(cursor.getString(2));
		}
		catch (ParseException ex)
		{
			Log.w(PerfilDataSource.class.getName(), "Error tratando de recuperar la fecha del mensaje. Seteando la fecha de nacimiento a la fecha mínima del sistema.");
			dtFechaMensaje = new Date(Long.MIN_VALUE);
		}
		
		mensaje.setFecha_mensaje(dtFechaMensaje);
		mensaje.setTexto(cursor.getString(3));

		return mensaje;
	}
	
	public void crearMensaje(boolean leido, String texto) {
		 
		open();
		ContentValues values = new ContentValues();	    
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		
		values.put(MySQLiteHelper.TABLA_MENSAJE_COLUMNA_LEIDO, leido ? 1 : 0);
		values.put(MySQLiteHelper.TABLA_MENSAJE_COLUMNA_FECHA_MENSAJE, df.format(DateTime.now().toDate()));
		values.put(MySQLiteHelper.TABLA_MENSAJE_COLUMNA_TEXTO, texto);

		database.insert(MySQLiteHelper.TABLA_MENSAJE, null,
				values);
		close();
	}
}
