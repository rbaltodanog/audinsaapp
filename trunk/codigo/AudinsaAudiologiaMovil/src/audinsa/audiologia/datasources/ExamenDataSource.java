package audinsa.audiologia.datasources;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import audinsa.audiologia.businessdomain.Examen;

public class ExamenDataSource {
	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.TABLA_EXAMEN_COLUMNA_ID,
			MySQLiteHelper.TABLA_EXAMEN_COLUMNA_ID_TIPO_EXAMEN, /* llave fK tipo exam*/
			MySQLiteHelper.TABLA_EXAMEN_COLUMNA_FECHA_INICIO,
			MySQLiteHelper.TABLA_EXAMEN_COLUMNA_DURACION_REAL,
			MySQLiteHelper.TABLA_EXAMEN_COLUMNA_PORCENTAJE_COMPLETADO

	};

	public ExamenDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	public Examen buscarExamen(int insertId){
		 open();
		Cursor cursor = database.query(MySQLiteHelper.TABLA_EXAMEN,
				allColumns, MySQLiteHelper.TABLA_EXAMEN_COLUMNA_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Examen examen = cursorAExamen(cursor);
		cursor.close();
		return examen;
	}

	public List<Examen> obtenerTodosLosExamenes() {
		List<Examen> examenes = new ArrayList<Examen>();

		Cursor cursor = database.query(MySQLiteHelper.TABLA_EXAMEN,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Examen examen = cursorAExamen(cursor);
			examenes.add(examen);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return examenes;
	}


	private Examen cursorAExamen(Cursor cursor) {
		String fechaInicio;
		Examen examen = new Examen();
		examen.setId_examen(cursor.getInt(0));
		examen.setId_tipo_examen(cursor.getInt(1));
		fechaInicio=cursor.getString(2);
		Date  Fincio;
		Fincio= new Date(fechaInicio);
		examen.setFecha_inicio(Fincio);	

		examen.setDuracion_real(cursor.getString(3));
		examen.setPorcentaje_completado(cursor.getDouble(4));


		return examen;
	}

	public boolean borrarExamen(long idExamen) {
		open();
		boolean resultado = database.delete(MySQLiteHelper.TABLA_EXAMEN, MySQLiteHelper.TABLA_EXAMEN_COLUMNA_ID
				+ " = " + idExamen, null) > 0;
		close();
		return resultado;
	}
}
