package com.emerson.bpm.nodes.match;


import java.util.Comparator;
import java.util.Date;

import com.emerson.bpm.api.ClauseComparator;
import com.picasso.paddle.annotation.Component;

@Component
public class RangeFieldValueComparator implements ClauseComparator {

	private Class fieldClass;

	private String fieldName;

	private Object value1;
	
	public Object getValue1() {
		return value1;
	}

	public void setValue1(Object value1) {
		this.value1 = value1;
	}

	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}

	private Object value2;
	
	Comparator comp;
	
	enum POSSIBLE_TYPES {	
		STRING,
		INT,
		DOUBLE,
		SHORT,
		LONG,
		FLOAT,
		BYTE,
		BOOLEAN,
		DATE		
	};
	
	POSSIBLE_TYPES definiteType;
	
	public Class getFieldClass() {
		return fieldClass;
	}

	public void setFieldClass(Class fieldClass) {
		this.fieldClass = fieldClass;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public RangeFieldValueComparator(Class fieldClass, String fieldName, Object value1, Object value2) {
		this.fieldClass = fieldClass;
		this.fieldName = fieldName;    				
		this.value1 = value1;
		this.value2 = value2;		
	}

	public RangeFieldValueComparator(String fieldName, Object value1, Object value2, Comparator comp) throws RangeException {
		this.fieldName = fieldName;    				
		this.value1 = value1;
		this.value2 = value2;		
		this.comp = comp;
		
		checkType();
	}

	public RangeFieldValueComparator(String fieldName, Object value1, Object value2) throws RangeException {
		this.fieldName = fieldName;    				
		this.value1 = value1;
		this.value2 = value2;		
		
		checkType();
	}

	private void checkType() throws RangeException {

		Class typeClass = this.value1.getClass();
				
		if(typeClass != this.value2.getClass())
			throw new RangeException("Datatypes do not match");
		
		// either a field name "custName, acNum" etc or a literal string e.g "Pluto"
		if(typeClass == String.class) {

		//	j1 = new JoinNode(left, right, cmp, leftFieldName, rightFieldName);			
			definiteType = POSSIBLE_TYPES.STRING;
			
		} else if(typeClass == int.class || typeClass == Integer.TYPE) {

		//	j1 = new JoinNodeInt(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.INT;
			
		} else if(typeClass == double.class || typeClass == Double.TYPE) {

		//	j1 = new JoinNodeDouble(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.DOUBLE;
			
		} else if(typeClass == long.class || typeClass == Long.TYPE) {
			
		//	j1 = new JoinNodeLong(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.LONG;
			
		} else if(typeClass == short.class || typeClass == Short.TYPE) {

		//	j1 = new JoinNodeShort(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.SHORT;
			
		} else if(typeClass == float.class || typeClass == Float.TYPE) {

		//	j1 = new JoinNodeFloat(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.FLOAT;

		} else if(typeClass == boolean.class || typeClass == Boolean.TYPE) {

		//	j1 = new JoinNodeBool(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.BOOLEAN;

		} else if(typeClass == byte.class || typeClass == Byte.TYPE) {
			
		//	j1 = new JoinNodeByte(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.BYTE;
			
		} else if(typeClass == Date.class) {

		//	j1 = new JoinNodeDate(left, right, cmp, leftFieldName, rightFieldName);
			definiteType = POSSIBLE_TYPES.DATE;

		}

	}

	public int fieldCompare(Class leftClass, String leftFieldName, Class rightClass, String rightFieldName) {
		return 0;
	}

//	@Override
//	public Operator getOperator() {
		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public boolean isTemporal() {
		// TODO Auto-generated method stub
		return false;
	}
}

