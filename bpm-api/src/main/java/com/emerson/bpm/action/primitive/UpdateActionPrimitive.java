package com.emerson.bpm.action.primitive;

import com.emerson.bpm.action.RulesAction.ActionType;

public class UpdateActionPrimitive implements ActionPrimitive {
	
	ActionType actionType;
	
	public UpdateActionPrimitive(ActionType actionType) {
		this.actionType = actionType;
	}

	@Override
	public Object doAction(Object currVal, Object newVal) {

		switch(this.actionType) {
		
		
		case ADD:			
			return doPlusAction(currVal, newVal);		
		case MINUS:			
			return doMinusAction(currVal, newVal);		
		case MULTIPLY:			
			return doMultiplyAction(currVal, newVal);		
		case DIVIDE:			
			return doDivideAction(currVal, newVal);		
		
		}		
		return doPlusAction(currVal, newVal);		
	}

	public Integer doPlusAction(Integer a, Integer b) {
		return a + b;
	}
	
	public Integer doMinusAction(Integer a, Integer b) {
		return a - b;
	}

	public Integer doMultiplyAction(Integer a, Integer b) {
		return a * b;
	}
	
	public Integer doDivideAction(Integer a, Integer b) {
		return a / b;
	}
	

	public long doPlusAction(long a, long b) {
		return a + b;
	}
	
	public long doMinusAction(long a, long b) {
		return a - b;
	}

	public long doMultiplyAction(long a, long b) {
		return a * b;
	}
	
	public long doDivideAction(long a, long b) {
		return a / b;
	}

	
	public short doPlusAction(short a, short b) {
		return (short) (a + b);
	}
	
	public short doMinusAction(short a, short b) {
		return (short) (a - b);
	}

	public short doMultiplyAction(short a, short b) {
		return (short) (a * b);
	}
	
	public short doDivideAction(short a, short b) {
		return (short) (a / b);
	}

	
	public double doPlusAction(double a, double b) {
		return a + b;
	}
	
	public double doMinusAction(double a, double b) {
		return a - b;
	}

	public double doMultiplyAction(double a, double b) {
		return a * b;
	}
	
	public String doPlusAction(String a, String b) {
		return a + b;
	}


	
	
}
