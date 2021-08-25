package com.emerson.bpm.dsl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class APICall {

	Map<String, String> apiparams = new HashMap();

	List<Function> criterias = new ArrayList();
	
	String exprCriteria;
				
	public Map getCredentials() {
		return apiparams;
	}

	public List<Function> getCriteria() {
		return criterias;
	}

	public void setExprCriteria(String exprCriteria) {
		this.exprCriteria = exprCriteria;
	}

}
