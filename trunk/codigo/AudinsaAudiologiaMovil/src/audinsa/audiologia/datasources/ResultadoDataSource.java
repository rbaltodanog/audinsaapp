package audinsa.audiologia.datasources;


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
import audinsa.audiologia.businessdomain.Resultado;

public class ResultadoDataSource {


	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID,
			MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL,
			MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_EXAMEN,    
			MySQLiteHelper.TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN,
			MySQLiteHelper.TABLA_RESULTADO_COLUMNA_DURACION_REAL};

	public ResultadoDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	//Almacena primero el examen luego el resultado
	public void crearResultado(long idPerfil,long idTipoExamen,int valor_examen,Date fechaHoraInicio) {
		ContentValues values = new ContentValues();	    
		
 	//Fecha del sistema en este momento
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);  
		Date date = new Date(); 
		dateFormat.format(date);
		
		//convierte fecha inicio para almacenar en bd
		String fechaInicio=dateFormat.format(fechaHoraInicio);
		 
	
		//duracion_real debe de restar la HORA de este momento menos la HORA recibida que viene.
					
		String hh =  Integer.toString(date.getHours()-fechaHoraInicio.getHours());
		String mm = Integer.toString(date.getMinutes()-fechaHoraInicio.getMinutes());
		String ss = Integer.toString(date.getSeconds()-fechaHoraInicio.getHours());
 
		String duracionReal= hh+mm+ss;
		
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_ID_TIPO_EXAMEN, idTipoExamen);
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_FECHA_INICIO,fechaInicio);
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_DURACION_APROXIMADA,0);
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_PORCENTAJE_COMPLETADO,0);
	//TODO FALTA DURACION APROX Y PORC DE EXAMEN
		
		long idExamen=database.insert(MySQLiteHelper.TABLA_EXAMEN, null,values);
		
	 	values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL, idPerfil);
		values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_EXAMEN,idExamen);
		values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN, valor_examen);
		values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_DURACION_REAL, duracionReal);

		database.insert(MySQLiteHelper.TABLA_RESULTADO, null,values);		
		 
	}

	public Resultado buscarResultado(int insertId){
		//busca todos los resultados para el perfil enviado

		Cursor cursor = database.query(MySQLiteHelper.TABLA_RESULTADO,
				allColumns, MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Resultado resultado = cursorAResultado(cursor);
		cursor.close();
		return resultado;

	}

	public void borrarResultado(Resultado resultado) {
		int id = resultado.getId_resultado();
		database.delete(MySQLiteHelper.TABLA_RESULTADO, MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID
				+ " = " + id, null);
	}

	public List<Resultado> obtenerTodosLosResultados() {
		List<Resultado> resultados = new ArrayList<Resultado>();

		Cursor cursor = database.query(MySQLiteHelper.TABLA_RESULTADO,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Resultado resultado = cursorAResultado(cursor);
			resultados.add(resultado);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return resultados;
	}
	private Resultado cursorAResultado(Cursor cursor) {
		Resultado resultado = new Resultado();

		resultado.setId_resultado(cursor.getInt(0));
		resultado.setId_perfil(cursor.getInt(1));
		resultado.setId_examen(cursor.getInt(2));
		resultado.setValor_examen(cursor.getInt(3));
		resultado.setDuracion_real(cursor.getInt(4));
//TODO revisar que viaje bien la duracion
		//revisar lo de la duracion	
		return resultado;
	}
}
