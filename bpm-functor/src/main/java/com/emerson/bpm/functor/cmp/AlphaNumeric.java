package com.emerson.bpm.functor.cmp;

import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public class AlphaNumeric extends Numeric {

	public AlphaNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	public boolean evalEquals(Object param) {
		return ((String) param).compareTo((String) data) == 0;
	}

	public boolean evalGreater(Object param) {
		return ((String) param).compareTo((String) data) > 0;		
	}

	public boolean evalLess(Object param) {
		return ((String) param).compareTo((String) data) < 0;
	}


}
