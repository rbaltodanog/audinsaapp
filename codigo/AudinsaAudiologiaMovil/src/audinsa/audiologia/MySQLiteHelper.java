package audinsa.audiologia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	/* TABLA PERFIL */
	public static final String TABLA_PERFIL = "perfil";
	public static final String TABLA_PERFIL_COLUMNA_ID = "id_perfil";
	public static final String TABLA_PERFIL_COLUMNA_NOMBRE = "nombre";
	public static final String TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO = "fecha_nacimiento";
	public static final String TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO = "correo_electronico";

	/* TABLA TIPOEXAMEN */
	public static final String TABLA_TIPOEXAMEN = "tipo_examen";
	public static final String TABLA_TIPOEXAMEN_COLUMNA_ID = "id_tipo_examen";
	public static final String TABLA_TIPOEXAMEN_COLUMNA_NOMBRE_EXAMEN = "nombre_examen";
	public static final String TABLA_TIPOEXAMEN_COLUMNA_INSTRUCCIONES = "instrucciones";

	private static final String NOMBRE_BASEDATOS = "audinsaaudiologia.db";
	private static final int VERSION_BASEDATOS = 1;

	// Database creation sql statement
	private static final String CREAR_TABLA_PERFIL = "create table "
			+ TABLA_PERFIL + "(" + TABLA_PERFIL_COLUMNA_ID
			+ " integer primary key autoincrement, " + TABLA_PERFIL_COLUMNA_NOMBRE
			+ " text not null, " + TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO
			+ " text not null, " + TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO
			+ " text not null);";
	
	private static final String CREAR_TABLA_TIPO_EXAMEN = "create table "
			+ TABLA_TIPOEXAMEN + "(" + TABLA_TIPOEXAMEN_COLUMNA_ID
			+ " integer primary key autoincrement, " + TABLA_TIPOEXAMEN_COLUMNA_NOMBRE_EXAMEN
			+ " text not null, " + TABLA_TIPOEXAMEN_COLUMNA_INSTRUCCIONES
			+ " text not null);";
	
	private static final String CREAR_TIPO_EXAMEN_SENSIBILIDAD_OIDO = "insert into " + TABLA_TIPOEXAMEN 
			+ " (" + TABLA_TIPOEXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPOEXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Sensibilidad de oído', '¿Cuál es el sonido más alto/bajo que puede oír?');";
	
	private static final String CREAR_TIPO_EXAMEN_HABLA_RUIDO = "insert into " + TABLA_TIPOEXAMEN 
			+ " (" + TABLA_TIPOEXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPOEXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Habla en ruido', '¿Que tan bien oye en ruido?');";
	
	private static final String CREAR_TIPO_EXAMEN_DIF_FRECUENCIAS = "insert into " + TABLA_TIPOEXAMEN 
			+ " (" + TABLA_TIPOEXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPOEXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Diferenciación de frecuencias', '¿Cuál es la más baja diferencia entre frecuencias que puede notar?');";
	
	private static final String CREAR_TIPO_EXAMEN_CUESTIONARIO = "insert into " + TABLA_TIPOEXAMEN 
			+ " (" + TABLA_TIPOEXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPOEXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Cuestionario', 'Preguntas acerca de su escucha diaria');";

	public MySQLiteHelper(Context context) {
		super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREAR_TABLA_PERFIL);
		database.execSQL(CREAR_TABLA_TIPO_EXAMEN);
		database.execSQL(CREAR_TIPO_EXAMEN_SENSIBILIDAD_OIDO);
		database.execSQL(CREAR_TIPO_EXAMEN_HABLA_RUIDO);
		database.execSQL(CREAR_TIPO_EXAMEN_DIF_FRECUENCIAS);
		database.execSQL(CREAR_TIPO_EXAMEN_CUESTIONARIO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Actualizando la base de datos de la version " + oldVersion + " a "
						+ newVersion + ", lo cual destruira la informacion contenida");
		db.execSQL("DROP TABLE IF EXISTS " + TABLA_PERFIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLA_TIPOEXAMEN);
		onCreate(db);
	}
} 