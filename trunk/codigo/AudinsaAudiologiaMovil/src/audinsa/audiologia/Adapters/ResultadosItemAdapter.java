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
import audinsa.audiologia.businessdomain.TipoExamen;
import audinsa.audiologia.datasources.ExamenDataSource;
import audinsa.audiologia.datasources.TipoExamenDataSource;

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
			int idtipoExamen=0;
			int idExamen=0;
			String nomExamen;
			ExamenDataSource datasourceExamen = new ExamenDataSource(context);
			TipoExamenDataSource datasourceTipoExamen = new TipoExamenDataSource(context);
			
			
			if(row == null)
			{
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new ResultadoHolder();
				holder.txtValorExamen = (TextView)row.findViewById(R.id.txtValorExamen);
				holder.txtNombreExamen = (TextView)row.findViewById(R.id.txtNombreExamen);
			    holder.imgTipoExamen = (ImageView)row.findViewById(R.id.imgTipoExamen);
				row.setTag(holder);			
			
			}
			else
			{
				holder = (ResultadoHolder)row.getTag();
			}

			Resultado resultado = getItem(position);
			try{
				
				holder.txtValorExamen.setText(Integer.toString(resultado.getValor_examen()));
			   //Permite obtener los valores para ir a obtener el nombre del
				//Tipo de exam.
				idExamen=resultado.getId_examen();
			    idtipoExamen= (datasourceExamen.buscarExamen(idExamen)).getId_tipo_examen();//obtiene el idtipoexamen del exam del resultado
			    nomExamen= (datasourceTipoExamen.buscarTipoExamen(idtipoExamen)).getNombreExamen();
			    holder.txtNombreExamen.setText(nomExamen);
			     
			    switch (idtipoExamen){
			    case 1:
			    	 holder.imgTipoExamen.setImageResource(R.drawable.ic_hearing_sensibility);
					   break;
			    case 2:
			    	 holder.imgTipoExamen.setImageResource(R.drawable.ic_hablaenruido);
					   break;
			    case 3:
			    	 holder.imgTipoExamen.setImageResource(R.drawable.ic_test);
					   break;
			    default:
			    	break;
			    
			    }
			    
			  
			}
			catch(Exception e ){
				e.getMessage();
				
			}		
			

			return row;
		}
		
		static class ResultadoHolder
		{
			TextView txtValorExamen;
			TextView txtNombreExamen;			
			ImageView imgTipoExamen;
			
			
		}
	}