package com.emerson.bpm.nodes.rel;

import com.emerson.bpm.api.Evaluator;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.functor.el.ChainEvaluator;

public class ComparatorSequenceSet  extends InferenceSequenceSet {

	Evaluator eval2;
	
	boolean isAssociative;
	
	public ComparatorSequenceSet(SDKNode node, boolean isAssociative) {
		super(node);
		this.isAssociative = isAssociative;
		buildSequenceSet();
	}

	private void buildSequenceSet() {		
		
		if(isAssociative) {
			this.eval2 = new ChainEvaluator(this.evaluator);		
		} else {			
			this.eval2 = this.evaluator;
		}						
	}

}
