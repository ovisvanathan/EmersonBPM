package com.emerson.bpm.nodes.match;

/*
 * For checking class referential constraints
 * 
 * cust.name == account.customerName etc.
 */
public interface ClassFieldNameMatcher extends NameMatcher {

	default boolean evaluate(String s1, String s2) { return true; }

	default boolean evaluate(Object c1, String c1s1, Object c1s1val) throws Exception { return true; }

	default boolean evaluate(Object c1, Object c2, String c1s1, String c2s2) throws Exception { return true; }
	
}
