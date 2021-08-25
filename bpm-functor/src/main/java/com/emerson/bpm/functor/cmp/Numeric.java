package com.emerson.bpm.functor.cmp;

import com.emerson.bpm.functor.NumberComparisonPredicate;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public abstract class Numeric {

	protected Object data;
	
	public Criterion evalop;

	public Numeric(Object input, Criterion crit) {
		this.data = input;
		this.evalop = crit;
	}

	public abstract boolean evalEquals(Object param);

	public abstract boolean evalGreater(Object param);

	public boolean evalGEQ(Object param) { return false; }

	public abstract boolean evalLess(Object param);

	public boolean evalLEQ(Object param) { return false; }

}
