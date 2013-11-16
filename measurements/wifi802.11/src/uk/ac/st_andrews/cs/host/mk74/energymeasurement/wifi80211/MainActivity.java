package uk.ac.st_andrews.cs.host.mk74.energymeasurement.wifi80211;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
		TimerTask scanNetworksTask = new TimerTask() {
			
			@Override
			public void run() {
				WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				wifiManager.startScan();
				List<ScanResult> scans = wifiManager.getScanResults();
				
				StringBuffer buff = new StringBuffer("");
				Iterator<ScanResult> it = scans.iterator();
				while(it.hasNext()){
					ScanResult scanResult = it.next();
					buff.append("SSID: " + scanResult.SSID+ "\tBSSID:" + scanResult.BSSID + "\tcapabilites" + scanResult.capabilities);
					buff.append("\tfrequency:" + scanResult.frequency + "\tlevel:" + scanResult.level + "\n"); 
				}
				System.out.println(buff.toString());
			}
		};
		Timer timer = new Timer();
		long delay = 5 * 1000;
		long period = 5 * 1000;
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
