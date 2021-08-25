package com.emerson.bpm.nodes.match;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.nodes.match.sql.QbField;
import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.sql.QbWhereOperator;
import com.emerson.bpm.sql.WhereInfo;
import com.picasso.paddle.annotation.Component;


/*
 * 
 */
@Component
public class FieldListValueComparator extends DefaultComparator {

	COMPARATOR cmp;
		
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
		
	public FieldListValueComparator(String fieldName, Object value, COMPARATOR cmp) {
		super(cmp);
		this.fieldNames = new String [] { fieldName };    				
		this.whereArgs = new Object [] { fieldName, cmp, value };
		this.cmp = cmp;
	}

	public FieldListValueComparator(String fieldName, Object value) {
		super(COMPARATOR.CONTAINS);
		this.fieldNames = new String [] { fieldName };    				
		this.whereArgs = new Object [] { fieldName, cmp, value };		
	}

	@Override
	public boolean evaluate(Object o) throws Exception {
				
		evaluateWithMagic(cmp, new ClassFieldNameMatcher() {
			
			@Override
			public boolean evaluate(Object fieldObj, String fieldName, Object valueToCheck) throws ValueNotSetException {

				Method getter = ELUtils.findGetMethod(fieldObj, fieldName);

				Object fieldVal = null;
				try {
					fieldVal = getter.invoke(fieldObj, null);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fieldVal instanceof Collection) {
				//	if(cmp != null) 
				//		return ((java.util.Collection) fieldVal).contains(valueToCheck);
				//		return ((java.util.Collection) fieldVal).contains(valueToCheck);
				//	else {
						Collection c = (Collection) fieldVal;
						return c.contains(valueToCheck);				
				//	}
				}

				return false;
			}
		});
		
		return false;
	}
	
	private boolean evaluateWithMagic(COMPARATOR comp, NameMatcher m) throws Exception {
	
		this.queryTables = this.queryData.getTables();
		
		this.queryFields = this.queryData.getFieldNames();
	
		QbWhere whr = (QbWhere) this.queryData.getWhere();

		List<WhereInfo> winfos = whr.getWhereInfos();

		List<Object []> slots = whr.getWherePlaces();
		
		WhereInfo winfo = winfos.get(0);
		Object [] fieldVals = slots.get(0);
			
		QbField qbfield = (QbField) winfo.getField();
		QbWhereOperator oper = winfo.getWhereOperator();		
		Object value = fieldVals[2];

		return m.evaluate(this.queryTables[0],  qbfield.toString(), value);					
		
	}

	
	
}

