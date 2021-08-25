package com.emerson.bpm.functor.el;


import com.emerson.bpm.api.Evaluator;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.functor.Predicate2;

/*
 * A Predicate that accepts an ELExpression and checks if it is true
 */
public class ExpressionPredicate implements Predicate2 {
	
	String relName;
	REL rel;
	ELProvider provider;
	
	Evaluator predicateEvaluator;
	
	public ExpressionPredicate(REL rel, ELProvider provider) {	
		this.rel = rel;
		this.provider = provider;		
	
		buildEvaluationSet();
	}
	
	private void buildEvaluationSet() {
		
		Evaluator eval2 = this.provider.getEvaluator();
		
		if(rel.isAssociative()) {
			predicateEvaluator = new ChainEvaluator(eval2);		
		} else {			
			predicateEvaluator = eval2;
		}					
	}

	
	@Override
	public boolean evaluate(Object args) {	
		try {
			return predicateEvaluator.evaluate(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Evaluator getEvaluator() {
		return this.predicateEvaluator;
	}
	
}
