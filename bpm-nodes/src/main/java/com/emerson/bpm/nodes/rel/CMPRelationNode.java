package com.emerson.bpm.nodes.rel;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.dsl.CMPRelation;
import com.emerson.bpm.dsl.REL;

/*
 * A Relation node that uses a comparator
 */
public class CMPRelationNode extends RelationNode implements CMPRelation {

	public CMPRelationNode(REL rel, AutonomousActor a, AutonomousActor b) {
		super(rel, a, b);		
	}

	public CMPRelationNode(REL rel, AutonomousActor a) {
		super(rel, a, null);		
	}
	
	
	

}
