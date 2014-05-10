package audinsa.audiologia;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.datasources.PerfilDataSource;

public class ResultadoActivity extends Activity {

	private PerfilDataSource dataSource;
	private UiLifecycleHelper uiHelper;
	private long tipoExamen;
	
	SharedPreferences sPrefs;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// pantalla vertical
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado);

		TextView txtResultadoDescription = (TextView) findViewById(R.id.txtResultadoDescription);
		String strResultado = getIntent().getStringExtra("strResultado");
		tipoExamen = getIntent().getLongExtra("idTipoExamen", 0);
		long duracionExamen = getIntent().getLongExtra("duracionExamen", 0);
		txtResultadoDescription.setText(strResultado);
		// Cambia la imagen del semáforo si esta aprobado o reprobado
		boolean aprobado = getIntent().getBooleanExtra("bolAprobado", true);
		ImageView img = (ImageView) findViewById(R.id.imgViewCuestionarioAprobado);
		if (aprobado) {
			img.setImageResource(R.drawable.animation_resultado_aprobado);
		} else {
			img.setImageResource(R.drawable.animation_resultado_reprobado);
		}

		//Cambia el titulo del activity en base en el tipo de examen
		if (tipoExamen == 1)
		{
			setTitle(R.string.title_activity_sensibilidad_oido_resultado);
		}
		if (tipoExamen == 2)
		{
			setTitle(R.string.title_activity_habla_ruido_resultado);
		}
		if (tipoExamen == 3)
		{
			setTitle(R.string.title_activity_cuestionario_resultado);
		}

		TableRow rowContactarClinica = (TableRow)findViewById(R.id.row_contactar_clinica);			
		rowContactarClinica.setClickable(true);
		rowContactarClinica.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean  bolEstado=getIntent().getBooleanExtra("bolAprobado", true);
				String estadoContactar=(bolEstado) ? getString(R.string.strContactarResultadoPositivo) : getString(R.string.strContactarResultadoNegativo) ;
				String estado= estadoContactar.toString();
				long idPerfil = getIntent().getLongExtra("idPerfil", 0);
				Perfil p = onGetPerfil(idPerfil);
				contactarClinica(estado,p,bolEstado);
			}
		});		

		TableRow rowCompartirResultado = (TableRow)findViewById(R.id.row_compartir_resultado);			
		rowCompartirResultado.setClickable(true);
		rowCompartirResultado.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//String estado=(getIntent().getBooleanExtra("bolAprobado", true)) ? "Pasa la prueba" : "Contacte un especialista";
				String estado=(getIntent().getBooleanExtra("bolAprobado", true)) ? getString(R.string.strCompartirResultadoPositivo) : getString(R.string.strCompartirResultadoNegativo);
				compartirInformacion(estado, v.getContext());
			}
		});
		
		TextView lblDuracionExamen = (TextView)findViewById(R.id.lblDuracionExamen);
		if (lblDuracionExamen != null)
		{
			lblDuracionExamen.setText("Duración del examen: " + String.valueOf(duracionExamen) + " segundos");
		}
		
		sPrefs = PreferenceManager.getDefaultSharedPreferences(ResultadoActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present(pantalla horizontal).

		getMenuInflater().inflate(R.menu.activity_cuestionario_resultado, menu);
		@SuppressWarnings("unused")
		TextView txtResultadoDescription = (TextView) findViewById(R.id.txtResultadoDescription);

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_regresar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);

		ImageView img = (ImageView) findViewById(R.id.imgViewCuestionarioAprobado);
		AnimationDrawable frameAnimation = (AnimationDrawable) img
				.getDrawable();
		frameAnimation.setCallback(img);
		frameAnimation.setVisible(true, true);
		frameAnimation.start();
	}

	public void onRegresarClick(View view) {
		this.finish();
	}

	// Obtiene el perfil por compartir
	public Perfil onGetPerfil(long idPerfil) {
		dataSource = new PerfilDataSource(this);
		Perfil p = new Perfil();
		try {
			p = dataSource.buscarPerfil(idPerfil);
		} catch (Exception ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de obtener el perfil.");
		}
		return p;
	}

	public void compartirInformacion(String estado, Context baseContext) {
		startDefaultAppOrPromptUserForSelection(estado);
	}

	public void startDefaultAppOrPromptUserForSelection(final String estado) {
		String action = Intent.ACTION_SEND;

		// Get list of handler apps that can send
		Intent intent = new Intent(action);
		intent.setType("image/jpeg");
		PackageManager pm = getPackageManager();
		List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);

		boolean useDefaultSendApplication = sPrefs.getBoolean("useDefaultSendApplication", false);
		if (!useDefaultSendApplication) {
			// Referenced http://stackoverflow.com/questions/3920640/how-to-add-icon-in-alert-dialog-before-each-item

			// Class for a singular activity item on the list of apps to send to
			class ListItem {
				public final String name;
				public final Drawable icon;
				public final String context;
				public final String packageClassName;
				public ListItem(String text, Drawable icon, String context, String packageClassName) {
					this.name = text;
					this.icon = icon;
					this.context = context;
					this.packageClassName = packageClassName;
				}
				@Override
				public String toString() {
					return name;
				}
			}

			// Form those activities into an array for the list adapter
			final ListItem[] items = new ListItem[resInfos.size()];
			int i = 0;
			for (ResolveInfo resInfo : resInfos) {
				String context = resInfo.activityInfo.packageName;
				String packageClassName = resInfo.activityInfo.name;
				CharSequence label = resInfo.loadLabel(pm);
				Drawable icon = resInfo.loadIcon(pm);
				items[i] = new ListItem(label.toString(), icon, context, packageClassName);
				i++;
			}
			ListAdapter adapter = new ArrayAdapter<ListItem>(
					this,
					android.R.layout.select_dialog_item,
					android.R.id.text1,
					items){

				public View getView(int position, View convertView, ViewGroup parent) {
					// User super class to create the View
					View v = super.getView(position, convertView, parent);
					TextView tv = (TextView)v.findViewById(android.R.id.text1);

					// Put the icon drawable on the TextView (support various screen densities)
					int dpS = (int) (32 * getResources().getDisplayMetrics().density + 0.5f);
					items[position].icon.setBounds(0, 0, dpS, dpS);
					tv.setCompoundDrawables(items[position].icon, null, null, null);

					// Add margin between image and name (support various screen densities)
					int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
					tv.setCompoundDrawablePadding(dp5);

					return v;
				}
			};

			// Build the list of send applications
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Escoja la aplicación:");
			builder.setIcon(R.drawable.ic_compartir);
			
			// Si se descomenta esto, sale un checkbox en la pantalla de
			// escoger aplicacion a compartir y permite establecer
			// una aplicación por defecto para compartir
			/*CheckBox checkbox = new CheckBox(getApplicationContext());
		  	checkbox.setText(getString(R.string.enable_default_send_application));
		  	checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

		   	// Save user preference of whether to use default send application
		   	@Override
		   	public void onCheckedChanged(CompoundButton paramCompoundButton,
		     	boolean paramBoolean) {
		    		SharedPreferences.Editor editor = sPrefs.edit();
		    		editor.putBoolean("useDefaultSendApplication", paramBoolean);
		    		editor.commit();
		   		}
		  	});
		  	builder.setView(checkbox);*/
			builder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface paramDialogInterface) {
					// do something
				}
			});

			// Set the adapter of items in the list
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences.Editor editor = sPrefs .edit();
					editor.putString("defaultSendApplicationName", items[which].name);
					editor.putString("defaultSendApplicationPackageContext", items[which].context);
					editor.putString("defaultSendApplicationPackageClassName", items[which].packageClassName);
					editor.commit();

					dialog.dismiss();
					String shareBody = "Estoy usando la aplicación de Audinsa S.A. para revisar mi audición. Mi resultado es: "+ estado;
					String linkDescription = "Página de la Clínica Auditiva Audinsa";
					String packageClassName = items[which].packageClassName;
					
					// Si es Facebook, compartir usando el SDK de Facebook
					if (packageClassName.contains("com.facebook.katana"))
					{
						FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(ResultadoActivity.this)
						.setApplicationName("Aplicación móvil de Audiología - Audinsa")
				        .setLink("http://www.clinicaaudinsa.com/espanol/index.htm")
				        .setDescription(linkDescription)
				        .setPicture("http://i232.photobucket.com/albums/ee68/rbaltodanog/ic_launcher_zps6d218b2a.png")
				        .build();
						
						uiHelper.trackPendingDialogCall(shareDialog.present());
					}
					else // De lo contrario, mande la información normal a otro tipo de aplicación
					{
						// Start the selected activity sending it the URLs of the resized images
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Compartir");
						sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
						sharingIntent.setClassName(items[which].context, items[which].packageClassName);
						startActivity(sharingIntent);
						finish();
					}
				}
			});

			AlertDialog dialog = builder.create();
			dialog.show();


		} else { // Start the default send application

			// Get default app name saved in preferences
			String defaultSendApplicationName = sPrefs.getString("defaultSendApplicationName", "<null>");
			String defaultSendApplicationPackageContext = sPrefs.getString("defaultSendApplicationPackageContext", "<null>");
			String defaultSendApplicationPackageClassName = sPrefs.getString("defaultSendApplicationPackageClassName", "<null>");
			if (defaultSendApplicationPackageContext == "<null>" || defaultSendApplicationPackageClassName == "<null>") {
				Toast.makeText(getApplicationContext(), "Can't find app: " + defaultSendApplicationName + 
						" (" + defaultSendApplicationPackageClassName + ")", Toast.LENGTH_LONG).show();

				// don't have default application details in prefs file so set use default app to null and rerun this method
				SharedPreferences.Editor editor = sPrefs.edit();
				editor.putBoolean("useDefaultSendApplication", false);
				editor.commit();
				startDefaultAppOrPromptUserForSelection(estado);
				return;
			}

			// Check app is still installed
			try {
				ApplicationInfo info = getPackageManager().getApplicationInfo(defaultSendApplicationPackageContext, 0);
			} catch (PackageManager.NameNotFoundException e){
				Toast.makeText(getApplicationContext(),  "Can't find app: " + defaultSendApplicationName +
						" (" + defaultSendApplicationPackageClassName + ")", Toast.LENGTH_LONG).show();

				// don't have default application installed so set use default app to null and rerun this method
				SharedPreferences.Editor editor = sPrefs.edit();
				editor.putBoolean("useDefaultSendApplication", false);
				editor.commit();
				startDefaultAppOrPromptUserForSelection(estado);
				return;
			}

			// Start the selected activity
			intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/jpeg");
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setClassName(defaultSendApplicationPackageContext, defaultSendApplicationPackageClassName);
			startActivity(intent);
			finish();
			return;
		}
	}
	
	  public void contactarClinica( String estado, Perfil p, boolean val_examen) {
			Intent contactIntent = new Intent(Intent.ACTION_SEND);
			contactIntent.setType("message/rfc822"); //set the email recipient
			SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.US);
			String fecha = sdf.format(p.getFechaNacimiento()) + " (día/mes/año)";
			String shareBody;
			String desExam="";
			//Cambia el titulo del activity en base en el tipo de examen
			if (tipoExamen == 1)
			{
				desExam=getString(R.string.title_activity_sensibilidad_oido_resultado);					
			}
			if (tipoExamen == 2)
			{
				desExam=getString(R.string.title_activity_habla_ruido_resultado);
			}
			if (tipoExamen == 3)
			{
				desExam=getString(R.string.title_activity_cuestionario_resultado);
			}
	
			//if(val_examen){
	
			shareBody = "He realizado el exámen:"+desExam +".Mi RESULTADO ES:" + estado  +"Nombre: " + p.toString() + ", Fecha de nacimiento: " + fecha + ", Correo Electrónico: " +	p.getCorreoElectronico();
						 
				//}
			//	else{
			//	 shareBody = "He realizado el exámen: "+desExam.toString() +"Mi RESULTADO ES:"+ estado+ "Nombre: " + p.toString() + ", Fecha de nacimiento: " + fecha + ", Correo Electrónico: " +	p.getCorreoElectronico();
			//}	
			contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
			contactIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
			String[] mail = { "info@clinicaaudinsa.com", "" };
			contactIntent.putExtra(Intent.EXTRA_EMAIL, mail);   
			startActivity(Intent.createChooser(contactIntent, "Enviar información usando"));
		}
}


