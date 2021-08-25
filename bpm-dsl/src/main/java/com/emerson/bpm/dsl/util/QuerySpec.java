package com.emerson.bpm.dsl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emerson.bpm.functor.Predicate2;

public class QuerySpec {

	Map bindVarsMap = new HashMap();

	Map<String, Predicate2> predicates = new HashMap();

	Map<String, BindingFunction> bindingFuncs = new HashMap();

	List<APICall> apiCalls = new ArrayList();
	
	public Map getBindingVariables() {
		return bindVarsMap;
	}

	public Map<String, Predicate2> getPredicates() {
		return predicates;
	}

	public List<APICall> getApiCalls() {
		return apiCalls;
	}

	public Map<String, BindingFunction> getBindingFunctions() {
		return bindingFuncs;
	}

}
