package com.emerson.bpm.functor.el;

import com.emerson.bpm.api.Evaluator;

public class ChainEvaluator implements Evaluator {
	
	Evaluator eval2;
	
	public ChainEvaluator(Evaluator eval2) {
		this.eval2 = eval2;
	}
	
	@Override
	public boolean evaluate(Object e) throws Exception {		
		return this.eval2.evaluate(null) && this.eval2.evaluate(null);		
	}

}
