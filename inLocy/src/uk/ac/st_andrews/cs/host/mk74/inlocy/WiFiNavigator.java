package uk.ac.st_andrews.cs.host.mk74.inlocy;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

//WiFiNavigator
//Scans available networks and send it over the network to fetch current mobile phone's localization
//
// New scan is triggered once previous one is finished(continuous scanning)
// Sending over scan results happen as often as new ones are available
// Assumes that WiFi is enabled
public class WiFiNavigator {
	public static String WIFI_FINGERPRINTS_SERVER = "http://mk74.host.cs.st-andrews.ac.uk/locy/location.php";
	public static String WIFI_FINGERPRINTS_SERVER_VAR = "wifi_scan_results";
	
	private boolean running = false;
	private WifiManager wifiManager;
	private IntentFilter wifiIntentFilter;
	private BroadcastReceiver wifiBroadcastReceiver;
	private Context context;
	private String location;
		
	public WiFiNavigator(Context context) {
		this.context = context;
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		location = "unknown";
		
		//intent and receiver for scheduling new scan when previous finished & sending results to get current location
		wifiIntentFilter = new IntentFilter();
		wifiIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		wifiBroadcastReceiver= new BroadcastReceiver(){		
			@Override
			public void onReceive(Context c, Intent i){
				new SendScanResultsTask().execute();
				wifiManager.startScan();
		    }
		};
	}
	
	//extract localization from web service response
	private void extractLocalization(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			location = reader.readLine(); // assumes that output is just one line
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//create String containing list of all available networks with BSSID, SSID and its strength level
	private String constructScanResuls() {
		StringBuffer buff = new StringBuffer();
		List<ScanResult> scanResults = wifiManager.getScanResults();
		for(ScanResult scanResult : scanResults){
			buff.append(scanResult.BSSID + " | " + scanResult.SSID + " | " + scanResult.level);
		}
		return buff.toString();
	}
	
	public void start() {
		if(!running){
			running = true;
			context.registerReceiver(wifiBroadcastReceiver, wifiIntentFilter);
			wifiManager.setWifiEnabled(true);
			while(!wifiManager.isWifiEnabled()){
				//wait till WiFI is enabled
			}
			wifiManager.startScan();
		}
	}
	
	public void stop() {
		running = false;
		context.unregisterReceiver(wifiBroadcastReceiver);
		wifiManager.setWifiEnabled(false);
		while(wifiManager.isWifiEnabled()){
			//wait till WiFI is enabled
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public String getLocation() {
		return location;
	}
	
	//Sends WifiScan results over the network to get current location
	private class SendScanResultsTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			//construct POST data with Wifi scan results
			String results = constructScanResuls();
			String post_data = "";
			try {
				post_data = URLEncoder.encode(WIFI_FINGERPRINTS_SERVER_VAR, "UTF-8") + "=" + 
								 	URLEncoder.encode(results, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//send those scan results and get the response where user is located
			try {
				URL url = new URL(WIFI_FINGERPRINTS_SERVER);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("POST");
				urlConnection.setDoInput(true);
				urlConnection.setDoOutput(true);
				
				OutputStream os = urlConnection.getOutputStream();
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				writer.write(post_data);
				writer.flush();
				writer.close();
				
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				extractLocalization(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}	
	}
}
