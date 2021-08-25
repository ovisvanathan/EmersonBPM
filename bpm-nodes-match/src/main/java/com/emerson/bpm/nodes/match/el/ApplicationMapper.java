package com.emerson.bpm.nodes.match.el;

import java.util.HashMap;
import java.util.Map;

public abstract class ApplicationMapper {

	static Map<String, Object> scopedBeans = new HashMap();

	public static Map<String, Object> getScopedBeans() {
		return scopedBeans;
	}
	
	
}
