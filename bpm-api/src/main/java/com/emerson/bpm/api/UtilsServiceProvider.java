package com.emerson.bpm.api;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.Predicate;

import com.emerson.bpm.sql.QbSQLFactory;
import com.emerson.bpm.sql.QbSQLQueryData;
import com.emerson.bpm.sql.QbSQLSelect;

public interface UtilsServiceProvider extends ServiceProvider {

	Map<String, Class> doFieldSearch(Object[] objects, Object[] fieldNames) throws Exception;

	List<Class> doSearchForRootNode(SDKNode[] sdkNodes);

	Object getClassInstance(Class criteriaClass);

	String stripBean(String fieldName1);

	Locale getCurrentLocale();

	Object replaceStrings(String result);

	Class[] toTypeArray(String[] strings) throws ClassNotFoundException;

	Object toTypeNameArray(Class[] paramTypes);

	Object strToType(String e);

	String nextVar();

	Predicate getPredicate(String prefix, String objectName, SDKNode rtmNode, String suffix) throws Exception;

	FactHandle getObjectNodeForClassNode(SDKNode node);

	Map convertPojosToMap(Object[] pojos);

}
