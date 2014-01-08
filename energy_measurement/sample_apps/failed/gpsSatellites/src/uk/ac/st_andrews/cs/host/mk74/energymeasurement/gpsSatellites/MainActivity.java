package uk.ac.st_andrews.cs.host.mk74.energymeasurement.gpsSatellites;

import java.util.Iterator;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.GpsStatus.Listener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		//print available satellites
		class GPSStatusListener implements Listener{
			@Override
			public void onGpsStatusChanged(int event) {
				StringBuffer buff = new StringBuffer("");
				if(event == GpsStatus.GPS_EVENT_STARTED)
					buff.append("GPS system has started\n");
				else if(event == GpsStatus.GPS_EVENT_FIRST_FIX){
					GpsStatus gpsStatus = locManager.getGpsStatus(null);
					buff.append("GSP time to first fix: " + gpsStatus.getTimeToFirstFix() + "\n");
				} else{
					buff.append("Random nr: "+ new Random().nextInt() + "\n");
					GpsStatus gpsStatus = locManager.getGpsStatus(null);
					Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
					while(it.hasNext()){
						GpsSatellite gpsSatellite = it.next();
						buff.append(gpsSatellite.getElevation() + " " + gpsSatellite.getAzimuth() + "\n");				
					}
				}
				System.out.println(buff);
				TextView tv = (TextView) findViewById(R.id.textView1);
				tv.setText(buff.toString());
			}			
		}
		locManager.addGpsStatusListener(new GPSStatusListener());
		
		//start GPS system
		class EmptyLocationListener implements LocationListener{
			
			@Override
			public void onLocationChanged(Location loc) {			
			}

			@Override
			public void onProviderDisabled(String arg0) {}

			@Override
			public void onProviderEnabled(String arg0) {}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
		}
		LocationListener locListener = new EmptyLocationListener();		
		String locProvider = LocationManager.GPS_PROVIDER;
		locManager.requestLocationUpdates(locProvider, 0, 0, locListener);
		
		
		Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
