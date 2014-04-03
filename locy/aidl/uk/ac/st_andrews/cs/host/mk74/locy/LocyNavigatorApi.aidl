package uk.ac.st_andrews.cs.host.mk74.locy;

interface LocyNavigatorApi {
	void start();
	
	void stop();
	
	double[] getLocation();
	
	String getInfo();
	
	String getDebuggerInfo();
}