package uk.ac.st_andrews.cs.host.mk74.energymeasurement.light;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {
	final private static int BATTERY_LEVEL_START=99;
	final private static int BATTERY_LEVEL_END=98;
	final private static int BATTERY_CHECK_FREQUENCY = 1;
	
	public float[] values;
	public Date batteryLevelStartTime, batteryLevelEndTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//register listener for light data and save it
		values = new float[]{(float) 0.0};
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		class EmptySensorEventListener implements SensorEventListener{

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				values[0] = event.values[0];
			}
			
		}
		int rate = 0;
		mSensorManager.registerListener(new EmptySensorEventListener(), mLight, rate);
		
		//print results every sec and check battery life whether the defined range was depleted  
		TimerTask readValues = new TimerTask() {
					
			@Override
			public void run() {
				String output = "Values: " + values[0] + "\n";
				System.out.println(output);
						
				IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
				Intent batteryStats = registerReceiver(null, ifilter);
				int level = batteryStats.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				if(level == BATTERY_LEVEL_START && batteryLevelStartTime == null){
					final String batteryOutput = "Battery level start is reached\n";
					System.out.println(batteryOutput);
					batteryLevelStartTime = new Date();
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							TextView tv = (TextView) findViewById(R.id.textView1);
							tv.setText(batteryOutput);
						}
					});
				}
				if(level == BATTERY_LEVEL_END && batteryLevelEndTime == null){
					System.out.println("Battery level end is reached\n");
					batteryLevelEndTime = new Date();
				}
				if(batteryLevelStartTime != null && batteryLevelEndTime != null){
					int diffInSecs = (int) ((batteryLevelEndTime.getTime() - batteryLevelStartTime.getTime()) / 1000);
					final String batteryOutput = "Time difference in secs is: " + diffInSecs + "\n";
					System.out.println(batteryOutput);
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							TextView tv = (TextView) findViewById(R.id.textView1);
							tv.setText(batteryOutput);
						}
					});
				}
			}
		};
		Timer timer = new Timer();
		long delay = BATTERY_CHECK_FREQUENCY * 1000;
		long period = BATTERY_CHECK_FREQUENCY * 1000;
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