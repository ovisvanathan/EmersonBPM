package com.emerson.bpm.dsl;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.Evaluator;
import com.picasso.paddle.tasks.util.ProxyFactoryBean;

public interface FieldProviderFactory<T extends FieldProvider, R extends Evaluator>  extends ProxyFactoryBean {

	public ClauseComparator getComparator(FieldProvider fp);

	@Override
	public R get(Object fieldProvider);
			
	//	if(fp instanceof FieldNameChecker)	
	//		return getFieldNameComparator(fp.getFieldName1(), fp.getFieldName2());			
			
//		return null;
//	}

	/*
	default public ClauseComparator getFieldNameComparator(String fieldName1, String fieldName2) {

			Class fieldClass = FieldNameComparator.class;
			
			ClauseComparator o = null;
			try {
				Constructor<?> cons = fieldClass.getConstructor(String.class, String.class);
				
				o = (ClauseComparator) cons.newInstance(fieldName1, fieldName2);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
			
			return o;
		
	}
	*/
	
}
