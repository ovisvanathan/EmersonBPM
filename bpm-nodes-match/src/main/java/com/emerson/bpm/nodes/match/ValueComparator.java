package com.emerson.bpm.nodes.match;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.functor.FunctorUtils;
import com.emerson.bpm.functor.IdentityPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;
import com.emerson.bpm.util.ServiceFactory;
import com.picasso.paddle.annotation.Component;

/*
 * Checks if the 2 named values are equal
 */
@Component
public class ValueComparator extends DefaultComparator {

	String name1;
	String name2;
	
	
	Object val1;
	Object val2;
	
	public ValueComparator(String name1, String name2) {
		super(COMPARATOR.EXISTS);
		this.name1 = name1;
		this.name2 = name2;		
	}

	@Override
	public boolean evaluate(Object o) {

		Session session = ServiceFactory.getSession();
		IdentityPredicate p1 = (IdentityPredicate) FunctorUtils.getPredicateByName(session, name1);
		IdentityPredicate p2 = (IdentityPredicate) FunctorUtils.getPredicateByName(session, name2);

		NumberComparisonPredicate np2 = new 
				NumberComparisonPredicate(p1.getValue(), p2.getValue(), Criterion.EQUAL);
		return np2.evaluate(null);
	}
	
	
}
