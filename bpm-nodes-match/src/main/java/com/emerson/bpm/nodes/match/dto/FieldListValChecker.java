package com.emerson.bpm.nodes.match.dto;

import com.emerson.bpm.dsl.FieldProvider;

public class FieldListValChecker  implements FieldProvider {
	
	String name1;
	
	Object val1;

	Class [] argTypes = new Class[2];

	Object [] args = new Object[2];
	
	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object getVal1() {
		return val1;
	}

	public void setVal1(Object val1) {
		this.val1 = val1;
	}

	public Class[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(Class[] argTypes) {
		this.argTypes = argTypes;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	String name2;
	

	public FieldListValChecker(String name1, Object val1) {
		this.name1 = name1;
		this.val1 = val1;
	
		argTypes[0] =  String.class;
		argTypes[1] =  Object.class;
		
		args[0] = name1;
		args[1] = name2;
		
	}
	
}
