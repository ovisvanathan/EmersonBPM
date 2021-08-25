package com.emerson.bpm.functor.cmp;

import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public class FloatNumeric extends Numeric {

	public FloatNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	public boolean evalEquals(Object param) {
		return (float) param == (float) data;
	}

	public boolean evalGreater(Object param) {
		return (float) param > (float) data;
		
	}

	public boolean evalGEQ(Object param) {
		return (float) param >= (float) data;

	}

	public boolean evalLess(Object param) {
		return (float) param < (float) data;

	}

	public boolean evalLEQ(Object param) {
		return (float) param <= (float) data;

	}


}
