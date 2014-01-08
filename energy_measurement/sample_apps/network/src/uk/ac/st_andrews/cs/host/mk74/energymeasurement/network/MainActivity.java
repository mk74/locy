package uk.ac.st_andrews.cs.host.mk74.energymeasurement.network;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {
	final private static int BATTERY_LEVEL_START=99;
	final private static int BATTERY_LEVEL_END=98;
	final private static int BATTERY_CHECK_FREQUENCY = 1;
	
	public double[] values;
	public Date batteryLevelStartTime, batteryLevelEndTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//register listener for network location service and save position
		values = new double[]{(double) 0.0, (double) 0.0};
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		class EmptyLocationListener implements LocationListener{
			
			@Override
			public void onLocationChanged(Location loc) {
				values[0] = loc.getLongitude();
				values[1] = loc.getLatitude();
			}

			@Override
			public void onProviderDisabled(String arg0) {}

			@Override
			public void onProviderEnabled(String arg0) {}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
		}
		LocationListener locListener = new EmptyLocationListener();
		((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
		String locProvider = LocationManager.NETWORK_PROVIDER;
		int minTime = 0;
		int minDistance = 0;
		locManager.requestLocationUpdates(locProvider, minTime, minDistance, locListener);
		System.out.println("Network Location listener is registered\n");
		
		//print results every sec and check battery life whether the defined range was depleted  
		TimerTask readValues = new TimerTask() {
			
			@Override
			public void run() {
				String output = "Values: " + values[0] + " " + values[1] + "\n";
				System.out.println(output);
				
				IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
				Intent batteryStats = registerReceiver(null, ifilter);
				int level = batteryStats.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				if(level == BATTERY_LEVEL_START && batteryLevelStartTime == null){
					final String batteryOutput = "Battery level start is reached\n";
					System.out.println(batteryOutput);
					batteryLevelStartTime = new Date();
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							TextView tv = (TextView) findViewById(R.id.textView1);
							tv.setText(batteryOutput);
						}
					});
				}
				if(level == BATTERY_LEVEL_END && batteryLevelEndTime == null){
					System.out.println("Battery level end is reached\n");
					batteryLevelEndTime = new Date();
				}
				if(batteryLevelStartTime != null && batteryLevelEndTime != null){
					int diffInSecs = (int) ((batteryLevelEndTime.getTime() - batteryLevelStartTime.getTime()) / 1000);
					final String batteryOutput = "Time difference in secs is: " + diffInSecs + "\n";
					System.out.println(batteryOutput);
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							TextView tv = (TextView) findViewById(R.id.textView1);
							tv.setText(batteryOutput);
						}
					});
				}
			}
		};
		Timer timer = new Timer();
		long delay = BATTERY_CHECK_FREQUENCY * 1000;
		long period = BATTERY_CHECK_FREQUENCY * 1000;
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
