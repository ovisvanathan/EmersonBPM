package com.emerson.bpm.model;

public class Flight {

	public enum CustType {
		REGULAR,
		BUSINESS
	};
	
	String flightNo, origin, dest, depTime, arrTime;
	
	double price, firstClassPrice;
	
	int totalSeats, numAvailable;
	
	CustType type;
	
	int numMiles;
	
	public Flight() {

	}

	
	public int getNumMiles() {
		return numMiles;
	}

	public void setNumMiles(int numMiles) {
		this.numMiles = numMiles;
	}

	public Flight(String flightNo, String origin, String dest, double price, double FirstClassPrice, 
							int totalSeats, int numAvailable, CustType type, int numMiles) {
		this.flightNo = flightNo;
		this.origin = origin;
		this.dest = dest;
		this.price = price;
		this.firstClassPrice = firstClassPrice;	
		this.totalSeats = totalSeats;
		this.numAvailable = numAvailable; 
		
		this.numMiles = numMiles;
		this.type = type;
	}

}
