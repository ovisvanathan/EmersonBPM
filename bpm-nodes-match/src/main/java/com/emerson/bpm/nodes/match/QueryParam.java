package com.emerson.bpm.nodes.match;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.OrderedSet;

public class QueryParam {

	Object orig;
	String fieldName;
	Object fieldVal;

	ClauseComparator defaultComparator;
	OrderedSet sequenceSet;
	
	public QueryParam(Object orig, String fieldName, Object fieldVal) {
		this.orig = orig;
		this.fieldName = fieldName;
		this.fieldVal = fieldVal;
	}

	public Object getOrig() {
		return orig;
	}

	public void setOrig(Object orig) {
		this.orig = orig;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldVal() {
		return fieldVal;
	}

	public void setFieldVal(Object fieldVal) {
		this.fieldVal = fieldVal;
	}
	
	public ClauseComparator getEvaluator() {
		return this.defaultComparator;
	}

	public void setDefaultComparator(ClauseComparator defaultComparator) {
		this.defaultComparator = defaultComparator;
	}

	public void setSequenceSet(OrderedSet sequenceSet) {
		this.sequenceSet = sequenceSet;
	}

	public OrderedSet getSequenceSet() {
		return this.sequenceSet;
	}


}
