package com.emerson.bpm.functor;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Predicate;

import com.emerson.bpm.api.Session;
import com.emerson.bpm.util.ServiceFactory;

public class NamedPredicate extends IdentityPredicate {
	
	String name1;
	String name2;

	Object [] args;
	 
	public NamedPredicate(String name, Object ... args) {
		super(name);
		this.name1 = name;
		this.args = args;
	}
	
	public NamedPredicate(String name1, String name2) {
		super(name1);
		this.name1 = name1;
		this.name2 = name2;
	}
	
	
	public boolean evaluate(Object ... arg0) {
	
		Object [] bx = arg0;
			
		Session session = ServiceFactory.getSession();
		
		Predicate p1 = FunctorUtils.getPredicateByName(session, name1);
			
		return p1.evaluate(bx);
		
	}
	
	public List<String> names() {

		return Arrays.asList(new String [] {name1, name2});
	}
	
}
