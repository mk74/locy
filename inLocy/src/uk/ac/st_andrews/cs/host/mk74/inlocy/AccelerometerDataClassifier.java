package uk.ac.st_andrews.cs.host.mk74.inlocy;

import java.util.ArrayList;

public class AccelerometerDataClassifier {
	private static final double MOVEMENT_THRESHOLD = 15.0;
	private static final int WINDOW_SIZE = 550;
	private static final int WINDOW_N = 3; 
	
	private int predictedMoving = 0;
	private int leftWindowN = WINDOW_N;
	
	private double lastStandardDeviation = 0.0; //only for debugging(printing values in real time)
	
	private boolean moving=true;
	ArrayList<Double> magnitudes = new ArrayList<Double>();

	public boolean add(float[] values) {
		//todo race condtion
		magnitudes.add(calcMagnitude(values));
		if(magnitudes.size() == WINDOW_SIZE){
			System.out.println("FULL WINDOW");
			classify();
			magnitudes.clear();
			
			leftWindowN--;
			if(leftWindowN == 0){
				if(predictedMoving>0){
					moving = true;
				}
				if(predictedMoving<0){
					moving = false;
				}
				predictedMoving = 0;
				leftWindowN = WINDOW_N;
			}
		}
		return moving;
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
		System.out.println("Standard deviation: " + standard_deviation);
		
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
