package audinsa.audiologia;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import audinsa.audiologia.Adapters.ResultadosItemAdapter;
import audinsa.audiologia.businessdomain.Resultado;
import audinsa.audiologia.datasources.ResultadoDataSource;

public class ResultadoPerfilActivity extends Activity {
	private ResultadoDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_perfil);
		loadData();
		ListView listView = (ListView) findViewById(R.id.listResultados);
		registerForContextMenu(listView);
		setOnListViewItemClickListener();	
		
		}

	
	private void loadData() {
		dataSource = new ResultadoDataSource(this);
		
		ArrayList<Resultado> resultados = dataSource.obtenerTodosLosResultados();

		ResultadosItemAdapter adapter = new ResultadosItemAdapter(this,R.layout.listview_resultados_item_row,resultados);
		ListView listView = (ListView) findViewById(R.id.listResultados);
		listView.setAdapter(adapter);
	
		
		}
	private void setOnListViewItemClickListener() {
		ListView lstView = (ListView) findViewById(R.id.listResultados);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			
			
			}
		};
		lstView.setOnItemClickListener(listener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultado_perfil, menu);
		return true;
	}

}