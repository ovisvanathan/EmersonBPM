package com.emerson.bpm.dsl;

import com.emerson.bpm.api.COMPARATOR;
import com.picasso.paddle.tasks.util.FactoryGetMethod;

public interface FieldProvider extends FactoryGetMethod {

	default String getFieldName1() { return null; }

	default String getFieldName2() { return null; }

	default Object getFieldVal() { return null; }

	default COMPARATOR getComparator() { return null; }

	default String [] getFieldNames() { return null; }

	default Object [] getFieldValues() { return null; }

}
