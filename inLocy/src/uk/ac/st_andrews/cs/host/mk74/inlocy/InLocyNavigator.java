package uk.ac.st_andrews.cs.host.mk74.inlocy;

import android.content.Context;

public class InLocyNavigator {

	private WiFiNavigator wifiNavigator;
	private ActivityRecognition activityRecognition;
	private boolean running = false;
	private boolean inPlace = false;
	private String location;
	
	public InLocyNavigator(Context context) {
		wifiNavigator = new WiFiNavigator(context);
		activityRecognition = new ActivityRecognition(this, context);
	}

	public void start() {
		wifiNavigator.start();
		activityRecognition.start();
		running = true;
	}
	
	public void stop(){
		running = false;
		activityRecognition.stop();
		wifiNavigator.stop();
	}
	
	//if user is moving, start WiFiNavigator
	public void activityMoving(){
		inPlace = false;
		if(!wifiNavigator.isRunning())
			wifiNavigator.start();
	}
	
	//if user is in Place, copy last location from WiFiNavigator and stop WiFiNagivator
	public void activityInPlace() {
		location = wifiNavigator.getLocation();
		if(wifiNavigator.isRunning())
			wifiNavigator.stop();
		inPlace = true;
	}

	public String getLocation() {
		if(inPlace)
			return location;
		else
			return wifiNavigator.getLocation();
	}
	
	public WiFiNavigator getWifiNavigator() {
		return wifiNavigator;
	}
	
	public ActivityRecognition getActivityRecognition() {
		return activityRecognition;
	}

	public boolean isRunning() {
		return running;
	}

}
