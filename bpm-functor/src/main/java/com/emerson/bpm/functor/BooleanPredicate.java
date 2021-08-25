package com.emerson.bpm.functor;

public class BooleanPredicate<T> implements Predicate2<T> {

	boolean data;
	
	public BooleanPredicate(boolean arg0) {
		this.data = arg0;				
	}
	
	public boolean evaluate(final T arg0) {	 
		return data;		 
	 }

	@Override
	public boolean evaluate(T... object) {
		return false;
	}

	@Override
	public boolean evaluateChain(T ... seq) {

		return false;
	}


}