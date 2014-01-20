package uk.ac.st_andrews.cs.host.mk74.inlocy;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ActivityClassifier {
	
	private boolean running = false;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private SensorEventListener mAccelerometerSensorEventListener;
	private InLocyNavigator inLocyNavigator;
	private boolean moving = true;
	
	public ActivityClassifier(InLocyNavigator navigator, Context context) {
		this.inLocyNavigator = navigator;
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mAccelerometerSensorEventListener = new SensorEventListener() {

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
//				for(int i=0; i<3; i++)
//					values[i] = event.values[i];
				boolean newMoving = true;
				
				//if activity classification changed, send it over to inLocyNavigator 
				if(newMoving != moving){
					if(newMoving)
						inLocyNavigator.activityMoving();
					else
						inLocyNavigator.activityInPlace();
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
}
