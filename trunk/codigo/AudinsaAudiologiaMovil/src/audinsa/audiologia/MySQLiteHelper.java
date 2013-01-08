package audinsa.audiologia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	  public static final String TABLA_PERFIL = "perfil";
	  public static final String TABLA_PERFIL_COLUMNA_ID = "id_perfil";
	  public static final String TABLA_PERFIL_COLUMNA_NOMBRE = "nombre";
	  public static final String TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO = "fecha_nacimiento";
	  public static final String TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO = "correo_electronico";

	  private static final String NOMBRE_BASEDATOS = "audinsaaudiologia.db";
	  private static final int VERSION_BASEDATOS = 1;

	  // Database creation sql statement
	  private static final String CREAR_BD = "create table "
	      + TABLA_PERFIL + "(" + TABLA_PERFIL_COLUMNA_ID
	      + " integer primary key autoincrement, " + TABLA_PERFIL_COLUMNA_NOMBRE
	      + " text not null, " + TABLA_PERFIL_COLUMNA_FECHA_NACIMIENTO
	      + " text not null, " + TABLA_PERFIL_COLUMNA_CORREO_ELECTRONICO
	      + " text not null);";

	  public MySQLiteHelper(Context context) {
	    super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(CREAR_BD);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(),
	        "Actualizando la base de datos de la version " + oldVersion + " a "
	            + newVersion + ", lo cual destruira la informacion contenida");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLA_PERFIL);
	    onCreate(db);
	  }

	} 