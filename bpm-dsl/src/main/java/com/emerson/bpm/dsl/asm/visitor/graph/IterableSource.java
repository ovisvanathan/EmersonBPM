package com.emerson.bpm.dsl.asm.visitor.graph;

import java.util.Iterator;

public class IterableSource {

	String parentType;
	String fieldLabel;
	String fieldType;
	
	boolean isIterable;

	public IterableSource(String parentType, String fieldLabel, String fieldType) {
		this(parentType, fieldLabel, fieldType, false);	
	}

	public IterableSource(String parentType, String fieldLabel, String fieldType, Boolean isIterable) {
			this.parentType = parentType;
			this.fieldLabel = fieldLabel;
			this.fieldType = fieldType;
			this.isIterable = isIterable;
	}

	public Iterator getIterator() {

		
		return null;
	}

}
