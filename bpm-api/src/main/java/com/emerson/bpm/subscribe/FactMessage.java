package com.emerson.bpm.subscribe;

public class FactMessage  implements ReteMessage {
	
	Object person;
	
	public FactMessage(Object person) {
		this.person = person;
	}

}
