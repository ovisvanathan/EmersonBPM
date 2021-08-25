package com.emerson.bpm.dsl;

import java.util.List;

import com.emerson.bpm.api.AutonomousActor;

public interface REL {

	default public String getName() {
		return null;
	}

	default public REL getRelation() {
		return null;
	}

	default public void setRelation(Relation relation) {
	}

	default public List<AutonomousActor> getParams() {
		return null;
	}

	default public void setParams(List<AutonomousActor> params) {
	}
	
	default public void setParam(Object param) {

	}


	default public int getNumParams() { return 0; }

	default public void setNumParams(int n) { }

	default public void setName(String name) {}
	
	default public boolean isAssociative() { return true; }

	default public void setAssociative(boolean associative) {}

	default public String getExpr() { return null; }

	default public REL getParent() { return null; }

	public REL getChild();

}
