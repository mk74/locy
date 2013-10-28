package uk.ac.st_andrews.cs.host.mk74.energymeasurement.accelerometer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		class EmptySensorEventListener implements SensorEventListener{

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				TextView tv = (TextView) findViewById(R.id.textView1);
				String output = "Values: " + event.values[0] + " " + event.values[1] + " " + event.values[2] +"\n";
				tv.setText(output);
				System.out.println(output);
			}
			
		}
		
		mSensorManager.registerListener(new EmptySensorEventListener(), mAccelerometer, 0);
		
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
