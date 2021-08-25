package com.emerson.bpm.functor;

public class EvenOddPredicate<T> implements Predicate2<T> {

	int data;

	public EvenOddPredicate(int arg0) {
		this.data = arg0;				
	}
	
	public boolean evaluate(final T arg0) {		 
		return data % 2 == 0;		 
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