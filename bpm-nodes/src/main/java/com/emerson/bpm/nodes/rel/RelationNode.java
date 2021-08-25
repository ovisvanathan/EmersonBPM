package com.emerson.bpm.nodes.rel;

import java.util.Arrays;
import java.util.List;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.dsl.REL;

public class RelationNode implements REL {

	REL relation;
	
	String name;
		
	public REL getRelation() {
		return relation;
	}

	public void setRelation(REL relation) {
		this.relation = relation;
	}

	public List getParams() {
		return params;
	}

	public void setParams(List params) {
		this.params = params;
	}

	List params;
	
	public RelationNode(String relName, AutonomousActor c, AutonomousActor b) {
		// TODO Auto-generated constructor stub
	}

	public RelationNode(REL rel, AutonomousActor c, AutonomousActor b) {
		this.relation = rel;
		this.params = Arrays.asList(c, b);
	}

	public RelationNode(REL rel, AutonomousActor ...c) {
		this.relation = rel;
		this.params = Arrays.asList(c);
	}

	public RelationNode(String relName, String ...vals) {
		this.name = name;					
		this.params.addAll(Arrays.asList(vals));		
	}

	@Override
	public REL getChild() {
		// TODO Auto-generated method stub
		return null;
	}	

}
