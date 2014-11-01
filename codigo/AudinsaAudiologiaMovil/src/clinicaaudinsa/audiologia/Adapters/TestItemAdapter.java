package clinicaaudinsa.audiologia.Adapters;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import clinicaaudinsa.audiologia.businessdomain.TipoExamen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import clinicaaudinsa.audiologia.R;

public class TestItemAdapter extends ArrayAdapter<TipoExamen> {

	private Context context;
	Dictionary<String, Integer> testLayouts;
	private ArrayList<TipoExamen> tipoExamenes;

	public TestItemAdapter(Context context, ArrayList<TipoExamen> tipoExamenes) {
		super(context, R.layout.listview_sensibilidadoido_item_row, tipoExamenes);
		this.context = context;
		this.tipoExamenes = tipoExamenes;
		testLayouts = new Hashtable<String, Integer>();
		testLayouts.put("sensibilidad de oído", R.layout.listview_sensibilidadoido_item_row);
	/* Se comenta mientras el usuario define el examen
		testLayouts.put("habla en ruido", R.layout.listview_hablaruido_item_row);
	*/	testLayouts.put("cuestionario", R.layout.listview_cuestionario_item_row);
		testLayouts.put("artículos", R.layout.listview_articulos_item_row);
		testLayouts.put("mensajería", R.layout.listview_mensajeria_item_row);
		
	}

	public int getCount() {
		return tipoExamenes.size();
	}

	public TipoExamen getItem(int position) {
		return tipoExamenes.get(position);
	}
	
	public TipoExamen getItemById(long id) {
		for(int i = 0; i < getCount(); i++)
		{
			if (getItem(i).getIdTipoExamen() == id)
			{
				return getItem(i);
			}
		}
		return null;
	}
	
	public long getItemId(int position) {
		return getItem(position).getIdTipoExamen();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TestHolder holder = null;
		
		TipoExamen tipoExamen = getItem(position);

		if(row == null)
		{
			//Setea dinamicamente el estilo dependiendo del examen
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(testLayouts.get(tipoExamen.getNombreExamen().toLowerCase()), parent, false);
			
			holder = new TestHolder();
			holder.txtNombreExamen = (TextView)row.findViewById(R.id.txtNombreExamen);
			holder.txtDescripcionExamen = (TextView)row.findViewById(R.id.txtDescripcionExamen);
			row.setTag(holder);
		}
		else
		{
			holder = (TestHolder)row.getTag();
		}
		
		holder.txtNombreExamen.setText(tipoExamen.getNombreExamen());
		holder.txtDescripcionExamen.setText(tipoExamen.getInstrucciones());

		return row;
	}
	
	static class TestHolder
	{
		ImageView imgTipoExamen;
		TextView txtNombreExamen;
		TextView txtDescripcionExamen;
	}
}
