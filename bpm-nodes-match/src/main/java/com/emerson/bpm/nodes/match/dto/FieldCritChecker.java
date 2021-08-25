package com.emerson.bpm.nodes.match.dto;

import com.emerson.bpm.dsl.FieldProvider;

public class FieldCritChecker  implements FieldProvider {
		
	Object [] vals;
		
	Class [] argTypes = new Class[1];
	
	Object [] args = new Object[1];
	
	public FieldCritChecker(Object [] vals) {
		this.vals = vals;
		
		argTypes[0] = Object [].class;
	
		args[0] = vals;
		
	}

	public Object[] getVals() {
		return vals;
	}

	public void setVals(Object[] vals) {
		this.vals = vals;
	}

	public Class[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(Class[] argTypes) {
		this.argTypes = argTypes;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	
	
}
