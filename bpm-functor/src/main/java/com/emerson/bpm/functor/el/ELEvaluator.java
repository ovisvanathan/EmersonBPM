package com.emerson.bpm.functor.el;

import com.emerson.bpm.api.ClauseComparator;

public interface ELEvaluator extends ClauseComparator {
	
	public boolean evaluate(Object p);
		
}
