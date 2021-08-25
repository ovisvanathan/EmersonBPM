package com.emerson.bpm.dsl.asm.visitor.graph;

import java.util.Iterator;

public interface IteratorGenerator {

	void addIterable(Iterable iterableFromSource);
	
	Iterator getIterator();
}
