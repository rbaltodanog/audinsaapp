package audinsa.audiologia;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import audinsa.audiologia.datasources.TipoExamenDataSource;

public class ExamenesActivity extends Activity {
	private TipoExamenDataSource dataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_examenes);

		dataSource = new TipoExamenDataSource(this);
		dataSource.open();
		
		SimpleAdapter adapter = new SimpleAdapter(
				this, dataSource.obtenerTodosLosTiposExamenesTwoLineItems(),
				android.R.layout.simple_list_item_2,
				new String[] {"First Line", "Second Line"},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		ListView lstView = (ListView)findViewById(R.id.listExamenes);
		lstView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_examenes, menu);
		return true;
	}

}
