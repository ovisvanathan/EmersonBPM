package com.emerson.bpm.dsl.record;

public class CustomerRecord extends AbstractDSLRecord {

	Object data;
	
	public Class getRequestType() { return this.data.getClass(); }

	public CustomerRecord(Object ... data) {
		this.data = data;		
	}
	
	@Override
	public int getColumnCount() {
		return 0;
	}
	
	
}
