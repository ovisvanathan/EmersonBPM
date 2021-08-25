package com.emerson.bpm.functor.cmp;

import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public class ByteNumeric extends Numeric {

	public ByteNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	public boolean evalEquals(Object param) {
		return (byte) param == (byte) data;
	}

	public boolean evalGreater(Object param) {
		return (byte) param > (byte) data;
		
	}

	public boolean evalGEQ(Object param) {
		return (byte) param >= (byte) data;

	}

	public boolean evalLess(Object param) {
		return (byte) param < (byte) data;

	}

	public boolean evalLEQ(Object param) {
		return (byte) param <= (byte) data;

	}

}
