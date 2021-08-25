package com.emerson.bpm.functor;

import java.text.DateFormat;
import java.util.Date;

public class DateTimePredicate {

	boolean dateAfter(String dateTime, DateFormat df) { return true; }
	
	boolean dateAfter(Date date) { return true; }

	boolean dateBefore(String dateTime, DateFormat df) { return true; }
	
	boolean dateBefore(Date date) { return true; }

	boolean dateBetween(Date startDate, Date endDate) { return true; }

	boolean dayOfMonth() {
		return true;
	}

}
