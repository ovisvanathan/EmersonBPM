package com.emerson.bpm.nodes.el;

import java.util.Map;

import com.sun.el.parser.Node;

import nz.org.take.Predicate;

public class MXPredicate implements Predicate {

	String name;
	
	Object node;
	
	public 	MXPredicate(String s, Node root) {
		this.name = s;
		this.node = root;
	}
	
	@Override
	public void addAnnotation(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAnnotations(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAnnotation(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeAnnotation(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getSlotNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class[] getSlotTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNegated() {
		// TODO Auto-generated method stub
		return false;
	}

}
