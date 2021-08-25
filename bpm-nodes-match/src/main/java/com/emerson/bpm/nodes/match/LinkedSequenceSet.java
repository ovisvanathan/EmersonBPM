package com.emerson.bpm.nodes.match;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.emerson.bpm.api.KeyValueMap;

public class LinkedSequenceSet<T>  implements Set, KeyValueMap  {

	LinkedHashSet<T> seqset = new LinkedHashSet();
	
	Map setmap;
	
	public LinkedSequenceSet() {}

	@Override
	public int size() {
		return seqset.size();
	}

	@Override
	public boolean isEmpty() {
		return seqset.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return seqset.contains(o);
	}

	@Override
	public Iterator iterator() {
		return seqset.iterator();
	}

	@Override
	public Object[] toArray() {
		return seqset.toArray();
	}

	@Override
	public Object[] toArray(Object[] a) {
		return seqset.toArray(a);
	}

	@Override
	public boolean add(Object e) {
		return seqset.add((T) e);
	}

	@Override
	public boolean remove(Object o) {
		return seqset.remove(o);
	}

	@Override
	public boolean containsAll(Collection c) {
		return seqset.containsAll(c);
	}

	@Override
	public boolean addAll(Collection c) {
		return seqset.addAll(c);
	}

	@Override
	public boolean retainAll(Collection c) {
		return seqset.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection c) {
		return seqset.removeAll(c);
	}

	@Override
	public void clear() {
		seqset.clear();		
	}
	
	//map methods
	public Object 	put(Object key, Object value) {
		return setmap.put(key, value);
	}
	
	public boolean 	containsKey(Object key) {
		return setmap.containsKey(key);
	}
	
	public boolean 	containsValue(Object value) {
		return setmap.containsValue(value);
	}

	@Override
	public Set entrySet() {
		return setmap.entrySet();
	}

	@Override
	public Object get(Object key) {
		return setmap.get(key);
	}

	@Override
	public Set keySet() {
		return setmap.keySet();
	}

	@Override
	public void putAll(Map m) {
		setmap.putAll(m);
	}

	@Override
	public
	Object 	putIfAbsent(Object key, Object value)  { 
		return setmap.putIfAbsent(key, value); 
	}
		
	@Override
	public
	boolean 	remove(Object key, Object value) { 
		return setmap.remove(key, value); 
	}
	
	@Override
	public
	Object 	replace(Object key, Object value)  { 
		return setmap.replace(key, value); 
	}
	
	@Override
	public
	boolean 	replace(Object key, Object oldValue, Object newValue) { 
		return setmap.replace(key, oldValue, newValue); 
	}
		
	@Override
	public
	Collection 	values()  { 
		return setmap.values(); 
	}
	

	
}
