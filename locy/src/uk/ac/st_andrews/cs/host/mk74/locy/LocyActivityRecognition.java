package uk.ac.st_andrews.cs.host.mk74.locy;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LocyActivityRecognition {
	LocyManager locyManager;
	
	public LocyActivityRecognition(LocyManager locyManager) {
		this.locyManager = locyManager;
	}
	
	public void register() {
		SensorManager mSensorManager = locyManager.getmSensorManager();
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		class ARSensorEventListener implements SensorEventListener{

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				String output = "Values: " + event.values[0] + " " + event.values[1] + " " + event.values[2] +"\n";
				LocyEvent locyEvent = new LocyEvent(output);
				locyManager.locyEventListener.onLocyChanged(locyEvent);
			}
		}
		int rate = 1000000 * 1000000;
		mSensorManager.registerListener(new ARSensorEventListener(), mAccelerometer, rate);
	}
	
}
