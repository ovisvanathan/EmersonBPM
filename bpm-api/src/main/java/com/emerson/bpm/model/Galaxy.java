package com.emerson.bpm.model;

import java.util.Date;

public class Galaxy {

	public enum GALAXY_DATE {
		EON,
		ERA,
		METRONS,
		NUMAS,
		HOURS,
		MINUTES,
		SECONDS,
		NANOSECS,
		PARSECS,
		LIGHTCYCLES		
	};
	
	public static GDate TODAY;
	
	class GDate {
		
		GALAXY_DATE gxdate;

		public GALAXY_DATE getGxdate() {
			return gxdate;
		}

		public void setGxdate(GALAXY_DATE gxdate) {
			this.gxdate = gxdate;
		}

		public Date getEarthDate() {
			return new Date();
		}
		
		
		
		
		
		
	}
	
}
