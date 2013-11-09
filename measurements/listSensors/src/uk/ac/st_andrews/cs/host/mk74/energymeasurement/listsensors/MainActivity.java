package uk.ac.st_andrews.cs.host.mk74.energymeasurement.listsensors;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		
		setContentView(R.layout.activity_main);
		
		Iterator<Sensor> it = deviceSensors.iterator();
		String listSensors = "";
		while(it.hasNext()){
			Sensor s = it.next();
			listSensors += s.getType() + " " +s.getName() + "\n";
		}
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(listSensors);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
