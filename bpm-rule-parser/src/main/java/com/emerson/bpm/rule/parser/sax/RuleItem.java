package com.emerson.bpm.rule.parser.sax;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.persistence.Entity;


public class RuleItem<T extends Entity> {
	
	T item;

	Vector<PropertyChangeListener> listeners = new Vector<>();
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.add(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.remove(listeners.indexOf(l));
	}
	
	public T getProperty() {
		return this.item;
	}
	
	public void setProperty(T item) {
		this.item = item;
		
		firePropertyChange(
					new PropertyChangeEvent(
							this, item.name(), null, item));
		
	}

	private void firePropertyChange(PropertyChangeEvent propertyChangeEvent) {
		
		for(PropertyChangeListener l : listeners)
				l.propertyChange(propertyChangeEvent);
		
	}

}
