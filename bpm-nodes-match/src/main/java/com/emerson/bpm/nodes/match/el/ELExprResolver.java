package com.emerson.bpm.nodes.match.el;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Stream;

import javax.el.ELContext;
import javax.el.VariableMapper;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.nodes.match.DefaultComparator;
import com.emerson.bpm.nodes.match.el.api.ExprEvalContext;
import com.emerson.bpm.nodes.match.el.api.FunctionMapperImpl;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.nodes.match.el.util.ResolverOps;
import com.emerson.bpm.nodes.match.el.util.ResolverOps.StringOperand;
import com.emerson.bpm.util.ServiceFactory;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.parser.Node;
import com.sun.el.parser.NodeVisitor;

public class ELExprResolver extends DefaultComparator  {

	String expr;
	static ELContext ctxt;
	
	protected static UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	public ELExprResolver(String expr2) {
		super(COMPARATOR.EQUALS);
		this.expr = expr2;

		parse(expr);
		
	}
	
	private void parse(String expr2) {

		
		
	}

	public boolean evaluate(Object e) {
	
		return false;
	}

	static class Customer {
		
		int custId;
		
		String name;
		
		int age;
		
		public int getCustId() { return 10; }
		
		public String getName() { return "avon"; }
		
		public int getAge() { return 30; }
		
	}

	static class Account {
		
		int customerId;
		
		public int getCustomerId() { return 10; }
		
	}

