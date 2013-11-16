package uk.ac.st_andrews.cs.host.mk74.energymeasurement.gyroscope;

import java.util.Timer;
import java.util.TimerTask;

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
	public float[] values;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		values = new float[]{(float) 0.0, (float) 0.0, (float) 0.0};
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		
		class EmptySensorEventListener implements SensorEventListener{

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				for(int i=0; i<3; i++)
					values[i] = event.values[i];
			}
			
		}
		
		int rate = 0;
		mSensorManager.registerListener(new EmptySensorEventListener(), mGyroscope, rate);
		
		TimerTask readValues = new TimerTask() {
			
			@Override
			public void run() {
				String output = "Values: " + values[0] + " " + values[1] + " " + values[2] +"\n";
				System.out.println(output);
			}
		};
		Timer timer = new Timer();
		long delay = 5 * 1000;
		long period = 5 * 1000;
		timer.schedule(readValues, delay, period);
		
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
