package audinsa.audiologia.datasources;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import audinsa.audiologia.businessdomain.Resultado;

public class ResultadoDataSource {


	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID,
			MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL,
			MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_EXAMEN,    
			MySQLiteHelper.TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN};

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
	public long crearResultado(long idPerfil,long idTipoExamen,int valor_examen, DateTime fechaHoraInicio) {
		ContentValues values = new ContentValues();	    
		long idResultado=0;
		// Convierte fecha inicio para almacenar en bd
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
		String fechaInicio = dateFormat.print(fechaHoraInicio);
		
		Duration duracionExamen = new Duration(fechaHoraInicio, DateTime.now());

		//String duracionReal = duracionExamen.getStandardMinutes() + " minutos : " + duracionExamen.getStandardSeconds() + " segundos";

		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_ID_TIPO_EXAMEN, idTipoExamen);
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_FECHA_INICIO, fechaInicio);
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_DURACION_REAL, duracionExamen.getStandardSeconds());
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_PORCENTAJE_COMPLETADO, 100);
		//TODO FALTA DURACION APROX Y PORC DE EXAMEN


		try {
		
		long idExamen=database.insert(MySQLiteHelper.TABLA_EXAMEN, null,values);

			if (idExamen > 0)
			{
				values = new ContentValues();	  		
				values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL, idPerfil);
				values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_EXAMEN, idExamen);
				values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN, valor_examen);
	
				idResultado = database.insert(MySQLiteHelper.TABLA_RESULTADO, null,values);
			}
		}
		catch(Exception ex){
			Log.v("Error almacenando el resultado del examen","Excepción: " + ex.getMessage());
		}
		return idResultado;
		

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
		return resultado;
	}
}
