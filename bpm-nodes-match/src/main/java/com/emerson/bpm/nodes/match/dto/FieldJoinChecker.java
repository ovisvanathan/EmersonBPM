package com.emerson.bpm.nodes.match.dto;

import com.emerson.bpm.dsl.FieldProvider;

public class FieldJoinChecker  implements FieldProvider {
	
	String [] names;
	
	Object [] vals;
		
	Class [] argTypes = new Class[2];
	
	Object [] args = new Object[2];
	
	public FieldJoinChecker(String [] names, Object [] vals) {
		this.names = names;
		this.vals = vals;
		
		argTypes[0] = String [].class;
		argTypes[1] = Object [].class;
	
		args[0] = names;
		args[1] = vals;
		
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
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
