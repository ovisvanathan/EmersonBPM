package com.emerson.bpm.nodes.match;

public class Matches {
	
	String s1, s2;
	
	public Matches(String s1, String s2) {
		this.s1 = s1;
		this.s2 = s2;		
	}

	public String toString() {
		return s1 + "|" + s2;
	}
}
