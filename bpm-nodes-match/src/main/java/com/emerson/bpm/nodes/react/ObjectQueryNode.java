package com.emerson.bpm.nodes.react;

public class ObjectQueryNode extends ObjectTypeNode {

	String queryName;
	
	Class [] queryClasses;
	
	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public Class[] getQueryClasses() {
		return queryClasses;
	}

	public void setQueryClasses(Class[] queryClasses) {
		this.queryClasses = queryClasses;
	}

	public ObjectQueryNode(String queryName2, Class [] queryClasses) {
		super(queryClasses[0]);
		this.queryName = queryName2;
		this.queryClasses = queryClasses;
	}
	
}
