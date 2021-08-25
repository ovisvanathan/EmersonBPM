package com.emerson.bpm.nodes.match.el;

import javax.el.ELContext;

import org.mariuszgromada.math.mxparser.Expression;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.event.StateChangeEvent;
import com.emerson.bpm.functor.el.ELEvaluator;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.parser.Node;

public class ExpressionEvaluator implements ELEvaluator  {
	
	Node root;
	
	EvaluationContext context;
	
	
	public ExpressionEvaluator(Node root, EvaluationContext ctxt) {
		this.root = root;
		this.context = ctxt;
	}
	
	@Override
	public boolean evaluate(Object p) {			
		Object tmpResult = root.getValue(context);		
		tmpResult = ELUtils.replaceStrings((String) tmpResult);
		Expression exp = new Expression( (String) tmpResult);
		Object result = exp.calculate();
		return result.equals("1");
	}

	public void setBindData(ELContext ctx) {
		
		SDKNode node = (SDKNode) ctx.getContext(SDKNode.class);
		fireStateChanged(new StateChangeEvent(null, node));
	}

	private void fireStateChanged(StateChangeEvent evt) {

		SDKNode node = (SDKNode) evt.getNewval();
		
		Class nodeClass = node.getKlazz();
		
		Object data = node.getObject();
		
		ELContext ctx = context.getELContext();
		
		String elname = ELUtils.toELName(nodeClass);
		
		ctx.getELResolver().setValue(ctx, data, elname, data);

		
	}

}
