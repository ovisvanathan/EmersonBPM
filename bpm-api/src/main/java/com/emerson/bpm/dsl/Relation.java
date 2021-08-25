package com.emerson.bpm.dsl;

public class Relation implements REL {
	
	String relationName;
	
	int numParams;
	
	boolean isAssociative;
	
	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	String expr;
	
	Relation parent;

	Relation child;
	
	public Relation(String relName) {
		this.relationName = relName;
		this.numParams = 1;
		this.expr = "";
		this.isAssociative = false;
	}
	
	public Relation(String relName, int num, String expr, boolean b) {
		this.relationName = relName;
		this.numParams = num;
		this.expr = expr;
		this.isAssociative = b;
	}
	
	public Relation(String relName, Relation ...rel) {
		this.relationName = relName;
		Relation tmp = this;
		for(Relation r : rel) {
			tmp.parent = r;
			tmp.parent.child = tmp;			
			tmp = r;
		}
	}

	public Relation getParent() {
		return parent;
	}

	public void setParent(Relation parent) {
		this.parent = parent;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public int getNumParams() {
		return numParams;
	}

	public void setNumParams(int numParams) {
		this.numParams = numParams;
	}

	public boolean isAssociative() {
		return isAssociative;
	}

	public void setAssociative(boolean isAssociative) {
		this.isAssociative = isAssociative;
	}

	public boolean equals(Object other) {
		
		if(!(other instanceof Relation))
			return false;
		
		Relation orel = (Relation) other;
		
		if(this.relationName.equals(orel.relationName))
			return true;
		
		return false;		
	}

	@Override
	public REL getChild() {
		return child;
	}
	
}
