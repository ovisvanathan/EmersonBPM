package com.emerson.bpm.rule.parser.entity;

import java.lang.annotation.Annotation;

import javax.persistence.Entity;

public class RuleEntity<T> implements Entity {
	
	String name;
	T item;
	
	public RuleEntity(String name, T item) {
		this.name = name;
		this.item = item;
	}

	@Override
	public Class<? extends Annotation> annotationType() {

		return null;
	}

	@Override
	public String name() {
		return this.name;
	}

}
