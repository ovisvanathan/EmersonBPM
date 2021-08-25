package com.emerson.bpm.nodes.match.dto;

import java.util.Comparator;

import com.emerson.bpm.dsl.FieldProvider;

public class DNE  implements FieldProvider {
	
	String name1;

	String name2;
	
	
	Class [] argTypes = new Class[2];
	
	Object [] args = new Object[2];
	
	
	
	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public DNE(String name1, String name2) {
		this.name1 = name1;
		this.name2 = name2;
	
		
		argTypes[0] = String.class;
		argTypes[1] = String.class;
		
		args[0] = name1;
		args[1] = name2;
		
	}

	public Class[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(Class[] argTypes) {
		this.argTypes = argTypes;
	}
	
}
