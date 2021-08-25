package com.emerson.bpm.model;

import java.util.List;

public class Account {

	String acnum;
	
	String type;
	
	double amount;
	
	List<Txn> txns;

	String acstatus;
	
	public String custName;

	public String bankName;

	public String branchCode;

	public Account(String bankName, String branchCode, String acnum, String custName, double d) {
		this.bankName = bankName;
		this.branchCode = branchCode;
		this.acnum = acnum;
		this.custName = custName;
		this.amount = d;
	}

	public Account() {

	}
	
	public String getAcstatus() {
		return acstatus;
	}

	public void setAcstatus(String acstatus) {
		this.acstatus = acstatus;
	}

	public List<Account> getAccountHistory() {
		return accountHistory;
	}

	public void setAccountHistory(List<Account> accountHistory) {
		this.accountHistory = accountHistory;
	}

	List<Account> accountHistory;
	
	public String getAcnum() {
		return acnum;
	}

	public void setAcnum(String acnum) {
		this.acnum = acnum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public List<Txn> getTxns() {
		return txns;
	}

	public void setTxns(List<Txn> txns) {
		this.txns = txns;
	}

	public String toString() {
		return this.bankName + ", " +  this.acnum;
	}
	
}
