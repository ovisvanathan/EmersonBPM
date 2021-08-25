package com.emerson.bpm.nodes.match.el;

import javax.el.ELContext;

import com.emerson.bpm.api.ClauseComparator;

import nz.org.take.Fact;

public interface ExpressionResolver {

	ClauseComparator getEvaluator();

	public ELContext getELContext();

	RuleTerm parseTerm(String s, int line);

	Fact parseCondition(String s, int no, boolean isNegated);
	

}
