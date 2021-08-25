package com.emerson.bpm.model;

import javax.persistence.Entity;

import com.emerson.bpm.model.Budget.KONA;


@Entity
public class Phase {

	String id;
	public double amount;
	
	KONA kona;
	
	public Phase(String id, double amount, KONA kona) {
		this.id = id;
		this.amount = amount;
		this.kona = kona;
	}

}
