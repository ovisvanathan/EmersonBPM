package com.emerson.bpm.nodes.match;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.inject.Inject;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.picasso.paddle.annotation.Component;

@Component
public class DoesNotExistsComparator extends DefaultComparator {
	
	SDKNode cc2;
	
	String fieldName1;
	String fieldName2;
	
	Object fieldObject1;
	Object fieldObject2;
	
	@Inject
	Map<Class, Method[]> beanGettersMap;

	public DoesNotExistsComparator(SDKNode cc2, String s1, String s2) {
		super(COMPARATOR.EQUALS);
		this.cc2 = cc2;
		this.fieldName1 = s1;				
		this.fieldName2 = s2;								
	
		//TODO STRIPBEAN by ExpressionResolver
		 new Thread( () -> {			 
			 EmersonUtils.stripBean(this.fieldName1);
			 EmersonUtils.stripBean(this.fieldName2);
			 
		 }).start();
	}

	public DoesNotExistsComparator(String s1, String s2) {
		super(COMPARATOR.EQUALS);
		this.fieldName1 = s1;				
		this.fieldName2 = s2;								
	
		 new Thread( () -> {			 
			 EmersonUtils.stripBean(this.fieldName1);
			 EmersonUtils.stripBean(this.fieldName2);
			 
		 }).start();
	}

	@Override
	public boolean evaluate(Object o) throws Exception {
		
		SDKNode otpNode = cc2.getParent();
		
		Object bean = otpNode.getObject();
		
		Method getter = ELUtils.findGetMethod(bean, fieldName2);
		Object result2 = null;
		try {
			result2 = getter.invoke(bean, null);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//for loan appln
	
		Method getter2 = ELUtils.findGetMethod(fieldObject1, fieldName1);
		
		Object fieldVal = null;
		try {
			fieldVal = getter2.invoke(fieldObject1, null);
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
						
		return !TYPEID.evaluate(result2, fieldVal, COMPARATOR.EQUALS);
		
		
	}
}
