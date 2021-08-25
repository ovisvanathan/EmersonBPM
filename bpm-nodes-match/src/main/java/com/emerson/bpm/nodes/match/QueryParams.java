package com.emerson.bpm.nodes.match;

import java.util.List;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.OrderedSet;

public class QueryParams {
	
	Object[] items;
	List<String> fieldNames;
	
	ClauseComparator defaultComparator;
	OrderedSet sequenceSet;
	
	public QueryParams(Object[] items, List<String> fieldNames) {
		this.items = items;
		this.fieldNames = fieldNames;
	}

	public Object[] getItems() {
		return items;
	}

	public void setItems(Object[] items) {
		this.items = items;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
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
