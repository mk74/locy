package uk.ac.st_andrews.cs.host.mk74.energymeasurement.cam;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements Callback {

	private static final String LOG = "CamPreview";
	SurfaceHolder mHolder;
	Camera mCamera;
	
	public CameraPreview(Context context) {
	    super(context);
	}
	public CameraPreview(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
	
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		System.out.println("CameraPreview()");
		
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		//don't need it now
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("SurfaceCreated");
		// The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(LOG, "Error setting camera preview: " + e.getMessage());
        }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}
}
