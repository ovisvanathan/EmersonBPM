package com.emerson.bpm.model;

public class Branch {

	String bankName;
	
	String branchCode;
	
	String branchName;
	
	public Branch() {

	}


	public Branch(String string, String string2, String string3) {
		this.bankName = string;
		this.branchCode = string2;
		this.branchName = string3;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	
	
}
