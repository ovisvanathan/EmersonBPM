package com.emerson.bpm.rule.parser.sax;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;

public class Payload<T> implements Entity {

	String name;
	
	T item;

	public Payload(String name2, T item2) {
		this.name = name2;
		this.item = item2;
	}

	public T getItem() {
		return item;
	}


	public void setItem(T item) {
		this.item = item;
	}


	@Override
	public String name() {

		return null;
	}


	@Override
	public Class<? extends Annotation> annotationType() {

		return null;
	}
}
