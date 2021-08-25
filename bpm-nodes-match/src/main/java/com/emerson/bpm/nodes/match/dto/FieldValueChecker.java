package com.emerson.bpm.nodes.match.dto;

import java.util.Comparator;

import com.emerson.bpm.dsl.FieldProvider;

public class FieldValueChecker  implements FieldProvider {
	
	String name1;
	
	Object val1;
	
	Comparator comp;
	
	Object [] args = new Object[3];
	
	public FieldValueChecker(String name1, Object val1, Comparator comp) {
		this.name1 = name1;
		this.val1 = val1;
	
		this.comp = comp;
		
		args[0] = name1;
		args[1] = val1;
		args[2] = comp;
		
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public Object getVal1() {
		return val1;
	}

	public void setVal1(Object val1) {
		this.val1 = val1;
	}

	public Comparator getComp() {
		return comp;
	}

	public void setComp(Comparator comp) {
		this.comp = comp;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
}
