package com.emerson.bpm.api;

public interface TupleSink {

	default void addChild(SDKNode node) {}

//	void addParent(ReactiveNode node);
	
//	default void addTupleSink(SDKNode node) {}

}
