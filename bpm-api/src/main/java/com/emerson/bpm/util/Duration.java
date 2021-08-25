package com.emerson.bpm.util;

import java.util.Date;

public class Duration {
	
	int dura;
	
	public final static Date TODAY = new Date();
	
	public enum Unit {
		DAYS,
		MONTHS,
		YEARS,
		HOURS,
		MINUTES,
		SECONDS,
		WEEKS
	};
		
	public Duration(int dur) {
		this.dura = dur;
	}

	public int getDura() {
		return dura;
	}

	public void setDura(int dura) {
		this.dura = dura;
	}
	
	
}
