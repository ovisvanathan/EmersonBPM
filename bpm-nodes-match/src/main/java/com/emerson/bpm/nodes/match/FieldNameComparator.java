package com.emerson.bpm.nodes.match;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.util.ServiceFactory;
import com.picasso.paddle.annotation.BeanPackageScan;
import com.picasso.paddle.annotation.Component;

@BeanPackageScan("com.emerson.bpm.model")
@Component("FieldNameChecker")
public class FieldNameComparator<T>  extends DefaultComparator {
	
	String accessField;
	
	UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) ServiceFactory.getUtilsProvider();
	
	public FieldNameComparator() {
		super(COMPARATOR.EXISTS);
	}

	public FieldNameComparator(COMPARATOR cmp) {
		super(cmp);
	}

	/* Makes a Join of 2 tables over fields s1 snd s2 */
	public FieldNameComparator(String s1, String s2) {
		super(s1, s2);
		this.cmp = COMPARATOR.EQUALS;
	}

	public FieldNameComparator(String s1, Object s2, Object value) {
		super(s1, s2);
		this.cmp = COMPARATOR.EQUALS;
	}

	/*
	 * 3 arg constructor. Means join on fields s2 and s3
	 * and fetch field s1
	 */
	public FieldNameComparator(String s1, String s2, String s3) {
		super(s2, s3);
		this.accessField = s1;
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
	
		if(this.queryData == null || this.cmp == COMPARATOR.EXISTS)
			return true;
		
		this.queryTables = this.queryData.getTables();
	
		this.queryFields = this.queryData.getFieldNames();
	
		return m.evaluate(queryTables[0], queryTables[1], queryFields[0], queryFields[1]);
		
	}
	
}
