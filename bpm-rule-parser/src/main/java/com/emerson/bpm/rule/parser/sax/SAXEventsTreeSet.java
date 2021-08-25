package com.emerson.bpm.rule.parser.sax;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;

import javax.persistence.Entity;

public class SAXEventsTreeSet<T > implements PropertyChangeListener {
	
	HashSet treeset = new HashSet();
	RuleItem ruleItem = new RuleItem();
	
	public 	SAXEventsTreeSet() {
		ruleItem.addPropertyChangeListener(this);
	}
	
	public void add(String name, T item) {	

		ruleItem.setProperty(wrap(name, item));
	}
	
	private Entity wrap(String name, T item) {
		return new Payload(name, item);	
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		
	}
	
	
}
