package audinsa.audiologia.datasources;


import java.util.ArrayList;
import java.util.List;
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



	public void crearResultado(int id_perfil,int id_examen,
			int valor_examen,int duracion_real) {
		ContentValues values = new ContentValues();	    

		values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_PERFIL, id_perfil);
		values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_ID_EXAMEN,id_examen);
		values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN, valor_examen);
		values.put(MySQLiteHelper.TABLA_RESULTADO_COLUMNA_DURACION_REAL, duracion_real);

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

		//revisar lo de la duracion	
		return resultado;
	}
}
