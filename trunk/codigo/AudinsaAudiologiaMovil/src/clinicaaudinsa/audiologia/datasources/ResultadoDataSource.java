package clinicaaudinsa.audiologia.datasources;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import clinicaaudinsa.audiologia.businessdomain.Resultado;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
//import android.widget.TextView;


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
		values.put(MySQLiteHelper.TABLA_EXAMEN_COLUMNA_PORCENTAJE_COMPLETADO, valor_examen);
		//TODO FALTA DURACION APROX Y PORC DE EXAMEN
		long idExamen=database.insert(MySQLiteHelper.TABLA_EXAMEN, null,values);


		try {

			
			if (idExamen > 0)
			{
				values = new ContentValues();	  		
				values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL, idPerfil);
				values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_EXAMEN, idExamen);
				values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN, valor_examen);

				idResultado = database.insert(MySQLiteHelper.TABLA_RESULTADO, null,values);
			}
			else{
				Log.v("else de Error almacenando el resultado del examen","Excepci�n: " );
			}
		}
		catch(Exception ex){
			Log.v("Error almacenando el resultado del examen","Excepci�n: " + ex.getMessage());
		}
		return idResultado;


	}

	public Resultado buscarResultado(long  idResultado,long idPerfil){
		//busca todos los resultados para el perfil enviado
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLA_RESULTADO,
				allColumns, MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL + " = " + idPerfil  + " AND " + MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID + " = " + idResultado, null,
				null, null, null);
		cursor.moveToFirst();
		Resultado resultado = cursorAResultado(cursor);
		cursor.close();
		return resultado;

	}

	public boolean borrarResultado(long idResultado,long idPerfil) {
		boolean resultado = true;
		open();
		try{
			resultado = database.delete(MySQLiteHelper.TABLA_RESULTADO,MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL + " = " + idPerfil  + " AND "  +
					MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID+ " = " + idResultado, null) > 0;
		}	
		catch(Exception ex){

			Log.w(ResultadoDataSource.class.getName(),
					ex.getMessage());
			resultado = false;		
		}
		close();		
		return resultado;	
	}

	public ArrayList<Resultado> obtenerTodosLosResultados(long idPerfil) {
		//busca todos los resultados para el perfil enviado

		ArrayList<Resultado> resultados = new ArrayList<Resultado>();
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLA_RESULTADO,
				allColumns,  MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL + " = " + idPerfil, null, null, null, null);

		if (cursor.getCount() == 0)
		{
			return resultados;
		}
		
		cursor.moveToLast();
		while (!cursor.isFirst()) {
			Resultado resultado = cursorAResultado(cursor);
			resultados.add(resultado);
			cursor.moveToPrevious();
		}
		// We still need to add the first one
		Resultado resultado = cursorAResultado(cursor);
		resultados.add(resultado);
		// Make sure to close the cursor
		cursor.close();
		close();
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

	public boolean borrarTodosResultado(long idPerfil) {
		boolean resultado = false;
		open();
		try{
	        
			resultado = database.delete(MySQLiteHelper.TABLA_RESULTADO,MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL + " = " + idPerfil , null) > 0;
		}	
		catch(Exception ex){

			resultado = false;		
		}
		close();		
		return resultado;	
		}
	
	//Invoca al DS de examen, el mismo borra todos los exam asociados a un resultado
	public boolean borrarExamen(long idPerfil,Context context) {
		ExamenDataSource dataSourceExamen= new ExamenDataSource(context);
		boolean resultado= false;
		ArrayList<Resultado> ArregloResultados= obtenerTodosLosResultados(idPerfil);
			for (int i = 0; i < ArregloResultados.size(); i++) {
				 int idExamen=ArregloResultados.get(i).getId_examen();
				 resultado= dataSourceExamen.borrarExamen(idExamen);
				
			}
		return resultado;
	}
}