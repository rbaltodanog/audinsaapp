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
import audinsa.audiologia.businessdomain.TipoExamen;

public class TestItemAdapter extends ArrayAdapter<TipoExamen> {

	private Context context;
	int layoutResourceId;
	private ArrayList<TipoExamen> tipoExamenes;

	public TestItemAdapter(Context context, int layoutResourceId, ArrayList<TipoExamen> tipoExamenes) {
		super(context, layoutResourceId, tipoExamenes);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.tipoExamenes = tipoExamenes;
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

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new TestHolder();
			holder.txtNombreExamen = (TextView)row.findViewById(R.id.txtNombreExamen);
			holder.txtDescripcionExamen = (TextView)row.findViewById(R.id.txtDescripcionExamen);
			row.setTag(holder);
		}
		else
		{
			holder = (TestHolder)row.getTag();
		}

		TipoExamen tipoExamen = getItem(position);
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
