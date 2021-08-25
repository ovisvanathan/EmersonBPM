package com.emerson.bpm.model;

import java.util.List;

public class Bank {
	
	public String name;
	
	String code;
	
	String mainBranchCode;

	public List<String> products;
	
	public Bank() {

	}

	
	public Bank(String string) {
		this.name = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMainBranchCode() {
		return mainBranchCode;
	}

	public void setMainBranchCode(String code) {
		this.mainBranchCode = code;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}
	
	

}
