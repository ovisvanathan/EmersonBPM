package com.emerson.bpm.event;

public class StateChangeEvent {

	Object oldval;
	
	Object newval;
	
	public StateChangeEvent(Object oldval, Object newval) {
		this.oldval = oldval;
		this.newval = newval;
	}

	public Object getOldval() {
		return oldval;
	}

	public Object getNewval() {
		return newval;
	}
}
