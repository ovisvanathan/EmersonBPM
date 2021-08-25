package com.emerson.bpm.nodes.match;

public class TxnCrit implements CriteriaComparator {

	public TxnCrit() {
		
	}
	
	@Override
	public boolean evaluate(Object... args) {
		System.out.println("txncrit eval");
		return false;
	}

	@Override
	public boolean isTemporal() {
		// TODO Auto-generated method stub
		return false;
	}
	
}