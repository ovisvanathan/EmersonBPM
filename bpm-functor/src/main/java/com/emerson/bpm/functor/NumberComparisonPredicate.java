package com.emerson.bpm.functor;

import java.util.function.Function;

import com.emerson.bpm.functor.cmp.Numeric;

public class NumberComparisonPredicate<T> implements Predicate2<T> {

	Object data;

	Object data2;

	Numeric dataNumer;
	Numeric dataNumer2;

	Function<Object, Boolean> calcEvaluate = (b) -> {
		
		if(b == null)
			b = data2;
		
		if(dataNumer.evalop == Criterion.EQUAL)
			return dataNumer.evalEquals(b);
		else if(dataNumer.evalop == Criterion.GREATER)
			return dataNumer.evalGreater(b);
		else if(dataNumer.evalop == Criterion.GEQ)
				return dataNumer.evalGEQ(b);
		else if(dataNumer.evalop == Criterion.LESS)
			return dataNumer.evalLess(b);
		else if(dataNumer.evalop == Criterion.LEQ)
			return dataNumer.evalLEQ(b);

		return false;
	};


	
	public enum Criterion {
		 EQUAL,
		 GREATER,
		 GEQ,
		 LESS,
		 LEQ,
	};

	public NumberComparisonPredicate(Object arg0, Criterion p) {

		this.data = arg0;
		
		try {
				dataNumer = FunctorUtils.checkPrimitive(arg0, p);
		} catch(Exception e) {
			System.out.println("NAN");
		}
				
	}

	public NumberComparisonPredicate(Object arg0, Object arg1, Criterion p) {

		this.data = arg0;
		this.data2 = arg1;
		
		try {
				dataNumer = FunctorUtils.checkPrimitive(arg0, p);

		} catch(Exception e) {
			System.out.println("NAN");
		}
				
	}

	public boolean evaluate(final T arg0) {
		 
		return calcEvaluate.apply(arg0);
		 
	 }

	@Override
	public boolean evaluate(T... object) {
		return false;
	}

	@Override
	public boolean evaluateChain(T ... seq) {

		return false;
	}

}