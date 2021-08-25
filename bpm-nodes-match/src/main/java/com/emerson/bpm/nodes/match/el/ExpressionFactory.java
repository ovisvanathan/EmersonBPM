package com.emerson.bpm.nodes.match.el;

import javax.el.ELContext;

import com.emerson.bpm.functor.el.ELEvaluator;
import com.sun.el.lang.EvaluationContext;

public class ExpressionFactory {

		ELContext ctxt;
		
		public static ELContext getELContext() {
				return new StandardELContext();
		}
	
	//	public ELEvaluator getExpressionEvaluator(ExpressionBuilder builder) {
	//		return new ExpressionEvaluator(new EvaluationContext(this.ctxt, 
	//						builder.getFunctionMapper(), builder.getVariableMapper()));
	//	}
		
		

}
