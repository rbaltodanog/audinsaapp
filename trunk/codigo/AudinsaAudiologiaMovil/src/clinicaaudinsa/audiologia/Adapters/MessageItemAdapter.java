package clinicaaudinsa.audiologia.Adapters;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import clinicaaudinsa.audiologia.R;
import clinicaaudinsa.audiologia.businessdomain.Mensaje;

public class MessageItemAdapter extends ArrayAdapter<Mensaje> {
	private Context context;
	private ArrayList<Mensaje> mensajes;
	int layoutResourceId;
	
	public MessageItemAdapter(Context context, ArrayList<Mensaje> mensajes) {
		super(context, R.layout.listview_mensaje_item_row, mensajes);
		layoutResourceId = R.layout.listview_mensaje_item_row;
		this.context = context;
		this.mensajes = mensajes;		
	}

	public int getCount() {
		return mensajes.size();
	}

	public Mensaje getItem(int position) {
		return mensajes.get(position);
	}
	
	public Mensaje getItemById(long id) {
		for(int i = 0; i < getCount(); i++)
		{
			if (getItem(i).getId_mensaje() == id)
			{
				return getItem(i);
			}
		}
		return null;
	}
	
	public long getItemId(int position) {
		return getItem(position).getId_mensaje();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TestHolder holder = null;
		
		Mensaje mensaje = getItem(position);

		if(row == null)
		{
			//Setea dinamicamente el estilo dependiendo del examen
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new TestHolder();
			holder.txtFechaMensaje = (TextView)row.findViewById(R.id.txtFechaMensaje);
			holder.txtTextoMensaje = (TextView)row.findViewById(R.id.txtTextoMensaje);
			row.setTag(holder);
		}
		else
		{
			holder = (TestHolder)row.getTag();
		}
		
		holder.txtFechaMensaje.setText(mensaje.getFecha_mensajeAsString());
		holder.txtTextoMensaje.setText(mensaje.getTexto());

		return row;
	}
	
	static class TestHolder
	{
		TextView txtFechaMensaje;
		TextView txtTextoMensaje;
	}
}
