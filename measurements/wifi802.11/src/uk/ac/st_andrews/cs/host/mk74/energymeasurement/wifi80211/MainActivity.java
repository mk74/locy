package uk.ac.st_andrews.cs.host.mk74.energymeasurement.wifi80211;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
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
	
	public Date batteryLevelStartTime, batteryLevelEndTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//WiFi Setup
		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
		
		//schedule new scan, only if previous is finished
		IntentFilter i = new IntentFilter();
		i.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		BroadcastReceiver receiver= new BroadcastReceiver(){
			
			@Override
			public void onReceive(Context c, Intent i){
				wifiManager.startScan();
				System.out.println("New scan started");
		    }
		};
		registerReceiver(receiver, i );
		
		
		//run first scan, print results every sec and check battery life whether the given range was depleted 
		wifiManager.startScan();
		TimerTask scanNetworksTask = new TimerTask() {
			
			@Override
			public void run() {				
				//print available WiFi Scan results:
				List<ScanResult> scans = wifiManager.getScanResults();
				StringBuffer buff = new StringBuffer("");
				Iterator<ScanResult> it = scans.iterator();
				while(it.hasNext()){
					ScanResult scanResult = it.next();
					buff.append("SSID: " + scanResult.SSID+ "\tBSSID:" + scanResult.BSSID + "\tcapabilites" + scanResult.capabilities);
					buff.append("\tfrequency:" + scanResult.frequency + "\tlevel:" + scanResult.level + "\n");
				}
				System.out.println(buff.toString());
				
				//battery measurements && time which was needed to deplete the battery range
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
		timer.schedule(scanNetworksTask, delay, period);

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
