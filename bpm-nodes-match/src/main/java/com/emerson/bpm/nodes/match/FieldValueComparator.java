package com.emerson.bpm.nodes.match;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.nodes.match.sql.QbField;
import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.sql.QbWhereOperator;
import com.emerson.bpm.sql.WhereInfo;
import com.picasso.paddle.annotation.Component;

@Component
public class FieldValueComparator extends  DefaultComparator {
		
	//List<Object []> whereOps;
	
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

	public FieldValueComparator(COMPARATOR cmp) {
		super(cmp);
		this.cmp = cmp;		
	}
	
	public FieldValueComparator(String fieldName, Object value, COMPARATOR cmp) {
		super(cmp);
		this.cmp = cmp;	
		this.fieldNames = new String [] { fieldName };
		this.whereArgs = new Object [] { 
								fieldName, this, value };
	}	
	
	public FieldValueComparator(Object[] whereClause) {
		super(COMPARATOR.MANY);
		this.whereArgs = whereClause;
	}

	@Override
	public boolean evaluate(Object o) throws Exception {
				
		evaluateWithMagic(new ClassFieldNameMatcher() {
			
			@Override
			public boolean evaluate(Object fieldObj,  String fieldName, Object valueToCheck) throws Exception {
				
				Method getter = ELUtils.findGetMethod(fieldObj, fieldName);
				
				//dont use fieldVal which is valueToCheck
				Object fieldVal1 = null;
				try {
					fieldVal1 = getter.invoke(fieldObj, null);
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
								
				return TYPEID.evaluate(fieldVal1, valueToCheck, cmp);
			}
		});
		
		return false;
	}		
	
	private boolean evaluateWithMagic(NameMatcher m) throws Exception {		
	
		this.queryTables = this.queryData.getTables();
		
		this.queryFields = this.queryData.getFieldNames();
		
		QbWhere whr = (QbWhere) this.queryData.getWhere();

		List<WhereInfo> winfos = whr.getWhereInfos();

		List<Object []> slots = whr.getWherePlaces();
		
		int x = 0;
		boolean eval1 = false;
		for(WhereInfo wi : winfos) {
			
			QbField qbfield = (QbField) wi.getField();
			QbWhereOperator oper = wi.getWhereOperator();		
			Object value = slots.get(x++)[2];

			eval1 &= m.evaluate(this.queryTables[0],  qbfield.toString(), value);					
		}
		
		return eval1;
	}

	/*
	boolean evaluate(Object fval, Object rval, COMPARATOR cmp) {
		try {
			
			this.queryTables = this.queryData.getTables();
			
			this.queryFields = this.queryData.getFieldNames();
		
		
			boolean b = m.evaluate(fval, rval, cmp);		
			return b;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	*/
	

}

