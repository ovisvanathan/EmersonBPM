package com.emerson.bpm.functor;

import java.util.Set;

public interface ProcessCombination {

	public void process(Set s1, Set s2, ProcessGlobal global) throws DuplicateSetException, 
	Exception;
	
}
