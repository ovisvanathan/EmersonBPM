package com.emerson.bpm.dsl.asm.visitor.graph;

import java.util.Iterator;

public class SchemaAwareIterable implements Iterable {

	IterableSource source;
	
	public SchemaAwareIterable(String parentType, String fieldLabel, 
			String token) {
				this.source = new IterableSource(parentType, fieldLabel, token, false);
	}
	
	public SchemaAwareIterable(String parentType, String fieldLabel, 
						String token, boolean isIterable) {
		this.source = new IterableSource(parentType, fieldLabel, token, isIterable);
	}

	public SchemaAwareIterable(IterableSource source) {
		this.source = source;
	}

	@Override
	public Iterator iterator() {
		return source.getIterator();
	}

}
