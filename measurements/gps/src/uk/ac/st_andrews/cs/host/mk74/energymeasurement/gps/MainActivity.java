package uk.ac.st_andrews.cs.host.mk74.energymeasurement.gps;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	public double[] position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		position = new double[]{(double) 0.0, (double) 0.0};
		class EmptyLocationListener implements LocationListener{
			
			@Override
			public void onLocationChanged(Location loc) {
				position[0] = loc.getLongitude();
				position[1] = loc.getLatitude();
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
		System.out.println("GPS Location listener is registered\n");
		
			TimerTask readValues = new TimerTask() {
			
			@Override
			public void run() {
				String output = "Position: " + position[0] + " " + position[1] + "\n";
				System.out.println(output);
			}
		};
		Timer timer = new Timer();
		long delay = 5 * 1000;
		long period = 5 * 1000;
		timer.schedule(readValues, delay, period);
		
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
