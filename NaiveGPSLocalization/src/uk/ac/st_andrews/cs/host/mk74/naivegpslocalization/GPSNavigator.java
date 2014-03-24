package uk.ac.st_andrews.cs.host.mk74.naivegpslocalization;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

//WiFiNavigator
//Scans available networks and send it over the network to fetch current mobile phone's localization
//
// New scan is triggered once previous one is finished(continuous scanning)
// Sending over scan results happen as often as new ones are available
// Assumes that WiFi is enabled
public class GPSNavigator {
	public static String WIFI_FINGERPRINTS_SERVER = "http://mk74.host.cs.st-andrews.ac.uk/locy/location.php";
	public static String WIFI_FINGERPRINTS_SERVER_VAR = "wifi_scan_results";
	
	private boolean running = false;
	private Context context; //maybe no need?!
	LocationManager  locManager;
	private LocationListener locListener;
	private String locProvider;
	private int minTime = 0, minDistance = 0;
	private double[] location;
	
		
	public GPSNavigator(Context context) {
		this.context = context;
		
		//create listener for gps location service
		location = new double[]{(double) 0.0, (double) 0.0};
		locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		class EmptyLocationListener implements LocationListener{
			
			@Override
			public void onLocationChanged(Location loc) {
				location[0] = loc.getLongitude();
				location[1] = loc.getLatitude();
			}

			@Override
			public void onProviderDisabled(String arg0) {}

			@Override
			public void onProviderEnabled(String arg0) {}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
		}
		locListener = new EmptyLocationListener();		
		locProvider = LocationManager.GPS_PROVIDER;
	}
	
	public void start() {
		if(!running){
			running = true;
			//switch on GPS //order?
			locManager.requestLocationUpdates(locProvider, minTime, minDistance, locListener);			
		}
	}
	
	public void stop() {
		running = false;
		//unregister listener
		//switch off GPS //order?
		locManager.removeUpdates(locListener);
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public double[] getLocation() {
		return location;
	}
}