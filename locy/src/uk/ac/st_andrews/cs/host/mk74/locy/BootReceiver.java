package uk.ac.st_andrews.cs.host.mk74.locy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("BootReceiver", "about to start Service");
		LocyNavigator locyNavigator = new LocyNavigator(context.getApplicationContext());
		Intent serviceIntent = new Intent(LocyNavigatorService.class.getName());
        context.startService(serviceIntent); 
	}

}
