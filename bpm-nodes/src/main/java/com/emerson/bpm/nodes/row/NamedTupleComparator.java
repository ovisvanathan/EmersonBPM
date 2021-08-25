package com.emerson.bpm.nodes.row;

import com.emerson.bpm.api.ClauseComparator;

public class NamedTupleComparator {
	
	ClauseComparator comparator;
	String leftField, rightField;

	String [] rightFieldNames;

	public NamedTupleComparator(ClauseComparator comparator, String leftField, String rightField) {
		this.comparator = comparator;
		this.leftField = leftField;
		this.rightField = rightField;
	}

	public NamedTupleComparator(ClauseComparator comparator, String leftField, String [] rightFields) {
		this.comparator = comparator;
		this.leftField = leftField;
		this.rightFieldNames = rightFields;
	}

}
