package clinicaaudinsa.audiologia.datasources;

import java.util.ArrayList;

import clinicaaudinsa.audiologia.businessdomain.TipoExamen;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
//import audinsa.audiologia.businessdomain.Resultado;

public class TipoExamenDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.TABLA_TIPO_EXAMEN_COLUMNA_ID,
			MySQLiteHelper.TABLA_TIPO_EXAMEN_COLUMNA_NOMBRE_EXAMEN,
			MySQLiteHelper.TABLA_TIPO_EXAMEN_COLUMNA_INSTRUCCIONES};

	public TipoExamenDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ArrayList<TipoExamen> obtenerTodosLosTiposExamenes() {
		open();
		ArrayList<TipoExamen> tiposExamenes = new ArrayList<TipoExamen>();

		Cursor cursor = database.query(MySQLiteHelper.TABLA_TIPO_EXAMEN,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TipoExamen tipoExamen = cursorATipoExamen(cursor);
			if(cursor.getLong(0) != 2){ //Evita obtener el tipo 2 Habla en ruido por falta de definición del usuario
			tiposExamenes.add(tipoExamen);
			}
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		close();
		return tiposExamenes;
	}

	public TipoExamen buscarTipoExamen(int idTipoExamen){
		//busca un tipo de exámen por el id
		open();
		Cursor cursor = database.query(MySQLiteHelper.TABLA_TIPO_EXAMEN,
				allColumns, MySQLiteHelper.TABLA_TIPO_EXAMEN_COLUMNA_ID + " = " + idTipoExamen, null,
				null, null, null);
		cursor.moveToFirst();
		TipoExamen tipoExamen = cursorATipoExamen(cursor);
		cursor.close();
		return tipoExamen;

	}
	private TipoExamen cursorATipoExamen(Cursor cursor) {
		TipoExamen tipoExamen = new TipoExamen();
		tipoExamen.setIdTipoExamen(cursor.getLong(0));
		tipoExamen.setNombreExamen(cursor.getString(1));
		tipoExamen.setInstrucciones(cursor.getString(2));

		return tipoExamen;
	}
}
