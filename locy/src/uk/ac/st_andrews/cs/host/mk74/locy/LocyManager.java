package uk.ac.st_andrews.cs.host.mk74.locy;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;


public class LocyManager {
	Activity activity;
	LocationManager locationManager;
	SensorManager mSensorManager;
	LocyEventListener locyEventListener;
	
	public LocyManager(Activity activity){
		this.activity = activity;
		mSensorManager =  (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE); 
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);	
	}
	
	public void registerListener(LocyEventListener locyEventListener) {
		this.locyEventListener = locyEventListener;
		LocyActivityRecognition locyActivityRecognition = new LocyActivityRecognition(this);
		locyActivityRecognition.register();
	}
	
	public Activity getActivity() {
		return activity;
	}
	
	public LocyEventListener getLocyEventListener() {
		return locyEventListener;
	}
	
	public SensorManager getmSensorManager() {
		return mSensorManager;
	}
}
