package com.emerson.bpm.dsl.util;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.functor.Predicate2;

public class DSLPredicate implements Predicate2 {

	String queryName;
	
	Class[] queryClasses;
	
	private ClauseComparator clauseComparator;
	
	public DSLPredicate(String queryName) {
		this.queryName = queryName;
	}
	
	public DSLPredicate(String queryName, Class[] expr2) {
		this.queryName = queryName;
		this.queryClasses = expr2;
	}

	@Override
	public boolean evaluate(Object param) {
		try {
			return this.clauseComparator.evaluate(param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean evaluateChain(Object... seq) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
	
	
}
