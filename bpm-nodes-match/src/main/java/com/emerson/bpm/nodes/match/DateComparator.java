package com.emerson.bpm.nodes.match;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import com.emerson.bpm.api.COMPARATOR;

public class DateComparator extends DefaultComparator {
	
	String argName;
	String criteria;
	String startDate;	
	
	Date formattedDate;
	
	Locale lc;
	
	public DateComparator(String argName, String criteria, String startDate) throws Exception {
		super(COMPARATOR.EQUALS);
		this.argName = argName;
		this.criteria = criteria;
		this.startDate = startDate;
	
		processDateTime();
	}

	private void processDateTime() throws Exception {

		
		try {
				lc = EmersonUtils.getCurrentLocale();
				
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, lc);
				formattedDate = df.parse(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("invalid date" + e.getMessage());
		}
		
		
		if(criteria == null || criteria.length() == 0)
			return;
		
		int k = Integer.parseInt(criteria.replaceAll("[\\D]", ""));
		
		if(k < 0) {
			
			
			
		} else if(k >= 0) {
			
		}
			
		
		
	}
}
