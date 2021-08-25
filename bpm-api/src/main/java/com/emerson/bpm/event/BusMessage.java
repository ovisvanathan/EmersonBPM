package com.emerson.bpm.event;

public class BusMessage {
	
	String evtName;
	Object data;
	
	public BusMessage(String evtName, Object arg0) {
		this.evtName = evtName;
		this.data = arg0;
	}

	public String getName() {

		return this.evtName;
	}

	public Object getData() {

		return this.data;
	}

}
