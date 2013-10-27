package uk.ac.st_andrews.cs.host.mk74.energymeasurement.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		LocationListener locListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location loc) {}

			@Override
			public void onProviderDisabled(String arg0) {}

			@Override
			public void onProviderEnabled(String arg0) {}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
			
		};
		
		String locProvider = LocationManager.GPS_PROVIDER;
		locManager.requestLocationUpdates(locProvider, 0, 0, locListener);
		System.out.println("Location listener is registered\n");
		
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
