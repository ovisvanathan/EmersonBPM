package com.emerson.bpm.api;

public interface ValueProvider<T> {

	public T getValue();

	public void setValue(T val);

}
