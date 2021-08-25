package com.emerson.bpm.nodes.rel;

import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.functor.Predicate2;
import com.emerson.bpm.functor.el.ExpressionPredicate;

public class InferenceSequenceSet  extends ExpressionSequenceSet  {

	protected Predicate2 predicate2;
	
	public InferenceSequenceSet(SDKNode node) {
		super(node);
	}

	public InferenceSequenceSet(SDKNode node, REL relation) {
		super(node, relation);
		
		buildSequenceSet();
	}

	private void buildSequenceSet() {

		REL rel = getRelation();
		boolean isAssoc = rel.isAssociative();
		
		this.predicate2 = new ExpressionPredicate(rel, this);
	
		this.predicate2.getEvaluator();
	}


}
