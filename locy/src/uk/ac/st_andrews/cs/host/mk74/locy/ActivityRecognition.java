package uk.ac.st_andrews.cs.host.mk74.locy;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ActivityRecognition {
	
	private boolean running = false;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private SensorEventListener mAccelerometerSensorEventListener;
	private LocyNavigator locyNavigator;
	private AccelerometerDataClassifier accelerometerDataClasifier;
	private boolean moving = true;
	
	public ActivityRecognition(LocyNavigator navigator, Context context) {
		this.locyNavigator = navigator;
		accelerometerDataClasifier = new AccelerometerDataClassifier();
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mAccelerometerSensorEventListener = new SensorEventListener() {

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				boolean newMoving = accelerometerDataClasifier.add(event.values);
				
				//if activity classification changed, send it over to locyNavigator 
				if(newMoving != moving){
					if(newMoving)
						locyNavigator.activityMoving();
					else
						locyNavigator.activityInPlace();
					moving = newMoving;
				}
			}
			
		};
	}

	public void start() {
		running = true;
		mSensorManager.registerListener(mAccelerometerSensorEventListener, mAccelerometer, 0);
	}
	
	public void stop() {
		mSensorManager.unregisterListener(mAccelerometerSensorEventListener);
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public boolean isMoving() {
		return moving;
	}

	public String getInfo() {
		return accelerometerDataClasifier.getInfo();
	}
}
