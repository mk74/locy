package uk.ac.st_andrews.cs.host.mk74.sampleapps.app1;


import uk.ac.st_andrews.cs.host.mk74.locy.LocyEvent;
import uk.ac.st_andrews.cs.host.mk74.locy.LocyEventListener;
import uk.ac.st_andrews.cs.host.mk74.locy.LocyManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LocyManager locyManager = new LocyManager(this);
		class PrintEventListener implements LocyEventListener{

			@Override
			public void onLocyChanged(LocyEvent event) {				
				String output = event.getValue();
				TextView tv = (TextView) findViewById(R.id.textView1);
				tv.setText(output);				
			}
			
		}
		locyManager.registerListener(new PrintEventListener());
		
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
