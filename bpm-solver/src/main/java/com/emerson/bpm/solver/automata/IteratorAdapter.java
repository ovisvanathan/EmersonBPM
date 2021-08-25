package com.emerson.bpm.solver.automata;

import java.util.Iterator;

public interface IteratorAdapter extends Iterator {

	@Override
	public boolean hasNext();

	@Override
	public Object next();

	public IterData newIterData();
	
}
