package uk.ac.st_andrews.cs.host.mk74.sensorsgraphplotter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class MainActivity extends Activity {

	
	private static final double GRAPH_DRAW_FREQUENCY = 0.5;
	public static double GRAVITY_G = 9.81;
	public static int WINDOW_SIZE = 5000;
	private XYPlot plot;
	private CopyOnWriteArrayList<Number> data = new CopyOnWriteArrayList<Number>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_xy_plot_example);
		
//		System.out.println("App started");
		//stylling
//        final LineAndPointFormatter series1Format = new LineAndPointFormatter();
//        series1Format.setPointLabelFormatter(new PointLabelFormatter());
//        series1Format.configure(getApplicationContext(),
//                R.layout.line_point_formatter_with_plf1);
		
		//register listener for accelerometer data, calculate total accelereation magnitude and save it
		SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		class EmptySensorEventListener implements SensorEventListener{
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {}
				@Override
				public void onSensorChanged(SensorEvent event) {
					double newPoint=calcTotalAccelerationMagnitude(event.values);
					data.add(newPoint);
					if(data.size()>=WINDOW_SIZE){
//						System.out.println("App reached Window size");
						data.remove(0);
					}
				}
					
		}
		int rate = 0;
		mSensorManager.registerListener(new EmptySensorEventListener(), mAccelerometer, rate);
				
		// initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
//        plot.getGraphWidget().setDrawMarkersEnabled(false);
//        plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.TRANSPARENT);
//        plot.getGraphWidget().getGridDomainLinePaint().setColor(Color.TRANSPARENT);
//        plot.getGraphWidget().getRangeGridLinePaint().setColor(Color.TRANSPARENT);
//        System.out.println(plot.getGraphWidget().isDrawMarkersEnabled());
 
        // update graph every half sec
      	TimerTask readValues = new TimerTask() {
      		@Override
      		public void run() {
      			plot.clear();
				XYSeries series1 = new SimpleXYSeries(
		           data,          
		           SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
		           "Accelerometer"); 
				plot.addSeries(series1, new LineAndPointFormatter(
					    Color.rgb(0, 0, 200),
					    Color.rgb(0, 0, 100),
					    null,
					    (PointLabelFormatter) null));
		        plot.redraw();
      		}
      	};
      	Timer timer = new Timer();
      	long delay = (long) (GRAPH_DRAW_FREQUENCY * 1000);
      	long period = (long) (GRAPH_DRAW_FREQUENCY * 1000);
      	timer.schedule(readValues, delay, period);
       
      	Window w = getWindow();
		w.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private double calcTotalAccelerationMagnitude(float[] values) {
		double result = 0;
		result += Math.pow(values[0], 2);
		result += Math.pow(values[1], 2);
		result += Math.pow(values[2], 2);
		result = Math.sqrt(result);
		result -=GRAVITY_G;
		return result;
	}

}
