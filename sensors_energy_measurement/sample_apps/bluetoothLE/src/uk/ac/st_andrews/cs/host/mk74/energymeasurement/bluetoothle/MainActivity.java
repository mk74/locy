package uk.ac.st_andrews.cs.host.mk74.energymeasurement.bluetoothle;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	
	final private static int REQUEST_ENABLE_BT = 1;
	final private static int BLE_SCAN_PERIOD = 10;
	
	public Date batteryLevelStartTime, batteryLevelEndTime;
	public int amountDevices = 0;
	public int bleScanTime=10;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Bluetooth Setup
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		    while(!mBluetoothAdapter.isEnabled()){
		    	//wait till bluetooth is enabled
		    }
		    System.out.println("Bluetooth enabled");
		}
		final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		    @Override
		    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
		        runOnUiThread(new Runnable() {
		           @Override
		           public void run() {
		        	   amountDevices++;
		           }
		       });
		   }
		};
		
		//keep scheduling new scan, print results every sec and check battery life whether the given range was depleted 
		TimerTask scanDevicesTask = new TimerTask() {
			
			@Override
			public void run() {	
				//schedule new BLE scanning
				if(bleScanTime==BLE_SCAN_PERIOD){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							mBluetoothAdapter.stopLeScan(mLeScanCallback);
							amountDevices = 0;
							System.out.println("About to start new scanning");
							mBluetoothAdapter.startLeScan(mLeScanCallback);
							System.out.println("Started new scanning");
							bleScanTime = 1;
						}
					});
				}else
					bleScanTime++;
				
				//print available devices:
				System.out.println("Values:" + amountDevices);
				
				//battery measurements && time which was needed to deplete the battery range
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
		timer.schedule(scanDevicesTask, delay, period);

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
