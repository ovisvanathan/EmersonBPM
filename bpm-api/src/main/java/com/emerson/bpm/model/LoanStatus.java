package com.emerson.bpm.model;

public class LoanStatus {

	public enum loanStatus {
		
		APPLIED,
		UNDER_PROCESSING,
		KYC_VERIFIED,
		APPROVED,
		DECLINED
	};
	
	loanStatus status;

	public loanStatus getStatus() {
		return status;
	}

	public void setStatus(loanStatus status) {
		this.status = status;
	}
	
	
}
