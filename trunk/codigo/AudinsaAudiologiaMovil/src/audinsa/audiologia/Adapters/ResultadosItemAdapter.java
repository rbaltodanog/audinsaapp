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
import audinsa.audiologia.businessdomain.Resultado;

public class ResultadosItemAdapter extends ArrayAdapter<Resultado> {

	private Context context;
		int layoutResourceId;
		private ArrayList<Resultado> resultados;


		
		public ResultadosItemAdapter(Context context, int layoutResourceId, ArrayList<Resultado> resultados) {
			super(context, layoutResourceId, resultados);
			this.context = context;
			this.layoutResourceId = layoutResourceId;
			this.resultados = resultados;
		}

		public int getCount() {
			return resultados.size();
		}

		public Resultado getItem(int position) {
			return resultados.get(position);
		}
		
		public Resultado getItemById(long id) {
			for(int i = 0; i < getCount(); i++)
			{
				if (getItem(i).getId_resultado() == id)
				{
					return getItem(i);
				}
			}
			return null;
		}
		
		public long getItemId(int position) {
			return resultados.get(position).getId_resultado();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			ResultadoHolder holder = null;

			if(row == null)
			{
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new ResultadoHolder();
				//holder.txtValorResultado = (TextView)row.findViewById(R.id.txtValorResultado);
				holder.txtNombreExamen = (TextView)row.findViewById(R.id.txtNombreExamen);
		    	//holder.txtDescripcionExamen = (TextView)row.findViewById(R.id.txtDescripcionExamen);
				row.setTag(holder);
			}
			else
			{
				holder = (ResultadoHolder)row.getTag();
			}

			Resultado resultado = getItem(position);
			holder.txtNombreExamen.setText(resultado.getValor_examen());

			return row;
		}
		
		static class ResultadoHolder
		{
			//TextView txtValorResultado;
			TextView txtNombreExamen;			
			//ImageView txtDescripcionExamen;
			
			
		}
	}