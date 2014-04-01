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
	private boolean activityMoving = true;
	private int sleepingIntervalWeight;
	
	public ActivityRecognition(LocyNavigator navigator, Context context, int sleepingIntervalWeight) {
		this.locyNavigator = navigator;
		this.sleepingIntervalWeight = sleepingIntervalWeight;
		
		accelerometerDataClasifier = new AccelerometerDataClassifier();
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mAccelerometerSensorEventListener = new SensorEventListener() {

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				synchronized (this) {
					//add new values, try to recognize activity and check whether recognition process is over
					accelerometerDataClasifier.add(event.values);
					boolean newActivityMoving = accelerometerDataClasifier.recognizeActivity();
					boolean isNewActivity = accelerometerDataClasifier.isRecognitionOver();
					
					//if new activity and activity classification changed, send it over to locyNavigator 
					if(isNewActivity){
						if(newActivityMoving != activityMoving){
							if(newActivityMoving)
								locyNavigator.activityMoving();
							else
								locyNavigator.activityInPlace();
							activityMoving = newActivityMoving;
						}
						
						sleepingInterval();
					}

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

	public String getInfo() {
		return "running: " + isRunning() + " | " + "sleepign interval weight: " + sleepingIntervalWeight + 
				" | " + accelerometerDataClasifier.getInfo();
	}

	public void setSleepingIntervalWeight(int sleepingIntervalWeight) {
		this.sleepingIntervalWeight = sleepingIntervalWeight;
	}
	
	private void sleepingInterval() {
		new Thread(new Runnable() {

			public void run() {
	        	stop();
	        	try {
	        		int samplingTime= AccelerometerDataClassifier.WINDOW_N * AccelerometerDataClassifier.WINDOW_TIME_MILLISECS;
					Thread.sleep(samplingTime * sleepingIntervalWeight);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	accelerometerDataClasifier.clear();
	        	start();
	        }
		}).start();
	}
}
