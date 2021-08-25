package com.emerson.bpm.model;

public class Miles {
	
	String custName;
	int miles;
	
	int numTrips;
	
	public int getNumTrips() {
		return numTrips;
	}

	public void setNumTrips(int numTrips) {
		this.numTrips = numTrips;
	}

	public Miles(String name, int miles) {
		this.custName = name;
		this.miles = miles;
	}

}
