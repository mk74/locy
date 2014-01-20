package uk.ac.st_andrews.cs.host.mk74.inlocy;

import android.content.Context;

public class InLocyNavigator {

	private WiFiNavigator wifiNavigator;
	private ActivityClassifier activityClassifier;
	private boolean running = false;
	private boolean inPlace = false;
	private String location;
	
	public InLocyNavigator(Context context) {
		wifiNavigator = new WiFiNavigator(context);
		activityClassifier = new ActivityClassifier(this, context);
	}

	public void start() {
		wifiNavigator.start();
		activityClassifier.start();
		running = true;
	}
	
	public void stop(){
		running = false;
		activityClassifier.stop();
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

	public boolean isRunning() {
		return running;
	}

}
