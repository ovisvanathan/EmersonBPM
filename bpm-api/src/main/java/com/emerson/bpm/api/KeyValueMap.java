package com.emerson.bpm.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public interface KeyValueMap<K,V> {

	void 	clear();
			
	boolean 	containsKey(Object key);
	
	boolean 	containsValue(Object value);
	
	Set<Map.Entry<K,V>> 	entrySet();
	
	boolean 	equals(Object o);
		
	V 	get(Object key);
		
	int 	hashCode();
	
	boolean 	isEmpty();
	
	Set<K> 	keySet();
		
	V 	put(K key, V value);
	
	void 	putAll(Map<? extends K,? extends V> m);
	
	default V 	putIfAbsent(K key, V value)  { return null; }
	
	default boolean remove(Object key)  { return false; }
	
	default boolean 	remove(Object key, Object value) { return true; }
	
	default V 	replace(K key, V value)  { return null; }
	
	default boolean 	replace(K key, V oldValue, V newValue) { return false; }
		
	default int 	size() { return 0; }

	default Collection<V> 	values()  { return null; }
	
}