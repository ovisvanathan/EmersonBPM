package com.emerson.bpm.nodes.match.dto;

import com.emerson.bpm.dsl.FieldProvider;

public class FieldNameChecker  implements FieldProvider {
	
	String name1;
	
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

	String name2;
	
	Object [] args = new Object[2];
	
	public FieldNameChecker(String name1, String name2) {
		this.name1 = name1;
		this.name2 = name2;
	
		args[0] = name1;
		args[1] = name2;
		
	}
	
}
