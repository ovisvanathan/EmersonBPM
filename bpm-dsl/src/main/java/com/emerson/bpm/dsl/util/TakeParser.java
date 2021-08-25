package com.emerson.bpm.dsl.util;

import java.beans.PropertyDescriptor;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;

import com.emerson.bpm.dsl.parser.AbstractTakeParser;
import com.emerson.bpm.nodes.match.el.RuleTerm;

import nz.org.take.Fact;
import nz.org.take.JPredicate;
import nz.org.take.KnowledgeBase;
import nz.org.take.Predicate;
import nz.org.take.Prerequisite;
import nz.org.take.PropertyPredicate;
import nz.org.take.SimplePredicate;
import nz.org.take.Term;
import nz.org.take.nscript.JSPELParser;
import nz.org.take.nscript.ScriptException;
import nz.org.take.nscript.Tokenizer;

public class TakeParser extends AbstractTakeParser {

//	protected ExpressionResolver elParser = ELExpressionResolver.getResolverInstance();
	protected JSPELParser elParser = new JSPELParser(variables,constants,aggregationFunctions);

	
	public TakeParser() {
		
	}
	
	@Override
	public KnowledgeBase parse(Reader r) throws ScriptException {	
		
		try {

			System.out.println("enter parse = ");
		
			return super.parse(r);

		} finally {

			System.out.println("exit parse = ");

		}

	}
	

	protected Fact parseCondition (String s,int no, boolean isPrerequisite,boolean isNegated) throws ScriptException {

			System.out.println("parseCondition s= "+s);
		
		if (CONDITION1.matcher(s).matches()) {
			int sep = -1;
			Fact fact = isPrerequisite?new Prerequisite():new Fact();
			// type of the condition is predicate[terms]
			sep=s.indexOf('[');
			String p = s.substring(0,sep);

			
			String t = s.substring(sep+1,s.length()-1);
			List<String> unparsedTerms = Tokenizer.tokenize(t,",");
			List<RuleTerm> terms = new ArrayList<RuleTerm>();
			for (String ut:unparsedTerms) {
		//		System.out.println("unparsedTerm= "+ut);
				terms.add(parseTerm(ut,no));
			}
			Class[] types = new Class[terms.size()];
			for (int i=0;i<terms.size();i++) {
				types[i]=terms.get(i).getType();
			}

			Predicate predicate = this.buildPredicate(p, isNegated, terms.toArray(new RuleTerm[terms.size()]),no);
			fact.setPredicate(predicate);
			fact.setTerms(terms.toArray(new RuleTerm[terms.size()]));
			return fact;
		}
		else {
			if (!isPrerequisite) {
				throw new ScriptException("Error in line " + no + " - the rule head must have the following form: predicate[term_1,..,term_n]");
			}
			else {
				// parse with JUEL
				return elParser.parseCondition(s,no,isNegated);
			}
		}
	}

	protected RuleTerm parseTerm(String s,int line) throws ScriptException {

		System.out.println("parseTerm TP name= "+ s);

		return new RuleTerm(this.elParser.parseTerm(s, line));
	}


	protected nz.org.take.Predicate buildPredicate(String name,boolean negated, Term[] terms,int line) throws ScriptException {
		Predicate predicate = null;

		System.out.println("buildPredicate TP name= "+name);

		Method m = getMethod(name,terms);
		PropertyDescriptor property = null;
		if (m==null)
			property = getProperty(name, terms[0].getType());
		
		if (m!=null) {
			debug("Interpreting predicate symbol ",name," as Java method ",m," in line ",line);

		//	System.out.println("buildPredicate as Java method= ");

			JPredicate p = new JPredicate();
			Class[] paramTypes = getParamTypes( terms);
			p.setMethod(m);
			p.setNegated(negated);
			predicate=p;			
		}
		else if (property!=null) {
			debug("Interpreting predicate symbol ",name," as bean property"," in line ",line);

		//	System.out.println("buildPredicate as bean property= ");

			PropertyPredicate p = new PropertyPredicate();
			p.setProperty(property);
			p.setOwnerType(terms[0].getType());
			p.setNegated(negated);
			// todo remove this line that just does lazy initialization
			predicate=p;			
		}
		else {
			debug("Interpreting predicate symbol ",name," as simple predicate"," in line ",line);

		//	System.out.println("buildPredicate as simple predicate= ");
			SimplePredicate p = new SimplePredicate();
			p.setName(name);
			p.setNegated(negated);
			Class[] types = new Class[terms.length];
			for (int i=0;i<terms.length;i++) { 
				types[i] = terms[i].getType();			
			}
			p.setSlotTypes(types);
			predicate=p;
		}
		String id = this.getId(predicate);
		Predicate existingPredicate = predicatesByName.get(id);
		if (existingPredicate==null) {
			predicatesByName.put(this.getId(predicate),predicate);
			return predicate;
		}
		else
			return existingPredicate;
	}
	

	public ELContext getELContext() {
//		return elParser.getELContext();
		return null;
	}

}
