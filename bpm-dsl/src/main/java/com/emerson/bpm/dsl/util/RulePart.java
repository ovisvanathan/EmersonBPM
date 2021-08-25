package com.emerson.bpm.dsl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nz.org.take.ComplexTerm;
import nz.org.take.Constant;
import nz.org.take.Function;
import nz.org.take.Predicate;
import nz.org.take.Variable;

public class RulePart {

	public static final int VARIABLE = 0;
	public static final int CONSTANT = 1;
	public static final int COMPLEX_TERM = 2;

	Predicate rulePredicate;
	
	public List<RulePart> getChildren() {
		return children;
	}

	public void setChildren(List<RulePart> children) {
		this.children = children;
	}

	public RulePart getParent() {
		return parent;
	}

	public void setParent(RulePart parent) {
		this.parent = parent;
	}

	List<RulePart> children = new ArrayList();
	
	RulePart parent;
	
	public List<Predicate> getTermPredicates() {
		return termPredicates;
	}

	public TermPart[] getTermParts() {
		return termParts;
	}

	public void setTermParts(TermPart[] termParts) {
		this.termParts = termParts;
	}

	public static int getVariable() {
		return VARIABLE;
	}

	public static int getConstant() {
		return CONSTANT;
	}

	public static int getComplexTerm() {
		return COMPLEX_TERM;
	}

	public Predicate getRulePredicate() {
		return rulePredicate;
	}

	public int getLineNum() {
		return lineNum;
	}

//	Predicate [] termPredicates;		

	TermPart [] termParts;

	private int lineNum;
	
	private String id;

	List<Predicate> termPredicates;
	
	public void setTermPredicates(List<Predicate> termPredicates) {
		this.termPredicates = termPredicates;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, String> getAnnotations() {
		return annotations;
	}

	private Map<String, String> annotations;
	
	class TermPart {
		
		int index;
		
		int termtype;
		
		Variable var;
		
		Constant cons;
		
		ComplexTerm cterm;
	
	}
	
	public void setRulePredicate(Predicate pred) {
		this.rulePredicate = rulePredicate;
	}

	public void setTermVarPart(int d, String varname, Class type) {

		
	}

	public void setTermConstPart(int d, Object obj, Class type, String ref) {
		// TODO Auto-generated method stub
		
	}

	public void setFunction(int d, Function f) {
		// TODO Auto-generated method stub
		
	}

	public void setReturnType(Class type) {
		// TODO Auto-generated method stub
		
	}

	public void setNumTerms(int n) {
		this.termParts = new TermPart[n];
	}

	public void setComplexTerm(int d, ComplexTerm cterm) {
		
		TermPart tp = new TermPart();
		
		tp.index = d;
		tp.termtype = 2;		
		tp.cterm = cterm;

		this.termParts[d] = tp;
	}

	public void setTermVarPart(int d, Variable vab) {

		TermPart tp = new TermPart();
		
		tp.index = d;
		tp.termtype = 0;		
		tp.var = vab;

		this.termParts[d] = tp;
		
	}

	public void setTermConstPart(int d, Constant cons) {
		TermPart tp = new TermPart();
		
		tp.index = d;
		tp.termtype = 1;		
		tp.cons = cons;

		this.termParts[d] = tp;
		
	}

	public void setRuleId(String id) {
		this.id = id;
	}

	public void setAnnotations(Map<String, String> annotations) {
		this.annotations = annotations;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

}
