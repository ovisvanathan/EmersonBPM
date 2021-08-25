package com.emerson.bpm.model;

import java.util.Date;

public class LoanApplication {

	String acnum;
	
	public String custName;
	
	String loanType;
	
	double amount;
	
	Date applicationDate;
	
	LoanStatus status;
	
	public String bankName;
	
	public String brcode;
	
	public LoanApplication(String loanType, String bankName, String brcode, String custName, String acnum, int i) {
		this.loanType = loanType;
		this.bankName = bankName;
		this.brcode = brcode;
		this.custName = custName;
		this.acnum = acnum;
		this.amount = i;
	}

	public String getAcnum() {
		return acnum;
	}

	public void setAcnum(String acnum) {
		this.acnum = acnum;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}
	
	
}
