package com.emerson.bpm.model;

public class Ticket {
	
	String custName;
	
	String flightNum;
	
	public String getFlightNum() {
		return flightNum;
	}

	public Ticket() {

	}


	public void setFlightNum(String flightNum) {
		this.flightNum = flightNum;
	}


	public Ticket(String name) {
		this.custName = name;
	}
	
}
