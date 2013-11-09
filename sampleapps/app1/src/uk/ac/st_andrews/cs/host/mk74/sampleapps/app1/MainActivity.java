package uk.ac.st_andrews.cs.host.mk74.sampleapps.app1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import uk.ac.st_andrews.cs.host.mk74.locy.LocyManager;
import uk.ac.st_andrews.cs.host.mk74.locy.LocyEventListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LocyManager locyManager = new LocyManager(this);
		class MyLocyEventListener implements LocyEventListener{
			
		}
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
