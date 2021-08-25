package com.emerson.bpm.nodes.el;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javax.el.ELContext;
import javax.el.VariableMapper;
import javax.inject.Inject;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.nodes.match.el.ELException;
import com.emerson.bpm.nodes.match.el.ExpressionBuilder;
import com.emerson.bpm.nodes.match.el.ExpressionEvaluator;
import com.emerson.bpm.nodes.match.el.ExpressionFactory;
import com.emerson.bpm.nodes.match.el.ExpressionResolver;
import com.emerson.bpm.nodes.match.el.RuleTerm;
import com.emerson.bpm.nodes.match.el.api.FunctionMapperImpl;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.picasso.paddle.inject.BeanGetters;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.parser.Node;

import nz.org.take.Fact;

public class ELExpressionResolver implements ExpressionResolver {

	
	@Inject
	BeanGetters beanGetters;
	
	String expr;
		
	Class [] exprClasses = new Class [0];
	
	FunctionMapperImpl fnMapper;
	
	VariableMapper varMapper;
	
	static ELExpressionResolver expressionResolver = new ELExpressionResolver();
	
	static List<ExpressionResolver> resolvers = new ArrayList();
		
	private ELExpressionResolver() {
	
	}

	public static ExpressionResolver getResolverInstance(Class [] exprClasses, String expr) {
	
		ExpressionResolver resolver = expressionResolver.
												new ExpressionResolverUnit(
														exprClasses, expr);
		resolvers.add(resolver);	
		
		return resolver;
	}

	public class ExpressionResolverUnit implements ExpressionResolver {
		Class [] exprClasses;
		
		String expr;

		ELContext ctxt;

		Node root;
		
		ClauseComparator evaluator;
		
		public ExpressionResolverUnit(Class [] exprClasses, String expr) {
			
			this.expr = expr;
			this.exprClasses = exprClasses;
	
			this.ctxt = ExpressionFactory.getELContext();

			parse();

		}

		public ExpressionResolverUnit() {
			this.ctxt = ExpressionFactory.getELContext();
		}

		BiFunction<Method, String, Integer> mapperFunction = (m, e) -> {
			fnMapper.addFunction("", m.getName(), m, e);
			return 0;
		};
		
		private Node parse() {

			try {
					ExpressionBuilder builder = new ExpressionBuilder(expr, ctxt);
				
					Node root = builder.createNode(expr);		
					
					varMapper = ctxt.getVariableMapper();
		
					fnMapper = (FunctionMapperImpl) ctxt.getFunctionMapper();

					if(exprClasses != null && exprClasses.length > 0) {

						for(Class k : exprClasses) {
											
							String elname = ELUtils.toELName(k);
						
							//	ctxt.getELResolver().setValue(ctxt, account, "account", account);
		
							beanGetters.findGetMethodsAndApplyFunction(k, elname, mapperFunction);			
						}
					}

				} catch (ELException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		

			EvaluationContext evalCtx = new EvaluationContext(ctxt, 
					ctxt.getFunctionMapper(),
					ctxt.getVariableMapper());

			evaluator = new ExpressionEvaluator(root, evalCtx);
					
			return root;
		}

		public ClauseComparator getEvaluator() {
			return this.evaluator;
		}

		public ELContext getELContext() {
			return this.ctxt;
		}

		@Override
		public RuleTerm parseTerm(String s, int line) {
			this.expr = s;			
			Node root = parse();	
			
			return new RuleTerm();			
		}

		@Override
		public Fact parseCondition(String s, int no, boolean isNegated) {
			this.expr = s;			
			Node root = parse();			
			
			return new MXFact(s, root);
		}
		
		
				
	}

	@Override
	public ClauseComparator getEvaluator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ELContext getELContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ExpressionResolver getResolverInstance() {
		ExpressionResolver resolver = expressionResolver.
				new ExpressionResolverUnit();
		resolvers.add(resolver);	

		return resolver;
}

	@Override
	public RuleTerm parseTerm(String s, int line) {

		
		
		return null;
	}

	@Override
	public Fact parseCondition(String s, int no, boolean isNegated) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
