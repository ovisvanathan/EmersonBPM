package com.emerson.bpm.action;

import org.joda.time.DateTime;

import com.emerson.bpm.model.Action;
import com.emerson.bpm.subscribe.ObserverEx;

public class RulesAction<T> implements Action, ObserverEx<T> {

	public final static int __CURRVAL__ = 1;

	public enum ActionType {
		ADD,
		MINUS,		
		MULTIPLY,		
		DIVIDE
	};	
		
	public static final String __DEFAULT = "monarchjbpm.defaultpkg";
	
	public enum ELABSTRATEGY {
		REDUCE, SELECT
	}
	
	protected Class klazz;
	
	protected String packageName;

	protected String fieldName;

	protected Class fieldType;

	protected Object fieldVal;
	
	protected TimeTag timeTag = new TimeTag();

	T currVal;
	
	T lastVal;

	public T getCurrVal() {
		return currVal;
	}

	public void setCurrVal(T currVal) {
		this.currVal = currVal;
	}

	public T getNewVal() {
		return newVal;
	}

	public void setNewVal(T newVal) {
		this.newVal = newVal;
	}


	T newVal;
	ActionType actionType;
	
	public RulesAction(Class klazz, String field, T newVal) {
		this.klazz = klazz;
		this.fieldName = field;
		this.newVal = newVal;	
	}
		
	public RulesAction() {
		// TODO Auto-generated constructor stub
	}

	public Class getKlazz() {
		return klazz;
	}

	public void setKlazz(Class klass) {
		this.klazz = klass;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public void execute() {

		if(timeTag.isFirst() || timeTag.isNew()) {			
			execute();
		} else
			System.out.println("sale action exception for klazz = " + klazz.getName() + " val = " + newVal);

	}
	
	
	public class TimeTag {

		DateTime tagTime;
		
		Object itemStr;
		
		int count;
		
		TimeTag tago;
		
		public TimeTag() {
			
		}
	
		public boolean isFirst() {

			if(count == 0) {
				this.tagTime = new DateTime();
				this.itemStr = newVal.toString();			
				return true;			
			}
			return false;
		}
			
		public boolean isNew() {		
			
			DateTime currTime = new DateTime();			
			if(currTime.isAfter(tagTime)) {
				
				if(!newVal.toString().equals(itemStr))
					return true;
			}

			return false;
		}
		
		class DefaultTimeTag extends TimeTag {
			
		}
		
	}


	@Override
	public void run() {

		
	}


	
}
