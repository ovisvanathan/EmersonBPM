package com.emerson.bpm.action;

import java.util.Map;


public class CreateAction<T> extends RulesAction<T>  {

	Map<String, Class> fields;
	
	String tclassName;
	
	public CreateAction(String tclassName, String pkg, Map fields) {
		this.tclassName = tclassName;
		this.packageName = pkg;
		this.fields = fields;
		
	}

	public CreateAction(Class tclass, String pkg, Map fields) {
		this.klazz = tclass;
		this.packageName = pkg;
		this.fields = fields;
		
	}

	public CreateAction(Class<T> tclass, String name) {
		this.klazz = tclass;
		this.packageName = name;
	}

	public CreateAction(String tclassName, Class<T> class1) {
		this.tclassName = tclassName;
		this.klazz = class1;
	}

	@Override
	public void run() {
		
	}
	
	
}
