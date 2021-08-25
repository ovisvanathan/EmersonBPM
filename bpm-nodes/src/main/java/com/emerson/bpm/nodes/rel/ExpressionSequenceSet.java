package com.emerson.bpm.nodes.rel;

import javax.el.ELContext;

import com.emerson.bpm.api.Evaluator;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.functor.cmp.Numeric;
import com.emerson.bpm.functor.el.ELProvider;
import com.emerson.bpm.nodes.el.ELExpressionResolver;
import com.emerson.bpm.nodes.match.SequenceSet;
import com.emerson.bpm.nodes.match.el.ExpressionResolver;
import com.emerson.bpm.nodes.match.el.util.ELUtils;

public class ExpressionSequenceSet extends SequenceSet  implements ELProvider {

	ExpressionResolver resolver;
	ELContext context;
	
	public ExpressionSequenceSet(SDKNode node) {
		super(node);
	}

	public ExpressionSequenceSet(SDKNode node, REL relation) {
		super(node, relation);
	
		parseExpression();	
	}

	private void parseExpression() {

		SDKNode item = (SDKNode) getNode();
		REL relation = getRelation();
		
		String name = relation.getName();
		String expr = relation.getExpr();
		int nparams = relation.getNumParams();
		
		boolean isAssoc = relation.isAssociative();
				
		this.resolver = ELExpressionResolver.getResolverInstance(
				new Class [] { Comparable.class }, expr);		

		this.context = resolver.getELContext();
		
	}
	
	
	@Override
	public Evaluator getEvaluator() {
		return null;
	}

}
