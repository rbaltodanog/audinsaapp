package audinsa.audiologia.businessdomain;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class CompartirResultado  extends Activity {

	public void contactarClinica(String estado, Perfil p) {
		Intent contactIntent = new Intent(Intent.ACTION_SEND);
		contactIntent.setType("message/rfc822"); //set the email recipient
		String shareBody = "He realizado el exámen: Cuestionario. Mi calificación es: "+ estado + ".Estoy interesado en obtener una cita médica. Mis datos son: " +
		"Nombre: " + p.toString() + ", Fecha de nacimiento: " + p.getFechaNacimiento() + ", Correo Electrónico: " +	p.getCorreoElectronico();
		contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
		contactIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
		String[] mail = { "info@clinicaaudinsa.com", "" };
		contactIntent.putExtra(Intent.EXTRA_EMAIL, mail);   
        startActivity(Intent.createChooser(contactIntent, "Enviar información usando"));
  }

	public void compartirInformacion(String estado, Context baseContext) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Estoy usando la aplicación de Audinsa S.A. para revisar mi audición. Mi calificación es: "+ estado;
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Compartir");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
		
		
		PackageManager pm = baseContext.getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
		for (final ResolveInfo app : activityList) {
			//twitter
		    if ("com.twitter.android.PostActivity".equals(app.activityInfo.name)) {
		        final ActivityInfo activity = app.activityInfo;
		        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
		        sharingIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |             Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		        sharingIntent.setComponent(name);
		        baseContext.startActivity(sharingIntent);
		        break;
		   }
		    else if ((app.activityInfo.name).contains("facebook")) {
		    	//twitter
		    	final ActivityInfo activity = app.activityInfo;
		        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
		        sharingIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |             Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		        sharingIntent.setComponent(name);
		        baseContext.startActivity(sharingIntent);
		        break;
		   }
		    else {
		       	startActivity(Intent.createChooser(sharingIntent, "Compartiendo información usando"));
		       	break;	
		    	
		    }
	}
	}
	

	
}
