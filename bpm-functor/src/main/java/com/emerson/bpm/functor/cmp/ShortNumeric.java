package com.emerson.bpm.functor.cmp;

import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public class ShortNumeric extends Numeric {

	public ShortNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	public boolean evalEquals(Object param) {
		return (short) param == (short) data;
	}

	public boolean evalGreater(Object param) {
		return (short) param > (short) data;
		
	}

	public boolean evalGEQ(Object param) {
		return (short) param >= (short) data;

	}

	public boolean evalLess(Object param) {
		return (short) param < (short) data;

	}

	public boolean evalLEQ(Object param) {
		return (short) param <= (short) data;

	}

}
