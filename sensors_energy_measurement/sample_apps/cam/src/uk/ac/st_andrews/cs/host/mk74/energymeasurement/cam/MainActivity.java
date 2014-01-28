package uk.ac.st_andrews.cs.host.mk74.energymeasurement.cam;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	final private static int BATTERY_LEVEL_START=99;
	final private static int BATTERY_LEVEL_END=98;
	final private static int BATTERY_CHECK_FREQUENCY = 1;
	
	public Date batteryLevelStartTime, batteryLevelEndTime;
	
	Camera mCamera;
	CameraPreview mPreview;
	private MediaRecorder mMediaRecorder;
	private static final String TAG = "MainActivity";
	private static final String CAM_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/measurement_cam.mp4";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_main);

		//clear last run, set up camera, its preview(with "smallest" size) and start recording within half a sec
		File myFile = new File(CAM_FILE_PATH);
		if(myFile.exists())
		    myFile.delete();
		mCamera = getCameraInstance();
		Camera.Parameters params = mCamera.getParameters();
		List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
		Camera.Size previewSize = previewSizes.get(previewSizes.size()-1);
 	    params.setPreviewSize(previewSize.width, previewSize.height);
 	    mCamera.setParameters(params);
		mPreview = new CameraPreview(getApplicationContext(), mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		runOnUiThread(new Runnable() {
            @Override
            public void run() {
                 final Handler handler = new Handler();
                 handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
               		 if(prepareVideoRecorder()){
               			System.out.println("about to start recording");
           				mMediaRecorder.start();
               		 }
                   }
                 }, 500);
            }
        });
		
		//every sec, check battery life whether the defined range was depleted  
		TimerTask readValues = new TimerTask() {
			
			@Override
			public void run() {
				String output = "Values: " + new File(CAM_FILE_PATH).length()  +"\n";
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	private boolean prepareVideoRecorder(){
	    mMediaRecorder = new MediaRecorder();

	    // Step 1: Unlock and set camera to MediaRecorder
	    mCamera.unlock();
	    mMediaRecorder.setCamera(mCamera);

	    // Step 2: Set sources without audio one
	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	    // Step 3: Set profile manually
	    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	    mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

	    // Step 4: Set output file
		mMediaRecorder.setOutputFile(CAM_FILE_PATH);

	    // Step 5: Set the preview output
	    mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

	    // Step 6: Prepare configured MediaRecorder
	    try {
	        mMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
	}
}
