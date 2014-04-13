package audinsa.audiologia;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import audinsa.audiologia.Adapters.ProfileItemAdapter;
import audinsa.audiologia.businessdomain.Perfil;
import audinsa.audiologia.datasources.PerfilDataSource;

public class PerfilesActivity extends Activity {
	private PerfilDataSource dataSource;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfiles);
		loadData();
		ListView listView = (ListView) findViewById(R.id.listPerfiles);
		registerForContextMenu(listView);
		setOnListViewItemClickListener();
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onAgregarPerfilClick(View view) {
		Intent intentCrearPerfil = new Intent(view.getContext(),
				PerfilesMantenimientoActivity.class);
		startActivity(intentCrearPerfil);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_perfiles, menu);
		return true;
	}

	private void loadData() {
		dataSource = new PerfilDataSource(this);
		ArrayList<Perfil> perfiles = dataSource.obtenerTodosLosPerfiles();
		context = this;
		ProfileItemAdapter adapter = new ProfileItemAdapter(this,
				R.layout.listview_perfiles_item_row, perfiles);
		ListView listView = (ListView) findViewById(R.id.listPerfiles);
		listView.setAdapter(adapter);
	}

	private void setOnListViewItemClickListener() {
		ListView lstView = (ListView) findViewById(R.id.listPerfiles);
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				long idPerfil = 0;
				idPerfil = ((Perfil) parent.getItemAtPosition(position))
						.getIdPerfil();
				Intent intent = new Intent(view.getContext(),
						ExamenesActivity.class);
				intent.putExtra("idPerfil", idPerfil);
				startActivity(intent);
			}
		};
		lstView.setOnItemClickListener(listener);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Resources res = this.getResources();
		menu.setHeaderTitle(res
				.getString(R.string.txtPerfilesMenuContextualTitulo));
		menu.add(0, 0, 0,
				res.getString(R.string.txtPerfilesMenuContextualEliminar));
		menu.add(1, 1, 1,
				res.getString(R.string.txtPerfilesMenuContextualModificar));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Resources res = this.getResources();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		long id = info.id;
		if (item.getTitle() == res
				.getString(R.string.txtPerfilesMenuContextualEliminar)) {
			eliminarPerfil(id);
		} else if (item.getTitle() == res
				.getString(R.string.txtPerfilesMenuContextualModificar)) {
			Intent intent = new Intent();
			intent.setClass(this.getBaseContext(), PerfilesMantenimientoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("idPerfil", id);
			intent.putExtra("actualizacion", true);
			startActivity(intent);
			return true;
		}
		else
		{
			return false;
		}
		return true;
	}

	public void eliminarPerfil(final long id) {
		Resources res = this.getResources();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(res.getString(R.string.txtPerfilesDialogoTitulo))
		.setMessage(
				res.getString(R.string.txtPerfilesDialogoDeseaEliminar))
				.setCancelable(false)
				.setNegativeButton(
						res.getString(R.string.txtPerfilesDialogoCancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
						.setPositiveButton(
								res.getString(R.string.txtPerfilesDialogoOk),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											dataSource.borrarPerfil(id, context);
											loadData();
											Toast.makeText(
													getBaseContext(),
													getBaseContext()
													.getResources()
													.getString(
															R.string.txtPerfilesToastPerfilEliminado),
															Toast.LENGTH_SHORT).show();
										} catch (Exception ex) {
											Toast.makeText(
													getBaseContext(),
													getBaseContext()
													.getResources()
													.getString(
															R.string.txtPerfilesToastPerfilErrorAlBorrar)
															+ ": " + ex.getMessage(),
															Toast.LENGTH_SHORT).show();
										}
									}
								});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
