package com.emerson.bpm.nodes.match;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Predicate;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.functor.BooleanPredicate;
import com.emerson.bpm.util.ServiceFactory;

public class YesNoComparator extends DefaultComparator {
	
	String ruleName;
	
	String displayName;
	
	
	public YesNoComparator(String ruleName) {		
		super(COMPARATOR.EQUALS);
		this.displayName = "has" + ruleName;
	}
	
	public boolean evaluate() {
		Session session = ServiceFactory.getSession();
		Predicate p = findPredicate(session, this.displayName);
		
		return p.evaluate(null);
	}

	private Predicate findPredicate(Session emersonSession, String displayName2) {

		List<Predicate> preds = 
				emersonSession.getWorkingMemory().getPredicatesCollection().stream()
				.filter(x -> (x instanceof BooleanPredicate))
					.filter(m -> m.toString().equals(displayName2))
						.collect(Collectors.toList());
		
		assert(preds.size() == 1);	
		
		return preds.get(0);		
	}
	
	
	
	
}
