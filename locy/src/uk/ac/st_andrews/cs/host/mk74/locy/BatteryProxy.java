package uk.ac.st_andrews.cs.host.mk74.locy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

//BatteryProxy
//Monitors battery's level
//It notifies LocyNavigator if a battery level has changed significantly
public class BatteryProxy {
	private final static int SIGNIFICANT_CHANGE = 20;
	
	private boolean enabled = false;
	
	IntentFilter batteryIntentFilter;
	private BroadcastReceiver batteryBroadcastReceiver;
	private Context context;
	private int lastBatteryLevel;
	
	public BatteryProxy(final LocyNavigator locyNavigator, Context context) {
		this.context = context;
		
		//get current battery life
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStats = context.registerReceiver(null, ifilter);
		lastBatteryLevel = batteryStats.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		
		//intent and receiver which is triggered when the battery level has been changed 
		batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				int newBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				
				if( Math.abs(newBatteryLevel - lastBatteryLevel) > SIGNIFICANT_CHANGE){
					locyNavigator.batteryChanged(newBatteryLevel);
					lastBatteryLevel = newBatteryLevel;
				}
			}

		};
	}
	
	public void enable(){
		enabled = true;
		context.registerReceiver(batteryBroadcastReceiver, batteryIntentFilter);
	}
	
	public void disable() {
		enabled = false;
		context.unregisterReceiver(batteryBroadcastReceiver);
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public int getLastBatteryLevel() {
		return lastBatteryLevel;
	}
	
	public String getInfo() {
		return "Battery level: " + getLastBatteryLevel() + " | ";
	}
}
