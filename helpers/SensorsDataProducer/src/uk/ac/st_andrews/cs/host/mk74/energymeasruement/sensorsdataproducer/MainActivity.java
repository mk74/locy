package uk.ac.st_andrews.cs.host.mk74.energymeasruement.sensorsdataproducer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	private boolean isMoving = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		System.out.println("First_new activity: inPlace"); //assumes that user is in place first
		
		//register accelerometer listener + print its data with time
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		class EmptySensorEventListener implements SensorEventListener{

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}

			@Override
			public void onSensorChanged(SensorEvent event) {
				System.out.println("Time: " + System.currentTimeMillis() + " Value: " + calcMagnitude(event.values));
			}
			
		}
		
		mSensorManager.registerListener(new EmptySensorEventListener(), mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		
		Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		
		ViewGroup linearyLayout = (ViewGroup) findViewById(R.id.layoutID);
		Button bt1 = new Button(this);
		bt1.setText("Change activity");
		bt1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		bt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isMoving){
//					System.out.println("new activity: inPlace");
					isMoving = false;
				}else{
//					System.out.println("new activity: Moving");
					isMoving = true;
				}
				
			}
		});
		linearyLayout.addView(bt1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Double calcMagnitude(float[] values) {
		double magnitude = 0;
		for(int i=0; i<3; i++)
			magnitude += Math.pow(values[i], 2);
		return magnitude;
	}

}
