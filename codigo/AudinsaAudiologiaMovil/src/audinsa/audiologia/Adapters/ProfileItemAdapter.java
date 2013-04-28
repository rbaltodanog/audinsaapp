package audinsa.audiologia.Adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import audinsa.audiologia.R;
import audinsa.audiologia.businessdomain.Perfil;

public class ProfileItemAdapter extends ArrayAdapter<Perfil> {

	private Context context;
	int layoutResourceId;
	private ArrayList<Perfil> perfiles;

	public ProfileItemAdapter(Context context, int layoutResourceId, ArrayList<Perfil> perfiles) {
		super(context, layoutResourceId, perfiles);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.perfiles = perfiles;
	}

	public int getCount() {
		return perfiles.size();
	}

	public Perfil getItem(int position) {
		return perfiles.get(position);
	}
	
	public Perfil getItemById(long id) {
		for(int i = 0; i < perfiles.size(); i++)
		{
			if (perfiles.get(i).getIdPerfil() == id)
			{
				return perfiles.get(i);
			}
		}
		return null;
	}
	
	public long getItemId(int position) {
		return perfiles.get(position).getIdPerfil();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ProfileHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new ProfileHolder();
			holder.txtNombrePerfil = (TextView)row.findViewById(R.id.txtNombrePerfil);
			holder.imgBorrarPerfil = (ImageView)row.findViewById(R.id.imgBorrarPerfil);
			row.setTag(holder);
		}
		else
		{
			holder = (ProfileHolder)row.getTag();
		}

		Perfil perfil = getItem(position);
		holder.txtNombrePerfil.setText(perfil.getNombre());

		return row;
	}
	
	static class ProfileHolder
	{
		TextView txtNombrePerfil;
		ImageView imgBorrarPerfil;
	}
}