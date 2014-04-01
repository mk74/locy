package uk.ac.st_andrews.cs.host.mk74.locy;

import android.content.Context;
import android.location.Location;

public class LocyNavigator {
	public static int BATTERY_SIGNIFICANT_CHANGE = 20;
	
	private BatteryProxy batteryProxy;
	private GPSNavigator gpsNavigator;
	private ActivityRecognition activityRecognition;
	private boolean running = false;
	private boolean inPlace = false;
	private Location location;
	
	public LocyNavigator(Context context) {
		batteryProxy = new BatteryProxy(this, context);
		gpsNavigator = new GPSNavigator(context);
		int sleepingIntervalWeight = calcSleepingIntervalWeight(batteryProxy.getLastBatteryLevel());
		activityRecognition = new ActivityRecognition(this, context, sleepingIntervalWeight);
	}

	//starts the system (trigger gpsNavigatior + activityRecognition system + BatteryEvalutor)
	public void start() {
		gpsNavigator.start();
		activityRecognition.start();
		running = true;
	}
	
	public void stop(){
		running = false;
		activityRecognition.stop();
		gpsNavigator.stop();
	}
	
	//if user is moving, start GPSNavigator
	public void activityMoving(){
		inPlace = false;
		if(!gpsNavigator.isRunning())
			gpsNavigator.start();
	}
	
	//if user is in Place, copy last location from WiFiNavigator and stop WiFiNagivator
	public void activityInPlace() {
		location = gpsNavigator.getLocation();
		if(gpsNavigator.isRunning())
			gpsNavigator.stop();
		inPlace = true;
	}
	
	//if battery levels has significantly changed:
	public void batteryChanged(int batteryLevel) {
		int sleepingIntervalWeight = calcSleepingIntervalWeight(batteryLevel);
		activityRecognition.setSleepingIntervalWeight(sleepingIntervalWeight);
	}

	public Location getLocation() {
		if(inPlace)
			return location;
		else
			return gpsNavigator.getLocation();
	}
	
	public ActivityRecognition getActivityRecognition() {
		return activityRecognition;
	}

	public boolean isRunning() {
		return running;
	}

	public String getInfo() {
		String output;
		if(getLocation()!=null){
		 output = "Location: " + getLocation().getLongitude() + " " + getLocation().getLatitude();
		}else{
			output = "Location: unknown";
		}
						
		output += " | GPSNavigator running:" + gpsNavigator.isRunning()+ "\n";
		return output;
	}

	public String getDebuggerInfo() {
		String output = activityRecognition.getInfo();
		return output;
	}
	
	//formula for calculating sleeping time weight for activity recognition
	//depending on current battery level
	private int calcSleepingIntervalWeight(int batteryLevel) {
		int sleepingIntervalWeight = (100/batteryLevel)/BATTERY_SIGNIFICANT_CHANGE + 1;
		return sleepingIntervalWeight;
	}
}
