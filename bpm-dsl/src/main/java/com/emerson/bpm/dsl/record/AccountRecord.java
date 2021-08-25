package com.emerson.bpm.dsl.record;

public class AccountRecord extends AbstractDSLRecord {

	Object data;
	
	public Class getRequestType() { return this.data.getClass(); }

	public AccountRecord(Object ... data) {
		this.data = data;		
	}

	

	
	@Override
	public int getColumnCount() {
		return 0;
	}
	
	
}
