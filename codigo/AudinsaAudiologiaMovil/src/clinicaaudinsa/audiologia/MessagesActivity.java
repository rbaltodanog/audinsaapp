package clinicaaudinsa.audiologia;

import java.util.ArrayList;

import com.google.android.gcm.GCMRegistrar;

import clinicaaudinsa.audiologia.Adapters.MessageItemAdapter;
import clinicaaudinsa.audiologia.businessdomain.Mensaje;
import clinicaaudinsa.audiologia.businessdomain.Perfil;
import clinicaaudinsa.audiologia.datasources.MensajeDataSource;
import clinicaaudinsa.audiologia.datasources.PerfilDataSource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import static clinicaaudinsa.audiologia.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static clinicaaudinsa.audiologia.CommonUtilities.EXTRA_MESSAGE;
import static clinicaaudinsa.audiologia.CommonUtilities.SENDER_ID;

public class MessagesActivity extends Activity {
	private MensajeDataSource dataSource;
	
	// Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
     
    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
	
	// Connection detector
    ConnectionDetector cd;
    public static String name;
    public static String email;
    private long idPerfil = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		idPerfil = getIntent().getLongExtra("idPerfil", 0);
		boolean perfilLoaded = false;
		Perfil p = new Perfil();
		
		if (idPerfil != 0)
		{
			perfilLoaded = true;
			p = getPerfil(idPerfil);
		}
		
		cd = new ConnectionDetector(getApplicationContext());
		dataSource = new MensajeDataSource(this);
		
		// Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MessagesActivity.this,
                    "Error de conexión",
                    "Asegure que este conectado a Internet", false);
            // stop executing code by return
            return;
        }
        
        if (perfilLoaded)
        {
	        name = p.getNombre();
	        email = p.getCorreoElectronico();
	        
			registerInGcm();
        }
        
        String newMessage = getIntent().getExtras().getString(EXTRA_MESSAGE);
        if (newMessage != null && !newMessage.isEmpty())
		{
			dataSource.crearMensaje(true, newMessage);
		}
        
        setContentView(R.layout.activity_messages);
		//unregisterInGcm();
		loadData();
		setOnListViewItemClickListener();
	}
	
	private void registerInGcm()
	{
		// Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
 
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));
         
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
 
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM           
            GCMRegistrar.register(getApplicationContext(), SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
                // Skips registration.              
                Toast.makeText(getApplicationContext(), "El dispositivo ya está inscrito en el servicio de Google Messaging", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, name, email, regId);
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
            }
        }
	}
	
	// Unregister this account/device pair within the server.
    void unregisterInGcm() {
    	// Get GCM registration id
       final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());
       ServerUtilities.unregister(getApplicationContext(), regId);
   }
	
	/**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyWakelockTag");
            wakeLock.acquire();
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            if (newMessage != null && !newMessage.isEmpty())
    		{
    			dataSource.crearMensaje(true, newMessage);
    			loadData();
    		}
            
            Toast.makeText(getApplicationContext(), "Nuevo mensaje: " + newMessage, Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            wakeLock.release();
        }
    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_messages, menu);
		
		return true;
	}
	
	private void loadData()
	{
		ArrayList<Mensaje> mensajes = dataSource.obtenerTodosLosMensajes();

		MessageItemAdapter adapter = new MessageItemAdapter(this, 
				mensajes);
		ListView listView = (ListView) findViewById(R.id.listMensajes);
		listView.setEmptyView(findViewById(R.id.lblMensajesVacio));
		listView.setAdapter(adapter);
	}
	
	// Obtiene el perfil por actualizar
	public Perfil getPerfil(long idPerfil) {
		PerfilDataSource dataSource = new PerfilDataSource(this);
		Perfil p = new Perfil();
		try {
			p = dataSource.buscarPerfil(idPerfil);
		} catch (Exception ex) {
			Log.w(PerfilDataSource.class.getName(),
					"Error tratando de obtener el perfil.");
			
		}
		return p;
	}
	
	@Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
	
	private void setOnListViewItemClickListener() {
		ListView lstView = (ListView) findViewById(R.id.listMensajes);
		if (lstView != null)
		{
			OnItemClickListener listener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Mensaje mensajeSeleccionado = null;
					MessageItemAdapter adapter = (MessageItemAdapter) parent.getAdapter();
					mensajeSeleccionado = adapter.getItem(position);
					// get prompts.xml view
					LayoutInflater li = LayoutInflater.from(MessagesActivity.this);
					View promptsView = li.inflate(R.layout.message_dialog, null);
					
					final AlertDialog alertDialog = new AlertDialog.Builder(MessagesActivity.this).create();
					alertDialog.setView(promptsView);
					
					final EditText messageInput = (EditText) promptsView
							.findViewById(R.id.txtMessage);
					messageInput.setText(mensajeSeleccionado.getTexto());
					
	                alertDialog.setTitle("Mensaje de texto:");
	                // Setting OK Button
	                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    }
	                });
	                // Showing Alert Message
	                alertDialog.show();
				}
	
			};
			lstView.setOnItemClickListener(listener);
		}
	}
}
