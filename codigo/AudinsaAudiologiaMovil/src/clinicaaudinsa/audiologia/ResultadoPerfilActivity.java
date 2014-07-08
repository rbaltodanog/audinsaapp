package clinicaaudinsa.audiologia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import clinicaaudinsa.audiologia.Adapters.ResultadosItemAdapter;
import clinicaaudinsa.audiologia.businessdomain.Examen;
import clinicaaudinsa.audiologia.businessdomain.Perfil;
import clinicaaudinsa.audiologia.businessdomain.Resultado;
import clinicaaudinsa.audiologia.datasources.ExamenDataSource;
import clinicaaudinsa.audiologia.datasources.PerfilDataSource;
import clinicaaudinsa.audiologia.datasources.ResultadoDataSource;

import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
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
import android.graphics.drawable.Drawable;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import clinicaaudinsa.audiologia.R;

public class ResultadoPerfilActivity extends Activity {
	private ResultadoDataSource resultadoDataSource;
	private ExamenDataSource examenDataSource;
	private ArrayList<Resultado> resultados;
	private UiLifecycleHelper uiHelper;
	SharedPreferences sPrefs;
	private int tipoExamen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sPrefs = PreferenceManager.getDefaultSharedPreferences(ResultadoPerfilActivity.this);
		setContentView(R.layout.activity_resultado_perfil);
		loadData();
		ListView listView = (ListView) findViewById(R.id.listResultados);
		listView.setEmptyView(findViewById(R.id.lblResultadosVacio));
		registerForContextMenu(listView);
		addOnClickListener();
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}


	private void loadData() {
		resultadoDataSource = new ResultadoDataSource(this);
		examenDataSource = new ExamenDataSource(this);
		long idPerfil=getIntent().getLongExtra("idPerfil",0);
		resultados = resultadoDataSource.obtenerTodosLosResultados(idPerfil);

		ResultadosItemAdapter adapter = new ResultadosItemAdapter(this,
				R.layout.listview_resultados_item_row,resultados);
		ListView listView = (ListView) findViewById(R.id.listResultados);
		listView.setAdapter(adapter);


	}
	private void addOnClickListener() {
		ListView lstView = (ListView) findViewById(R.id.listResultados);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				long idPerfil,idResultado,idExamen;
				idPerfil = ((Resultado) parent.getItemAtPosition(position))
						.getId_perfil();
				idResultado = ((Resultado) parent.getItemAtPosition(position))
						.getId_resultado();
				idExamen = ((Resultado) parent.getItemAtPosition(position))
						.getId_examen();
				tipoExamen = ((Examen)examenDataSource.
						buscarExamen(Integer.parseInt(Long.toString(idExamen)))).getId_tipo_examen();
				Intent intent = new Intent(view.getContext(),
						ExamenesActivity.class);
				intent.putExtra("idPerfil", idPerfil);
				intent.putExtra("idResultado", idResultado);
				intent.putExtra("idExamen", idExamen);

				//startActivity(intent);
				view.setLongClickable(false);  
				openContextMenu(view);

			}
		};
		lstView.setOnItemClickListener(listener);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_resultado_perfil, menu);
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

	@Override  
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo);  
		getMenuInflater().inflate(R.menu.activity_contextual_resultado, menu);
		menu.setHeaderTitle(R.string.menu_titulo);  
	}  

	@Override  
	public boolean onContextItemSelected(MenuItem item) {  
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Resultado resultadoSeleccionado = resultados.get(info.position);
		Resources res = this.getResources();

		final long idPerfil = resultadoSeleccionado.getId_perfil();
		final long idResultado = resultadoSeleccionado.getId_resultado();
		final long idExamen = resultadoSeleccionado.getId_examen();

		switch (item.getItemId()) {
		case R.id.menu_borrar:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(res.getString(R.string.txtResultadoDialogoTituloDeseaEliminar))
			.setMessage(res.getString(R.string.txtResultadosDialogoDeseaEliminar))
			.setCancelable(false)
			.setPositiveButton(
					res.getString(R.string.txtPerfilesDialogoOk),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							try {
								boolean resultado= false;
								resultado = resultadoDataSource.borrarResultado(idResultado, idPerfil);
								if (resultado)
								{
									examenDataSource.borrarExamen(idExamen); 
								}
								mostrarMensaje(resultado);
								loadData();
							} catch (Exception ex) {
								Toast.makeText(
										getBaseContext(),
										getBaseContext()
										.getResources()
										.getString(
												R.string.txtResultadoErrorAlBorrar)
												+ ": " + ex.getMessage(),
												Toast.LENGTH_SHORT).show();
							}
						}
					})
					.setNegativeButton(
							res.getString(R.string.txtPerfilesDialogoCancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		case R.id.menu_compartir:
			compartirInformacion(idResultado, idPerfil);
			return true;
		case R.id.menu_contactar:
			contactarClinica(idResultado, idPerfil);
			return true;

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private void compartirInformacion(long idResultado, long idPerfil) {

		Resultado r = onGetResultado(idResultado,idPerfil);
		String estado= r.getValorResultado_examen();
		compartirInformacion(estado,this.getBaseContext());
	}

	private void contactarClinica(long idResultado, long idPerfil) {
		Perfil p = onGetPerfil(idPerfil);
		Resultado r = onGetResultado(idResultado,idPerfil);
		int val_examen= r.getValor_examen();
		String estado= r.getValorResultado_examen();
		contactarClinica(estado, p,val_examen);
	}

	private Perfil onGetPerfil(long idPerfil) {
		PerfilDataSource dataSource = new PerfilDataSource(this);
		Perfil p = new Perfil();
		try {
			p = dataSource.buscarPerfil(idPerfil);
		} catch (Exception ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de obtener el perfil.");
		}
		return p;
	}

	private Resultado onGetResultado(long idResultado, long idPerfil) {
		Resultado r = new Resultado();
		try {
			r = resultadoDataSource.buscarResultado(idResultado,idPerfil);
		} catch (Exception ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de obtener el perfil.");
		}
		return r;
	}

	public void mostrarMensaje(boolean resultado){
		AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
		TextView myMsg = new TextView(this);

		if (resultado == false){
			Toast.makeText(
					getBaseContext(),
					"No fue posible borrar el resultado",
					Toast.LENGTH_SHORT).show();
		}
		else{	
			Toast.makeText(
					getBaseContext(),
					"El resultado ha sido borrado",
					Toast.LENGTH_SHORT).show();
		}
		myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
		popupBuilder.setView(myMsg);
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
					String shareLink = "http://www.clinicaaudinsa.com";
					String linkDescription = "Página de la Clínica Auditiva Audinsa";
					String packageClassName = items[which].packageClassName;
					
					// Si es Facebook, compartir usando el SDK de Facebook
					if (packageClassName.contains("com.facebook.katana"))
					{
						/*OpenGraphObject meal = OpenGraphObject.Factory.createForPost("audinsa:take");
						meal.setProperty("title", "Buffalo Tacos");
						meal.setProperty("image", "http://example.com/cooking-app/images/buffalo-tacos.png");
						meal.setProperty("url", "https://example.com/cooking-app/meal/Buffalo-Tacos.html");
						meal.setProperty("description", "Leaner than beef and great flavor.");

						OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
						action.setProperty("hearing_test", meal);

						FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(ResultadoPerfilActivity.this, action, "audinsa:take", "hearing_test")
						        .build();
						uiHelper.trackPendingDialogCall(shareDialog.present());*/
						
						// ESTE ES EL CODIGO MAS PROXIMO PARA USAR EL SHARE DE FACEBOOK
						/*OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
						action.setProperty("hearing_test", "https://www.clinicaaudinsa.com/");
						action.setType("hearing_test.take");

						@SuppressWarnings("deprecation")
						FacebookDialog shareDialog = new FacebookDialog.
								OpenGraphActionDialogBuilder(ResultadoPerfilActivity.this, action, "hearing_test")
						        .build();
						uiHelper.trackPendingDialogCall(shareDialog.present());*/
						// Start the selected activity sending it the URLs of the resized images
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						sharingIntent.putExtra(Intent.EXTRA_SUBJECT, linkDescription);
						sharingIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
						sharingIntent.setClassName(items[which].context, items[which].packageClassName);
						startActivity(sharingIntent);
						finish();
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
	
  public void contactarClinica(String estado, Perfil p, int val_examen) {
		Intent contactIntent = new Intent(Intent.ACTION_SEND);
		contactIntent.setType("message/rfc822"); //set the email recipient
		SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.US);
		String fecha = sdf.format(p.getFechaNacimiento()) + " (día/mes/año)";
		String shareBody;
		String desExam="";
		//Cambia el titulo del activity en base en el tipo de examen
		
		if (tipoExamen == 1)
		{
			desExam=getString(R.string.sensibilidad_oido_nombre);					
		}
		if (tipoExamen == 2)
		{
			desExam=getString(R.string.habla_ruido_nombre);
		}
		if (tipoExamen == 3)
		{
			desExam=getString(R.string.cuestionario_nombre);
		}
		
		shareBody = "He realizado el exámen:"+desExam +
				".Mi RESULTADO ES:" + estado  +
				". Nombre: " + p.toString() + 
				", Fecha de nacimiento: " + fecha + 
				", Correo Electrónico: " +	p.getCorreoElectronico();
		
		contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
		contactIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
		String[] mail = { "info@clinicaaudinsa.com", "" };
		contactIntent.putExtra(Intent.EXTRA_EMAIL, mail);   
		startActivity(Intent.createChooser(contactIntent, "Enviar información usando"));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}
		});
	}
}
