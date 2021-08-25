package com.emerson.bpm.api;

import java.util.Comparator;
import java.util.Set;

/**
 * 
 */
public interface OrderedSet extends Set {

	void sort(Comparator c);

	void setEvaluator(Object evaluator);

	void bind(ClauseComparator cmp);

	boolean accept(Object value) throws Exception;

    public Object getEvaluator();

}