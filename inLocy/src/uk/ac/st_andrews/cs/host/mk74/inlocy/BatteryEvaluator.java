package uk.ac.st_andrews.cs.host.mk74.inlocy;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

//BatteryEvaluator
//Monitors battery performance - checks how long 1% depletion of battery life takes
public class BatteryEvaluator {
	final private static int BATTERY_LEVEL_START=99;
	final private static int BATTERY_LEVEL_END=98;
	
	private Date batteryLevelStartTime, batteryLevelEndTime;
	private boolean enabled = false;
	private int timeDifference = -1;
	private int batteryLevel = -1;
	
	IntentFilter batteryIntentFilter;
	private BroadcastReceiver batteryBroadcastReceiver;
	private Context context;
	
	public BatteryEvaluator(Context context) {
		this.context = context;
		
		//intent and receiver which update battery level and estimates time for 1% of battery
		batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		batteryBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				
				if(batteryLevel == BATTERY_LEVEL_START && batteryLevelStartTime == null){
					System.out.println("Battery level start is reached\n");
					batteryLevelStartTime = new Date();
				}
				if(batteryLevel == BATTERY_LEVEL_END && batteryLevelEndTime == null){
					System.out.println("Battery level end is reached\n");
					batteryLevelEndTime = new Date();
				}
				if(batteryLevelStartTime != null && batteryLevelEndTime != null){
					timeDifference = (int) ((batteryLevelEndTime.getTime() - batteryLevelStartTime.getTime()) / 1000);
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

	public int getTimeDifference() {
		return timeDifference;
	}
	
	public int getBatteryLevel() {
		return batteryLevel;
	}
	
	public String getInfo() {
		return "Battery level: " + getBatteryLevel() + " | " +
				"Time difference: " + getTimeDifference() + "\n";

	}
}
