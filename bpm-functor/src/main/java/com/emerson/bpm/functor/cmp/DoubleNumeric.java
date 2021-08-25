package com.emerson.bpm.functor.cmp;

import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public class DoubleNumeric extends Numeric  {

	public DoubleNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	public boolean evalEquals(Object param) {
		return (double) param == (double) data;
	}

	public boolean evalGreater(Object param) {
		return (double) param > (double) data;
		
	}

	public boolean evalGEQ(Object param) {
		return (double) param >= (double) data;

	}

	public boolean evalLess(Object param) {
		return (double) param < (double) data;

	}

	public boolean evalLEQ(Object param) {
		return (double) param <= (double) data;

	}


}
