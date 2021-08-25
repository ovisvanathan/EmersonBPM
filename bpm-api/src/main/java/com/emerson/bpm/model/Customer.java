package com.emerson.bpm.model;

public class Customer {
	
	String name;
	int age;
	
	int miles;
	
	public Customer() {

	}

	public int getMiles() {
		return miles;
	}

	public void setMiles(int miles) {
		this.miles = miles;
	}

	public Customer(String name, int age) {
		this.name = name;
		this.age = age;
	}

		
}
