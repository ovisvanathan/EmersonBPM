package com.emerson.bpm.model;

public class LoanProduct {

	public LoanProduct(String string, String string2, int i) {
		this.name = string;
		this.loanType = string2;
		this.maxAmount = i;
	}

	String loanType;
	
	double maxAmount;

	public String name;

}
