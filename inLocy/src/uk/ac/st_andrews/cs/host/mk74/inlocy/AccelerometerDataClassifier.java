package uk.ac.st_andrews.cs.host.mk74.inlocy;

import java.util.ArrayList;

public class AccelerometerDataClassifier {
	private static final double MOVEMENT_THRESHOLD = 25;
	private static final int WINDOW_SIZE = 1000;
	
	private boolean moving=true;
	ArrayList<Double> magnitudes = new ArrayList<Double>();

	public boolean add(float[] values) {
		//todo race condtion
		magnitudes.add(calcMagnitude(values));
		if(magnitudes.size() == WINDOW_SIZE){
			System.out.println("FULL WINDOW!");
			classify();
			magnitudes.clear();
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
		if(standard_deviation > MOVEMENT_THRESHOLD)
			moving = true;
		else
			moving = false;
		
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
	
}
