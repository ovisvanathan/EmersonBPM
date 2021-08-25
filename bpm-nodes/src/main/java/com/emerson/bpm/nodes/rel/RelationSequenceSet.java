package com.emerson.bpm.nodes.rel;

import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.nodes.match.SequenceSet;

/*
 * A RelationSequenceSet accepts a relation and checks whether the relation holds 
 * true for the given tuple
 */
public class RelationSequenceSet extends InferenceSequenceSet {

	public RelationSequenceSet(SDKNode node, REL relation) {
		super(node, relation);
	}

	
	
}
