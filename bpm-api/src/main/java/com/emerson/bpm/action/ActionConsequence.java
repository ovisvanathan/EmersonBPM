package com.emerson.bpm.action;

import com.emerson.bpm.api.WorkingMemory;

public class ActionConsequence implements Consequence {
	String id;

	RulesAction action;
	WorkingMemory wm;
	
	public ActionConsequence(WorkingMemory wm, String id) {
		this.id = id;
		this.wm = wm;
	}
	
	public ActionConsequence() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setAction(RulesAction action) {
		this.action = action;
	}

	@Override
	public void execute() {
		this.action.execute();
	}

	/*
	@Override
	public RulesAction newUpdateAction(Class<Miles> class1, String string, int currval, int numMiles) {

		//	assert clazz.name == 'Foo'                                                  
		RulesAction o = null;
		try {

			if(updateActionClass == null) {
			
				updateActionClass = gcl.loadClass(" com.monarch.v2.react.action.UpdateAction.groovy");
				
				o = (RulesAction) updateActionClass.newInstance();
	
				o.setClassName(class1.getName());
				o.setFieldName(string);
				o.setCurrentVal(currval);

			}
		} catch (CompilationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		return o;
	}
	*/

}
