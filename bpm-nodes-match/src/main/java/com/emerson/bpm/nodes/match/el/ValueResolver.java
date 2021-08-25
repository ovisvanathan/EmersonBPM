package com.emerson.bpm.nodes.match.el;

import java.beans.FeatureDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;

import com.emerson.bpm.nodes.match.el.util.ELUtils;

public class ValueResolver extends  ELResolver {

		Map<String, Object>  varobjs = new HashMap();
	@Override
	public Object getValue(ELContext context, Object base, Object property) {						
			return resolveValue(base, property);
	}

	private Object resolveValue(Object base, Object property) {

		if(base == null) {
			return this.varobjs.get(property);		
		} else {
			
			Object result = null;
			try {
				Method m = ELUtils.findGetMethod(base.getClass(), (String) property);
				
				result = m.invoke(base, null);
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
			
			return result;
		}
	
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {			
		varobjs.put((String) property,  value);
		context.setPropertyResolved(true);
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		// TODO Auto-generated method stub
		return null;
	}

}
