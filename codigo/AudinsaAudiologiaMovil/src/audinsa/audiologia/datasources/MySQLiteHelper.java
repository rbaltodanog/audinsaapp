package audinsa.audiologia.datasources;

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
	
	/* TABLA EXAMENES */
	public static final String TABLA_EXAMEN = "examen";
	public static final String TABLA_EXAMEN_COLUMNA_ID = "id_examen";
	public static final String TABLA_EXAMEN_COLUMNA_ID_TIPO_EXAMEN = "id_tipo_examen";
	public static final String TABLA_EXAMEN_COLUMNA_FECHA_INICIO = "fecha_inicio";
	public static final String TABLA_EXAMEN_COLUMNA_DURACION_REAL = "duracion_real";
	public static final String TABLA_EXAMEN_COLUMNA_PORCENTAJE_COMPLETADO = "porcentaje_completado";
	
	/* TABLA TIPOEXAMEN */
	public static final String TABLA_TIPO_EXAMEN = "tipo_examen";
	public static final String TABLA_TIPO_EXAMEN_COLUMNA_ID = "id_tipo_examen";
	public static final String TABLA_TIPO_EXAMEN_COLUMNA_NOMBRE_EXAMEN = "nombre_examen";
	public static final String TABLA_TIPO_EXAMEN_COLUMNA_INSTRUCCIONES = "instrucciones";

	private static final String NOMBRE_BASEDATOS = "audinsaaudiologia.db";
	private static final int VERSION_BASEDATOS = 1;

	
	/*TABLA RESULTADO*/
	public static final String TABLA_RESULTADO = "resultado";
	public static final String TABLA_RESULTADO_COLUMNA_ID = "id_resultado";
	public static final String TABLA_RESULTADO_COLUMNA_ID_PERFIL = "id_perfil";
	public static final String TABLA_RESULTADO_COLUMNA_ID_EXAMEN = "id_examen";
	public static final String TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN = "valor_examen";
		
	// Database creation sql statement
	private static final String CREAR_TABLA_PERFIL = "create table "
			+ TABLA_PERFIL + "(" + TABLA_PERFIL_COLUMNA_ID
			+ " integer primary key autoincrement, " + TABLA_PERFIL_COLUMNA_NOMBRE
			+ " text not null, " + TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO
			+ " text not null, " + TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO
			+ " text not null);";

	
	private static final String CREAR_TABLA_RESULTADO ="create table"
			+ " " + TABLA_RESULTADO + "(" + TABLA_RESULTADO_COLUMNA_ID
			+ " integer primary key autoincrement, " 
			+ TABLA_RESULTADO_COLUMNA_ID_PERFIL + " integer not null,"
		    + TABLA_RESULTADO_COLUMNA_ID_EXAMEN +" integer not null,"
		    + TABLA_RESULTADO_COLUMNA_VALOR_EXAMEN + " integer not null,"
		    + "foreign key (" +TABLA_RESULTADO_COLUMNA_ID_PERFIL +") References "+TABLA_PERFIL +"("+TABLA_PERFIL_COLUMNA_ID +") ON DELETE CASCADE,"			
			+ "foreign key (" +TABLA_RESULTADO_COLUMNA_ID_EXAMEN +") References "+TABLA_EXAMEN 	+"("+TABLA_EXAMEN_COLUMNA_ID +") ON DELETE CASCADE"
			+ " );";

	private static final String CREAR_TABLA_EXAMEN ="create table "
			+ TABLA_EXAMEN + "(" + TABLA_EXAMEN_COLUMNA_ID
			+ " integer primary key autoincrement, "
			+ TABLA_EXAMEN_COLUMNA_ID_TIPO_EXAMEN + " integer not null,"
			+ TABLA_EXAMEN_COLUMNA_FECHA_INICIO + " text not null,"
		    + TABLA_EXAMEN_COLUMNA_DURACION_REAL +" integer not null,"
		    + TABLA_EXAMEN_COLUMNA_PORCENTAJE_COMPLETADO +" integer not null,"
		    + "foreign key (" +TABLA_EXAMEN_COLUMNA_ID_TIPO_EXAMEN +") References "+TABLA_TIPO_EXAMEN +"("+TABLA_TIPO_EXAMEN_COLUMNA_ID +")"
		    + " );";
	
	private static final String CREAR_TABLA_TIPO_EXAMEN = "create table "
			+ TABLA_TIPO_EXAMEN + "(" + TABLA_TIPO_EXAMEN_COLUMNA_ID
			+ " integer primary key autoincrement, " + TABLA_TIPO_EXAMEN_COLUMNA_NOMBRE_EXAMEN
			+ " text not null, " + TABLA_TIPO_EXAMEN_COLUMNA_INSTRUCCIONES
			+ " text not null);";
	
	private static final String CREAR_TIPO_EXAMEN_SENSIBILIDAD_OIDO = "insert into " + TABLA_TIPO_EXAMEN 
			+ " (" + TABLA_TIPO_EXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPO_EXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Sensibilidad de oído', '¿Cuál es el sonido más bajo/alto que puede oír?');";
	

	private static final String CREAR_TIPO_EXAMEN_HABLA_RUIDO = "insert into " + TABLA_TIPO_EXAMEN 
			+ " (" + TABLA_TIPO_EXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPO_EXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Habla en ruido', '¿Que tan bien oye en ruido?');";
	
	private static final String CREAR_TIPO_EXAMEN_CUESTIONARIO = "insert into " + TABLA_TIPO_EXAMEN 
			+ " (" + TABLA_TIPO_EXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPO_EXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Cuestionario', 'Preguntas acerca de su escucha diaria');";

	private static final String CREAR_TIPO_EXAMEN_ARTICULOS = "insert into " + TABLA_TIPO_EXAMEN 
			+ " (" + TABLA_TIPO_EXAMEN_COLUMNA_NOMBRE_EXAMEN + "," + TABLA_TIPO_EXAMEN_COLUMNA_INSTRUCCIONES + ")"
			+ " values ('Artículos', 'Conozca información importante acerca de su audición');";
	
	public MySQLiteHelper(Context context) {
		super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		/*create*/
		database.execSQL(CREAR_TABLA_PERFIL);
		database.execSQL(CREAR_TABLA_TIPO_EXAMEN);
		database.execSQL(CREAR_TABLA_EXAMEN);
		database.execSQL(CREAR_TABLA_RESULTADO);
			
		/*insert*/
		database.execSQL(CREAR_TIPO_EXAMEN_SENSIBILIDAD_OIDO);
		database.execSQL(CREAR_TIPO_EXAMEN_HABLA_RUIDO);
		database.execSQL(CREAR_TIPO_EXAMEN_CUESTIONARIO);
		database.execSQL(CREAR_TIPO_EXAMEN_ARTICULOS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Actualizando la base de datos de la version " + oldVersion + " a "
						+ newVersion + ", lo cual destruira la informacion contenida");
		db.execSQL("DROP TABLE IF EXISTS " + TABLA_PERFIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLA_TIPO_EXAMEN);
		onCreate(db);
	}
} 