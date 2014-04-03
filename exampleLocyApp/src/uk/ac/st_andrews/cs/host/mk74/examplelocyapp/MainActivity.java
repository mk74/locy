package uk.ac.st_andrews.cs.host.mk74.examplelocyapp;

import java.util.Timer;
import java.util.TimerTask;

import uk.ac.st_andrews.cs.host.mk74.locy.LocyNavigatorApi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {
	protected static final String TAG = "Examplelocyapp";
	
	public static boolean EXPERIMENT_ON = true;
	public static double SCREEN_UDPATE_FREQUENCY = 3.0; // 3.0 - experiment, 0.5 - debugging

	BatteryEvaluator batteryEvaluator;
//	LocyNavigator locyNavigator;
	
	private LocyNavigatorApi api;
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		  @Override
		  public void onServiceConnected(ComponentName name, IBinder service) {
		    Log.i(TAG, "Service connection established");
		 
		    // that's how we get the client side of the IPC connection
		    api = LocyNavigatorApi.Stub.asInterface(service);
		    try {
		    	api.start();
		    } catch (RemoteException e) {
		      Log.e(TAG, "Failed to start service", e);
		    }
		     
		  }
		 
		  @Override
		  public void onServiceDisconnected(ComponentName name) {
		    Log.i(TAG, "Service connection closed");      
		  }
		};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//declare api + start LocyNavigator
		String intentName = "uk.ac.st_andrews.cs.host.mk74.locy.LocyNavigatorService";
		Intent intent = new Intent(intentName);
		startService(intent);
	    bindService(intent, serviceConnection, 0);
	      
		
		//set up components: WiFiNavigator and BatteryEvaluator	
//		locyNavigator = new LocyNavigator(getApplicationContext());
		batteryEvaluator = new BatteryEvaluator(getApplicationContext());
//		locyNavigator.start();
		batteryEvaluator.enable();
		
		//prepare screen/layout
		Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		
		//print (screen & logcat) state of Navigator:
		//current localization, whether WiFiNavigator is enabled and battery info(level + time difference)
		TimerTask readValues = new TimerTask() {
			@Override
			public void run() {
//				String locyInfo = locyNavigator.getInfo();
				String locyInfo = "";
				try {
					locyInfo += api.getInfo();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				
				if(!EXPERIMENT_ON){
//					locyInfo += locyNavigator.getDebuggerInfo();
					try {
						locyInfo += api.getDebuggerInfo();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				final String output = locyInfo + batteryEvaluator.getInfo();
				System.out.println(output);
								
				runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                    	TextView tv = (TextView) findViewById(R.id.textView1);
                    	tv.setText(output);
                    }
				});				
			}
		};
		Timer timer = new Timer();
		long delay = (long) (SCREEN_UDPATE_FREQUENCY * 1000);
		long period = (long) (SCREEN_UDPATE_FREQUENCY * 1000);
		timer.schedule(readValues, delay, period);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
