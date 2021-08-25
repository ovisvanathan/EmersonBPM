package com.emerson.bpm.api;

import java.util.List;

import com.emerson.bpm.util.Array;

public interface RowTuple extends Array {

	default int getLength() { return 0; }
	
	default SDKNode [] getItems() { return null; }

	default List getItemsAsList()  { return null; }

	default public int getOrder() { return 2; }

	default void addTuple(SDKNode node) {}

}
