package com.emerson.bpm.functor.cmp;

import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public class IntNumeric  extends Numeric {

	public IntNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	public boolean evalEquals(Object param) {
		return (int) param == (int) data;
	}

	public boolean evalGreater(Object param) {
		return (int) param > (int) data;
		
	}

	public boolean evalGEQ(Object param) {
		return (int) param >= (int) data;

	}

	public boolean evalLess(Object param) {
		return (int) param < (int) data;

	}

	public boolean evalLEQ(Object param) {
		return (int) param <= (int) data;

	}


}
