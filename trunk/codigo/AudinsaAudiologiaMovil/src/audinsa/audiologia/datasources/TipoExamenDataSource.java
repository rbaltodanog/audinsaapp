package audinsa.audiologia.datasources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import audinsa.audiologia.businessdomain.TipoExamen;

public class TipoExamenDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.TABLA_TIPOEXAMEN_COLUMNA_ID,
			MySQLiteHelper.TABLA_TIPOEXAMEN_COLUMNA_NOMBRE_EXAMEN,
			MySQLiteHelper.TABLA_TIPOEXAMEN_COLUMNA_INSTRUCCIONES};

	public TipoExamenDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public List<TipoExamen> obtenerTodosLosTiposExamenes() {
		List<TipoExamen> tiposExamenes = new ArrayList<TipoExamen>();

		Cursor cursor = database.query(MySQLiteHelper.TABLA_TIPOEXAMEN,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TipoExamen tipoExamen = cursorATipoExamen(cursor);
			tiposExamenes.add(tipoExamen);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return tiposExamenes;
	}
	
	public List<Map<String, String>> obtenerTodosLosTiposExamenesTwoLineItems() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> data = null;

		Cursor cursor = database.query(MySQLiteHelper.TABLA_TIPOEXAMEN,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TipoExamen tipoExamen = cursorATipoExamen(cursor);
			data =  new HashMap<String, String>(2);
			data.put("Nombre", tipoExamen.getNombreExamen());
			data.put("Instrucciones", tipoExamen.getInstrucciones());
			list.add(data);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return list;
	}

	private TipoExamen cursorATipoExamen(Cursor cursor) {
		TipoExamen tipoExamen = new TipoExamen();
		tipoExamen.setIdTipoExamen(cursor.getLong(0));
		tipoExamen.setNombreExamen(cursor.getString(1));
		tipoExamen.setInstrucciones(cursor.getString(2));

		return tipoExamen;
	}
}
