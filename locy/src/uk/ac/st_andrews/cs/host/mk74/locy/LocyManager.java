package uk.ac.st_andrews.cs.host.mk74.locy;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;


public class LocyManager {
	LocationManager locationManager;
	SensorManager mSensorManager;
	
	public LocyManager(Activity activity){
		mSensorManager =  (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE); 
		locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);	
	}
	
	
}
