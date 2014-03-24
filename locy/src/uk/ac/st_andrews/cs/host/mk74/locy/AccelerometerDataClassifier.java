package uk.ac.st_andrews.cs.host.mk74.locy;

import java.util.ArrayList;

public class AccelerometerDataClassifier {
	private static final double MOVEMENT_THRESHOLD = 15.0;
	public static final int WINDOW_TIME_MILLISECS = 2666;
	public static final int WINDOW_N = 3; 
	
	private int predictedMoving = 0;
	private int leftWindowN = WINDOW_N;
	
	private double lastStandardDeviation = 0.0; //only for debugging(printing values in real time)
	
	private boolean moving=true;
	private long timeWindowStart = 0;
	private ArrayList<Long> timestamps = new ArrayList<Long>();
	private ArrayList<Double> magnitudes = new ArrayList<Double>();

	public void add(float[] values) {
		//add new fixtures
		long currentTime = System.currentTimeMillis();
		if(timeWindowStart==0)
			timeWindowStart = currentTime;
		timestamps.add(currentTime);
		magnitudes.add(calcMagnitude(values));
	}
	
	public boolean recognizeActivity(){
		long windowDurationTime = System.currentTimeMillis() - timeWindowStart;
		
		//check whether window is complete
		if(windowDurationTime >= WINDOW_TIME_MILLISECS){
			//classify current window
			classify();
			
			clearWindow();
			
			//check whether enough windows check to interference
			leftWindowN--;
			if(leftWindowN == 0){
				if(predictedMoving>0){
					moving = true;
				}
				if(predictedMoving<0){
					moving = false;
				}
				clearClassifier();
			}
		}
		return moving;
	}
	
	public boolean isRecognitionOver() {
		return (leftWindowN == WINDOW_N && magnitudes.size() == 0); //n windows to check + just cleared magnitudes 
	}
	
	public void clear() {
		clearWindow();
		clearClassifier();
	}
	
	private void clearWindow() {
		timeWindowStart = 0;
		lastStandardDeviation = 0.0;
		timestamps.clear();
		magnitudes.clear();
	}
	
	private void clearClassifier() {
		predictedMoving = 0;
		leftWindowN = WINDOW_N;
	}

	private Double calcMagnitude(float[] values) {
		double magnitude = 0;
		for(int i=0; i<3; i++)
			magnitude += Math.pow(values[i], 2);
		return magnitude;
	}

	private void classify() {
		double mean = calcMean();
		double standard_deviation = calcStandardDeviation(mean);
		lastStandardDeviation = standard_deviation;
		
		if(standard_deviation > MOVEMENT_THRESHOLD)
			predictedMoving++;
		else
			predictedMoving--;
	}

	private double calcStandardDeviation(double mean) {
		double standard_deviation = 0;
		for(double magnitude:magnitudes){
			standard_deviation += Math.pow(mean - magnitude, 2);
		}
		standard_deviation /= magnitudes.size();
		standard_deviation = Math.sqrt(standard_deviation);
		return standard_deviation;
	}

	private double calcMean() {
		double mean = 0;
		for(double magnitude : magnitudes){
			mean+=magnitude;
		}
		mean /= magnitudes.size();
		return mean;
	}

	public String getInfo() {
		double rounded = (double) Math.round(lastStandardDeviation * 100) / 100;
		return "isMoving: " + moving + " | predictableMoving: " + predictedMoving + 
				" | left window n: " + leftWindowN + " | last stddev: " + rounded + "\n";
	}
	
}
