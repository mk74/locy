package com.example.accelerometerhelloworld;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

	public SensorManager mSensorManager;
	private Sensor mAcceler;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String message="";
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor mSensor: deviceSensors){
			message+=mSensor.getName() + " enery:" + mSensor.getPower()+ " minDelay:" + mSensor.getMinDelay() 
					+ " vendor:" + mSensor.getVendor() + "\n\n";
		}
		
		mAcceler = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		TextView textView = new TextView(this);
		textView.setTextSize(16);
		textView.setText("Value: " + message);
		
		setContentView(textView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSensorManager.registerListener(this, mAcceler, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float coord_x = event.values[0]; 
		float coord_y = event.values[1]; 
		float coord_z = event.values[2]; 
		System.out.println("Current x,y,z:"+ coord_x + "," + coord_y + "," + coord_z + "\n");
	}

}
