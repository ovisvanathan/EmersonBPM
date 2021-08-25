package com.emerson.bpm.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.emerson.bpm.action.primitive.ActionPrimitive;
import com.emerson.bpm.action.primitive.UpdateActionPrimitive;
import com.evatic.paddle.api.framework.Framework;


public class UpdateAction<T> extends CreateAction<T> {
		
	ActionPrimitive a;

	public UpdateAction(Class tclass, String pkg, Map fields) {
		super(tclass, pkg, fields);		
	
		a = new UpdateActionPrimitive(actionType);	
	}

	
	public UpdateAction(Class<T> tclass, String field, String fieldVal) {
		super(tclass, tclass.getPackage().getName());
		this.klazz = tclass;
		this.fieldName = field;
		this.fieldVal = fieldVal;	
	}


	public void fieldUpdate(String field, T currval) {
		a = new UpdateActionPrimitive(actionType);	
	}

	@Override
	public void execute() {
		
		/*
		try {			

		
				
				Object tobj = klazz.newInstance();
				
				List mlist = (List) Framework.getterSettersMap.get(klazz.getName() + "." + fieldName);

				Method mgetter = (Method) mlist.get(0);
				
				Object result = mgetter.invoke(tobj, null);

				Method msetter = (Method) mlist.get(1);

				Class [] params = msetter.getParameterTypes();
				
				Object [] vals = new Object[params.length];
								
				vals[0] = a.doAction(currVal, newVal);
				
				msetter.invoke(tobj, vals[0]);

				newVal = (T) vals[0];
				
				
			} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {			
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 		
			*/

		}
	
}
