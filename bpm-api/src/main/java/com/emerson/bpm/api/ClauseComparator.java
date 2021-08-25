package com.emerson.bpm.api;

import java.util.Collection;

import javax.el.ELContext;

import org.kie.api.runtime.rule.Operator;

import com.emerson.bpm.sql.QbSQLField;
import com.emerson.bpm.sql.QbSQLQueryData;
import com.emerson.bpm.sql.QbWhereOperator;

public interface ClauseComparator extends Evaluator {

	public final String EVALUATE = "evaluate";

	default boolean evaluate(Object e) throws Exception { return true; }

	default boolean evaluate(Collection fieldVal, Object valueToCheck) { return false; }

	default void setStringMode(boolean b) {}

	default void copyValues(SDKNode reactiveNode) throws Exception {}

	default void setSequenceSet(OrderedSet sequenceSet) {}

	default OrderedSet getSequenceSet()  { return null; }

	default COMPARATOR get() { return null; }

	default Object [] getFieldNames() { return null; }

	default Object[] getWhereArgs() { return null; }

	default QbSQLField qbfield() { return null; }

	default QbWhereOperator qbwhere() { return null; }

	default Object qbvalue() { return null; }

	default public Operator getOperator() { return null; }

	default public boolean isTemporal() { return false; }

	default int getValue() { return 0; }

	default void setQueryData(QbSQLQueryData queryData) {}

	default void setBindData(ELContext data) {}
	
}
