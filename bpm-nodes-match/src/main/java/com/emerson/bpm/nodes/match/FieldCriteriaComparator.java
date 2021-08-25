package com.emerson.bpm.nodes.match;

import java.util.Map;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.util.PassthroughInvocationHandler;
import com.picasso.paddle.annotation.Component;

@Component
public class FieldCriteriaComparator<T>  extends DefaultComparator {

	private Class<T> criteriaClass;
	
	String [] args;
	
	Map<Class, Map<String, Map.Entry>> classesMetadataMap;
	
	public FieldCriteriaComparator() {
		super(COMPARATOR.MANY);
	}

	public FieldCriteriaComparator(COMPARATOR cmp) {
		super(COMPARATOR.MANY);
	}

	public FieldCriteriaComparator(String [] args) {
		super(COMPARATOR.MANY);
		this.args = args;
	}
	
	/*
	 * @param args an object [] consisting of name-value pairs e.g name1, value1, name2, value2 etc.
	 */
	public FieldCriteriaComparator(Object[] args) {
		super(COMPARATOR.MANY);
	}

	@Override
	public boolean evaluate(Object o) throws Exception {
				
		evaluateWithMagic(new ClassFieldNameMatcher() {
			
			@Override
			public boolean evaluate(Object [] args) throws ValueNotSetException {
				
				T criteriaClassObject = (T) EmersonUtils.getClassInstance(criteriaClass);
				
			    CriteriaComparator proxy1 = 
					   (CriteriaComparator) 
					   		PassthroughInvocationHandler.proxying(criteriaClassObject, criteriaClass);
			    	
				return proxy1.evaluate(args);				
			}

		});
		
		return false;
	}
	
	private boolean evaluateWithMagic(NameMatcher m) throws Exception {
	
		try {
			return m.evaluate(args);
		} catch (ValueNotSetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
