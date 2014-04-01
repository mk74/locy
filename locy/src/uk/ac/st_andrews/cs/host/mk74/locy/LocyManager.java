package uk.ac.st_andrews.cs.host.mk74.locy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;

//wrapper for LocyNavigator
//acts as LocationManager
public class LocyManager{
	private static final String GPS_PROVIDER = "gps";
	private static final int REQUEST_TIME = 1000;
	
	private LocyNavigator locyNavigator;
	private List<LocationListener> locationListeners;
	private Thread locyNavigatorThread;
	
	public LocyManager(Context context) {
		locationListeners = new CopyOnWriteArrayList<LocationListener>(); //Concurrency issues solved
		locyNavigator = new LocyNavigator(context);
		locyNavigatorThread = new Thread(new Runnable() {
			
			//every second, goes through listeners and broadcast location
			public void run(){
				try {
					System.out.println("LocyNavigatorThread has started");
					while(true){
						if(locyNavigator.getLocation()!=null){
							for(LocationListener listener: locationListeners){
								listener.onLocationChanged(locyNavigator.getLocation());
							}
						}
						Thread.sleep(REQUEST_TIME);
					}
				} catch (InterruptedException e) {
					System.out.println("LocyNavigatorThread has stopped");
				}
	        }
		});
	}
	
	//checks whether provider is enabled
	public boolean isProviderEnabled(String provider){
		if(provider.equals(GPS_PROVIDER))
			return true;
		else
			return false;
	}
	
	//register listener for location updates
	public void requestLocationUpdates(String provider, long minTime, 
										float minDistance, LocationListener listener){
		if(provider == GPS_PROVIDER){
			locationListeners.add(listener);
			if(locationListeners.size() == 1){
				locyNavigator.start();
				if ( locyNavigatorThread.getState() == Thread.State.NEW )
					locyNavigatorThread.start();
			}
		}
	}
	
	//remove listener for location updates
	public void removeUpdates(LocationListener listener){
		locationListeners.remove(listener);
		if(locationListeners.size() == 0){
			locyNavigatorThread.interrupt();
			locyNavigator.stop();
		}
	}
	
	//returns last known location for given provider
	public Location getLastKnownLocation(String provider){
		if(provider.equals(GPS_PROVIDER)){
			return locyNavigator.getLocation();
		}else
			return null;

	}
	
	//returns all available providers
	public List<String> getProviders(Criteria criteria, boolean enabledOnly){
		List<String> providers = new ArrayList<String>();
		providers.add(GPS_PROVIDER);
		return providers;
	}

}
