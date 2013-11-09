package uk.ac.st_andrews.cs.host.mk74.locy;

import java.util.ArrayList;
import java.util.Iterator;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LocyActivityRecognition {
	LocyManager locyManager;
	float[] averageOldValues = null;
	ArrayList<float[]> lastValues = new ArrayList<float[]>(); 
	static int BATCH_SIZE = 20;
	
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
				float[] averageValues;
				String output="";
				
				if(lastValues.size()<BATCH_SIZE)
					lastValues.add(event.values.clone());
				else{
					averageValues = calcAverageOldValues();
					lastValues.clear();
					if(averageOldValues != null){
						output = "Distance:"+ calcEuclideanDistance(averageOldValues, averageValues);
						LocyEvent locyEvent = new LocyEvent(output);
						locyManager.locyEventListener.onLocyChanged(locyEvent);
					}
					averageOldValues = averageValues;
				}
			}
		}
		int rate = 1000000 * 1000000;
		mSensorManager.registerListener(new ARSensorEventListener(), mAccelerometer, rate);
	}
	
	private float[] calcAverageOldValues(){
		float[] avg = new float[3];
		for(int i=0; i<3; i++)
			avg[i] = (float) 0.0;
		
		Iterator<float[]> it = lastValues.iterator();
		while(it.hasNext()){
			float[] tmp = it.next();
			for(int i=0; i<3; i++)
				avg[i]+= tmp[i];
		}
		
		for(int i=0; i<3; i++)
			avg[i] = avg[i]/lastValues.size();
		return avg;
	}
	
	private static int calcEuclideanDistance(float[] values1, float[] values2) {
		double sum = 0.0;
		for(int i=0;i<3;i++){
			sum += Math.pow(values1[i] - values2[i], 2.0);
		}
		return (int) Math.sqrt(sum);
	}
	
}
