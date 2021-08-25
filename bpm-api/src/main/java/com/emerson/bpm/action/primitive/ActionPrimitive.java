package com.emerson.bpm.action.primitive;

public interface ActionPrimitive<T> {

	default public T doPlusAction(T a, T b) { return a; }
	default public T doMinusAction(T a, T b) { return a; }
	default public T doMultiplyAction(T a, T b) { return a; }
	default public T doDivideAction(T a, T b) { return a; }

	public Object doAction(T currVal, T newVal);
 	
}
