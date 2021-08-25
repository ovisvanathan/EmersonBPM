package com.emerson.bpm.nodes.match;

public interface NameMatcher {
	
	boolean evaluate(String s1, String s2);

	boolean evaluate(Object fieldObj, String fieldName, Object value) throws Exception;

	default boolean evaluate(Object [] args) throws Exception { return true; }
	
	default boolean evaluate(Object c1, Object c2, String c1s1, String c2s2) throws Exception { return true; }

}
