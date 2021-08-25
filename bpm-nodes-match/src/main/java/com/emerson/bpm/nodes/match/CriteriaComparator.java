package com.emerson.bpm.nodes.match;

import com.emerson.bpm.api.ClauseComparator;

public interface CriteriaComparator extends ClauseComparator {

	boolean evaluate(Object ... args);

}
