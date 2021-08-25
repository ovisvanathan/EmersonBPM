package com.emerson.bpm.api;

public interface SetFilter {

	public boolean accept(Object e) throws Exception;
	
	void setEvaluator(Object evaluator);

}
