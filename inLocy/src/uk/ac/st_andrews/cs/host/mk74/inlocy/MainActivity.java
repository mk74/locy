package uk.ac.st_andrews.cs.host.mk74.inlocy;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static boolean EXPERIMENT_ON = true;
	public static double SCREEN_UDPATE_FREQUENCY = 3.0; // 3.0 - experiment, 0.5 - debugging

	BatteryEvaluator batteryEvaluator;
	InLocyNavigator inLocyNavigator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set up components: WiFiNavigator and BatteryEvaluator
		
		inLocyNavigator = new InLocyNavigator(getApplicationContext());
		batteryEvaluator = new BatteryEvaluator(getApplicationContext());
		inLocyNavigator.start();
		batteryEvaluator.enable();
		
		//prepare screen/layout
		Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);
		
		//print(screen & logcat) state of Navigator:
		//current localization, whether WiFiNavigator is enabled and battery info(level + time difference)
		TimerTask readValues = new TimerTask() {
			@Override
			public void run() {
				final String output = inLocyNavigator.getInfo() 
						  + batteryEvaluator.getInfo();
				System.out.println(output);
				
				if(EXPERIMENT_ON){
					//only update screen if the experiment is over
					if(batteryEvaluator.getTimeDifference()!=-1){
						runOnUiThread(new Runnable(){
	                    	@Override
	                    	public void run() {
	                    		TextView tv = (TextView) findViewById(R.id.textView1);
	                    		tv.setText(output);
	                    	}
						});
					}
				}else{
					runOnUiThread(new Runnable(){
                    	@Override
                    	public void run() {
                    		TextView tv = (TextView) findViewById(R.id.textView1);
                    		tv.setText(output);
                    	}
					});
				};
				
				
			}
		};
		Timer timer = new Timer();
		long delay = (long) (SCREEN_UDPATE_FREQUENCY * 1000);
		long period = (long) (SCREEN_UDPATE_FREQUENCY * 1000);
		timer.schedule(readValues, delay, period);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}