package com.emerson.bpm.nodes.el;

import com.sun.el.parser.Node;

import nz.org.take.Fact;
import nz.org.take.Predicate;

public class MXFact extends Fact {

	Predicate mxp;
	
	public MXFact(String s, Node root) {
		this.mxp = new MXPredicate(s, root); 
	}

	public Predicate getPredicate() {
		return this.mxp;
	}

}
