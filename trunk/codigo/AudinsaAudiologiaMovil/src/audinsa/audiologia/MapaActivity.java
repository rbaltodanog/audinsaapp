package audinsa.audiologia;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class MapaActivity extends FragmentActivity implements
		OnMapClickListener {
	private final LatLng UPV = new LatLng(9.949407, -84.058312);
	private final LatLng UPV2 = new LatLng(9.975042, -84.118026);
	private GoogleMap mapa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa);
		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV2, 15));

		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);
		// Marcador Audinsa Guadalupe
		mapa.addMarker(new MarkerOptions()
				.position(UPV)
				.title("UPV")
				.snippet("Audinsa-Guadalupe")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher))
				.anchor(0.5f, 0.5f));

		// Marcador Audinsa Heredia
		mapa.addMarker(new MarkerOptions()
				.position(UPV2)
				.title("UPV2")
				.snippet("Audinsa-Heredia")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher))
				.anchor(0.5f, 0.5f));

		mapa.setOnMapClickListener(this);
	}

	public void moveCamera(View view) {
		mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
	}

	public void animateCamera(View view) {
		if (mapa.getMyLocation() != null)
			mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					mapa.getMyLocation().getLatitude(), mapa.getMyLocation()
							.getLongitude()), 15));
	}

	public void addMarker(View view) {
		mapa.addMarker(new MarkerOptions().position(new LatLng(mapa
				.getCameraPosition().target.latitude,
				mapa.getCameraPosition().target.longitude)));

	}

	@Override
	public void onMapClick(LatLng puntoPulsado) {
		mapa.addMarker(new MarkerOptions().position(puntoPulsado).icon(
				BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
	}
}
