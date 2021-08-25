package com.emerson.bpm.nodes.react;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import com.emerson.bpm.api.AutonomousActor;

public class PrototypeMap<K, V> extends AbstractMap<K, V>  implements AutonomousActor {
	
	Map.Entry<K, V> entry;
	
	public PrototypeMap(int i, String name) {
		entry = new SimpleEntry(i, name);		
	}

	public String getName() {
		return (String) entry.getValue();
	}

	public void setName(String title) {
		entry.setValue((V) title);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {

		return null;
	}

	@Override
	public Object getValue() {
		return entry.getValue();
	}

	@Override
	public void setValue(Object val) {
		entry.setValue((V) val);
	}
	
	public Object getKey() {
		return entry.getKey();
	}

	
}
