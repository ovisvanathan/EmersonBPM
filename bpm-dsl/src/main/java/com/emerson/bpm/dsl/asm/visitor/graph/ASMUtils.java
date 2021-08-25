package com.emerson.bpm.dsl.asm.visitor.graph;

public class ASMUtils {

	public static Iterable iterableFromSource(String parentType, String fieldLabel, String token) {
		return iterableFromSource(parentType, fieldLabel, token, false);
	}

	public static Iterable iterableFromSource(String parentType, String fieldLabel, String token, boolean isIterable) {

		return (isIterable)?
				new SchemaAwareIterable(
						new IterableSource(parentType, fieldLabel, token, isIterable))
					:
				new IgnorableIterable();								
	}

}