	public static void main(String [] args) {
		  
		ctxt = ExpressionFactory.getELContext();

		Customer cust = new Customer();
		
		Account acct = new Account();
		
		FunctionMapperImpl fmap = (FunctionMapperImpl) ctxt.getFunctionMapper();

	//	ctxt.getELResolver().setValue(ctxt, cust, "", "p1");
	//	ctxt.getELResolver().setValue(ctxt, acct, "", "a4");
					  		
		String expr = "${p1.custId} == ${a4.customerId} && ( ${p1.name} == 'avon' && ${p1.age} == 30 ) ";

		
		String expr2 = "( ${p1.name} == 'Anshu'  && ${place} == 'here' ) && ( ${p1.custId} == ${a4.customerId} ) && ( ${p1.name} == 'avon' && ${p1.age} == 30 )  ";

		String expr3 = "2 + (7 - 5) * 3.14159 * x^(12-10) + sin(-3.141)";
		
		try {

			ExpressionBuilder builder = new ExpressionBuilder(expr, ctxt);
			
			Node root = builder.createNode(expr);
			
	//		Node root = builder.build();

			FunctionMapperImpl fnMapper = (FunctionMapperImpl) ctxt.getFunctionMapper();
			VariableMapper varMapper = ctxt.getVariableMapper();
			
			Method meth = ELUtils.findGetMethod(Customer.class, "custId");
			
			fnMapper.addFunction("", meth.getName(), meth, "p1");
			
			Method meth2 = ELUtils.findGetMethod(Account.class, "customerId");
			
			fnMapper.addFunction("", meth2.getName(), meth2, "a4");

			Method meth3 = ELUtils.findGetMethod(Customer.class, "name");
			
			fnMapper.addFunction("", meth3.getName(), meth3, "p1");

			Method meth4 = ELUtils.findGetMethod(Customer.class, "age");
			
			fnMapper.addFunction("", meth4.getName(), meth4, "p1");

//			printTree(new EvaluationContext(ctxt, fnMapper, varMapper), root, 0);
			
			
			
						
			ctxt.getELResolver().setValue(ctxt, cust, "p1", cust);
			ctxt.getELResolver().setValue(ctxt, acct, "a4", acct);
			
			/*
			String exprA = "${p1}";
			
			Node nodeA = ExpressionBuilder.createNode(exprA);

			ValueExpression vxp1 = new ValueExpressionImpl(exprA, nodeA, fnMapper,
				          										varMapper,  Customer.class);

			vxp1.setValue(new EvaluationContext(ctxt, fnMapper, varMapper), cust);
			
			String exprB = "${a4}";
			
			Node nodeB = ExpressionBuilder.createNode(exprB);

			ValueExpression vxp2 = new ValueExpressionImpl(exprB, nodeB, fnMapper,
						varMapper,  Account.class);

			String exprC = "name";
			
			Node nodeC = ExpressionBuilder.createNode(exprC);

			ValueExpression vxp3 = new ValueExpressionImpl(exprC, nodeC, fnMapper,
						varMapper,  Customer.class);

			vxp3.setValue(ctxt, cust);

			String exprD = "age";
			
			Node nodeD = ExpressionBuilder.createNode(exprD);

			ValueExpression vxp4 = new ValueExpressionImpl(exprD, nodeD, fnMapper,
						varMapper,  Customer.class);

			vxp4.setValue(ctxt, cust);
			*/

			/*
			ValueExpression vxp = new ValueExpressionImpl();
			vxp.setValue(ctxt, cust);
			varMapper.setVariable("p1", vxp);
			
			ValueExpression axp6 = new ValueExpressionImpl();
			axp6.setValue(ctxt, acct);
			varMapper.setVariable("a4", axp6);

			*/

			
	//		Node node = builder.build();
			
			ExprEvalContext evalCtx = new ExprEvalContext(ctxt); 
						
			Object result = evalCtx.getResult(root);
			
			System.out.println(" result x = " + result);
	
			
	//		printResult(result);
		
		} catch (ELException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	//	ExpressionEvaluator expeval = ExpressionFactory.getExpressionEvaluator(builder);
		
	//	expeval.evaluate();
	//		nvs.visit(root);
			
		
	}

	private static void printResult(Object result) {
		
			String [] str = result.toString().split(" ");
			
			Stream tokenStream = Arrays.asList(str).stream().map(e -> new StringOperand(e));
			
			Stream charstream = Stream.of(tokenStream, "::=");

			Object osr2 = calcResult(charstream);
			
			System.out.println(" osres 2 = " + osr2);
			
	}
	
	private static Object calcResult(Stream result) {
		//  10 == 10 && ( avon == 'avon' && 30 == 30 ) 
		
			Object expresult = null;	
			Stack operands = new Stack();	

			ResolverOps revops = new ResolverOps(operands);
	//		Supplier<Stream> supplier = () -> result;
			
//			while(operands.size() != 1) {
				
				result.forEach(e -> {
					
						Object res1 = null;
						Optional.of(ResolverOps.expectOperator(e)).
								orElseGet(() -> {
									
									Optional.of(ResolverOps.expectGroup(e)).
										orElseGet(() -> {								
												
												operands.push(EmersonUtils.strToType((String) e));			

												return true;
										});
								
										return true;
								});
					});
					
				if(!operands.isEmpty())
					expresult = operands.pop();
				
//			}

			return expresult;

	}

	static void echo(String s, int n, int indent) {

		for(int i=0;i<indent;i++)
			System.out.print(" ");

			System.out.println(s + "(" + n + ")");
	}
		
	private static void printTree(EvaluationContext ctx, Node root, int ind) {
	
		int n = root.jjtGetNumChildren();
		
		echo(root.toString(), n, ind);
		

		if(n > 0) {

			for(int i=0;i<n;i++) {					
				Node child = root.jjtGetChild(i);
				printTree(ctx, child, ind + 6);									
			}
		}
	
	}
	
	

	static class ELNodeVisitor implements  NodeVisitor {

		
//		FunctionMapperFactory factory = new FunctionMapperFactory (fmap);
		
		
		public ELNodeVisitor() {
			
			
		}
		
		@Override
		public void visit(Node node) throws javax.el.ELException {

			System.out.println("node class = " + node.getClass().getName());
			
			/*
			if(node instanceof ASTCompositeExpression) {
				ASTCompositeExpression astComp = (ASTCompositeExpression) node;
				
			} else if(node instanceof ASTValue)
			
			
			int n = node.jjtGetNumChildren();

			System.out.println("n = " + n);

			for(int i=0;i<n;i++) {			
						Node child = node.jjtGetChild(i);				
						visit(child);						
			}
			*/
						
		}
		
		
	}

		

}
