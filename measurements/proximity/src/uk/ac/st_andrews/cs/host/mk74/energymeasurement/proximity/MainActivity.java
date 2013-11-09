package uk.ac.st_andrews.cs.host.mk74.energymeasurement.proximity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor mProximity= mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		
		class EmptySensorEventListener implements SensorEventListener{

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				String output = "Values: " + event.values[0] + "\n";
				System.out.println(output);
			}
			
		}
		
		mSensorManager.registerListener(new EmptySensorEventListener(), mProximity, 0);
		
		
		Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
