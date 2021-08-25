package com.emerson.bpm.dsl.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.map.MultiValueMap;

import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.parser.RuleDescr;
import com.emerson.bpm.dsl.util.RulePart.TermPart;
import com.emerson.bpm.engine.Rete;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.parser.Node;

import nz.org.take.ComplexTerm;
import nz.org.take.Constant;
import nz.org.take.DerivationRule;
import nz.org.take.Function;
import nz.org.take.KnowledgeBase;
import nz.org.take.KnowledgeElement;
import nz.org.take.Predicate;
import nz.org.take.Prerequisite;
import nz.org.take.Term;
import nz.org.take.Variable;

public class TakeBuilder {
	
	KnowledgeBase kb;
	
	TakeParser parser;

	private Set<RulePart> ruleParts = new LinkedHashSet();
		
	MultiValueMap predicatesMap = new MultiValueMap();
	
	List<Predicate> rulePredicates = new ArrayList();
	
	List<RulePart> predicates = new LinkedList();
	
	List<RulePart> children = new ArrayList();

	RulePart suroot = new RulePart();

	RulePart root;
	
	public TakeBuilder(KnowledgeBase kb, TakeParser parser) {
		this.kb = kb;
		this.parser = parser;	
	
	}
	
	public List<RulePart> getChildren() {
		return children;
	}

	public void setChildren(List<RulePart> children) {
		this.children = children;
	}

	public void aggregateRules() {

		Rete rete = new Rete();
				
		List<KnowledgeElement> elems = kb.getElements();
		
		for(KnowledgeElement ke : elems) {
			
			if(ke instanceof DerivationRule) {

				RulePart part = new RulePart();
				
				if(root == null) {
					root = part;
					suroot.getChildren().add(root);
					root.setParent(suroot);
				}
				
				DerivationRule dx = (DerivationRule) ke;

				Predicate pred = dx.getPredicate();

				Predicate prevHead = null;
				RulePart prevPart = null;
				if(predicates.size() > 0) {
					prevPart = predicates.get(predicates.size()-1);
					prevHead = prevPart.getRulePredicate();
				}
				
				predicates.add(part);
				
				List<Prerequisite> prereqs = dx.getBody();

				
				part.setRuleId(dx.getId());
				
				part.setAnnotations(dx.getAnnotations());
				
				part.setRulePredicate(pred);
				
				part.setLineNum(dx.getLineNum());
								
				int n = prereqs.size();
				
				part.setNumTerms(n);
				
				for(int d=0;d<n;d++) {
					
					Prerequisite prereq = prereqs.get(d);		
					
					Predicate pq = prereq.getPredicate();

					rulePredicates.add(pq);
					
				//	part.setTermPredicate(d, pq);
					
					Term [] terms = prereq.getTerms();

					doTerms(part, terms, false);				
				
				}

				
				for(Predicate r : rulePredicates) {
					if(r.equals(prevHead)) {						
						part.getChildren().add(prevPart);
						prevPart.setParent(part);
					}
				}
				
				part.setTermPredicates(rulePredicates);

				predicatesMap.put(pred, part);

			}
		
		}
		
	}

	private void doTerms(RulePart part, Term [] terms, boolean isCmplx) {
		
		int d = 0;
		for(Term tm : terms) {
			
			Class type = null;
			if(tm instanceof ComplexTerm) {
				
				ComplexTerm cterm = (ComplexTerm) tm;
				
				Function f = cterm.getFunction();
				
				type = cterm.getType();
				
				Term [] cpterms = cterm.getTerms();
				
			//	part.setFunction(f);
				
			//	part.setReturnType(type);
		
				part.setComplexTerm(d, cterm);
				
				doTerms(part, cpterms, true);
				
			} else if(tm instanceof Variable) {
				
				Variable vab = (Variable) tm;
				
			//	String varname = vab.getName();
				
			//	type = vab.getType();
				
				part.setTermVarPart(d, vab);
				
			} else {
				
				Constant cons = (Constant) tm;
				
			//	Object obj = cons.getObject();
				
			//	String ref = cons.getRef();
				
			//	type = cons.getType();

				part.setTermConstPart(d, cons);

			} 
							
			d++;
			
			ruleParts .add(part);
			
			
		}

	}

	public Topology build() {
		
		Rete rete = new Rete();
		aggregateRules();
							
		
		printRules(suroot.children);

		return rete;
	}
	
	void printRules(List list) {
	
		for(RulePart rp : this.suroot.children) {
			
			String id = rp.getId();

			System.out.println(" got root id " + id);
			
			printRules(rp.children);		
			
		}
			/*
			RuleDescr rule = new RuleDescr(id);
			
		//	rule.setLineNum(rp.getLineNum());
			
			Predicate p = rp.getRulePredicate();
			
			List<Predicate> pds = rp.getTermPredicates();
			
			TermPart [] tps = rp.getTermParts();
			
		//	RTMNode rnode = new RTMNode(p);
			
			int tpx = 0;
			for(TermPart tp : tps) {
				
				Predicate termpred = pds.get(tpx);
				
				if(tp.termtype == 0) {
					
					String varname = tp.var.getName();
					
					Class vartype = tp.var.getType();

			//		AlphaNode anode = new AlphaNode();
					
				} else if(tp.termtype == 1) {
					
					Object obj = tp.cons.getObject();
					
					Class vartype = tp.cons.getType();
					
				} else {
					
					Function f = tp.cterm.getFunction();
					
					Class rtype = tp.cterm.getType();
					
					Term [] tms = tp.cterm.getTerms();
					
				}
				
				
				tpx++;
			}
			
			*/
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
	

}
