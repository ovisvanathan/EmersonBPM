package com.emerson.bpm.nodes.match;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.nodes.match.el.util.ELUtils;

public class FieldJoinComparatorX<T>  extends DefaultComparator {
	
	public FieldJoinComparatorX() {
		super(COMPARATOR.EXISTS);
	}

	public FieldJoinComparatorX(COMPARATOR cmp) {
		super(cmp);
	}

	/* Makes a Join of 2 tables over fields s1 snd s2 */
	public FieldJoinComparatorX(String s1, String s2) {
		super(s1, s2);
		this.cmp = COMPARATOR.EQUALS;
	}

	
	@Override
	public boolean evaluate(Object o)  throws Exception  {
		
		System.out.println("comp eval o = " + o.getClass().getName());
	
		if(this.cmp == COMPARATOR.EXISTS)
			return true;
				
		evaluateWithMagic(new ClassFieldNameMatcher()  {
			
			@Override
			public boolean evaluate(Object fobj1, Object fobj2, 
											String leftFieldName, String rightFieldName) throws Exception {

				System.out.println("comp eval 1 ");

				Method getter1 = ELUtils.findGetMethod(queryTables[0], queryFields[0]);
				
				Method getter2 = ELUtils.findGetMethod(queryTables[1], queryFields[1]);
				
				Object leftFieldVal = null;
				Object rightFieldVal = null;
				try {
					leftFieldVal = getter1.invoke(queryTables[0], null);
					rightFieldVal = getter2.invoke(queryTables[1], null);
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
				
	
				return TYPEID.evaluate(leftFieldVal, rightFieldVal, COMPARATOR.EQUALS);
			}
		});
		
		return false;
	}
	
	private boolean evaluateWithMagic(NameMatcher m)  throws Exception  {
	
		this.queryTables = this.queryData.getTables();
	
		this.queryFields = this.queryData.getFieldNames();
	
		return m.evaluate(queryTables[0], queryTables[1], queryFields[0], queryFields[1]);
		
	}
	
}
