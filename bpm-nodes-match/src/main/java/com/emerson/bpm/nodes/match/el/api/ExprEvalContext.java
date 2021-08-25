package com.emerson.bpm.nodes.match.el.api;

import javax.el.ELContext;

import org.mariuszgromada.math.mxparser.Expression;

import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.util.ServiceFactory;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.parser.Node;


public class ExprEvalContext {
	
		ELContext ctx;
		EvaluationContext evalCtx;
		
		protected UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
				ServiceFactory.getUtilsProvider();

		public ExprEvalContext(ELContext ctx) {
				this.evalCtx = new EvaluationContext(ctx, 
									ctx.getFunctionMapper(),
									ctx.getVariableMapper());
		}

		public Object getResult(Node root) {

			Object result = root.getValue(evalCtx);
			result = EmersonUtils.replaceStrings((String) result);
			Expression exp = new Expression( (String) result);
			return exp.calculate();
		}
}
