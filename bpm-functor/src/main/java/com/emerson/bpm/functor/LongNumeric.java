package com.emerson.bpm.functor;

import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;
import com.emerson.bpm.functor.cmp.Numeric;

public class LongNumeric extends Numeric {

	public LongNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	public boolean evalEquals(Object param) {
		return (long) param == (long) data;
	}

	public boolean evalGreater(Object param) {
		return (long) param > (long) data;
		
	}

	public boolean evalGEQ(Object param) {
		return (long) param >= (long) data;

	}

	public boolean evalLess(Object param) {
		return (long) param < (long) data;

	}

	public boolean evalLEQ(Object param) {
		return (long) param <= (long) data;

	}


}
