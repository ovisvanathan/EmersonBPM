package com.emerson.bpm.nodes.match;

public interface Evaluator2<E> {

	boolean evaluate(E a, E b) throws Exception;

}
