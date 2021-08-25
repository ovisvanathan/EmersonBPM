package com.emerson.bpm.nodes.match.el;

import java.util.ArrayList;
import java.util.List;

public class WherePart {

	String expr;	
	
	public List<WherePart> parts =  new ArrayList();
	
	public List<WherePart> getParts() {
		return parts;
	}

	public String getExpr() {
		return expr;
	}
	
	public WherePart(String expr) {
		this.expr = expr;
	}
	
	
}
