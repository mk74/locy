package uk.ac.st_andrews.cs.host.mk74.locy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class LocyNavigatorService extends Service {
	private static final String TAG = LocyNavigatorService.class.getSimpleName();
	private LocyNavigator locyNavigator;

	public LocyNavigatorService() {
		
	}
	
	private void createLocyNavigator() {
		locyNavigator = new LocyNavigator(this);
	}

	private LocyNavigatorApi.Stub apiEndpoint = new LocyNavigatorApi.Stub() {
		
		@Override
		public void stop() throws RemoteException {
			locyNavigator.stop();
		}
		
		@Override
		public void start() throws RemoteException {
			createLocyNavigator(); //XXX
			locyNavigator.start();
		}
		
		@Override
		public double[] getLocation() throws RemoteException {
			return locyNavigator.getLocation();
		}

		@Override
		public String getInfo() throws RemoteException {
			return locyNavigator.getInfo();
		}

		@Override
		public String getDebuggerInfo() throws RemoteException {
			return locyNavigator.getDebuggerInfo();
		}
	};
			
	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
//		throw new UnsupportedOperationException("Not yet implemented");
		if (LocyNavigatorService.class.getName().equals(intent.getAction())) {
		    Log.d(TAG, "Bound by intent " + intent);
		    return apiEndpoint;
		} else {
		    return null;
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service creating");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");
	}
}
