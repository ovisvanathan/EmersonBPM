package com.emerson.bpm.functor.cmp;

import java.util.Date;

import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;

public class DateNumeric extends Numeric {

	public DateNumeric(Object input, Criterion crit) {
		super(input, crit);
	}

	@Override
	public boolean evalEquals(Object param) {
		 if( ((Date)data).equals((Date) param)){
             System.out.println("Date1 is equal Date2");
             return true;
		 }
		 return false;
	}

	@Override
	public boolean evalGreater(Object param) {
		 if( ((Date)data).after((Date) param)){
             System.out.println("Date1 is equal Date2");
             return true;
		 }
		 return false;
	}

	@Override
	public boolean evalLess(Object param) {
		 if( ((Date)data).before((Date) param)){
             System.out.println("Date1 is equal Date2");
             return true;
		 }
		 return false;
	}


}
