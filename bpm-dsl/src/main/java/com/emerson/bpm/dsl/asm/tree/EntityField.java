package com.emerson.bpm.dsl.asm.tree;

import com.github.javaparser.ast.expr.SimpleName;

public class EntityField {

	String name;
	Class type;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public boolean isAnyMatch() {
		return anyMatch;
	}

	public void setAnyMatch(boolean anyMatch) {
		this.anyMatch = anyMatch;
	}

	boolean anyMatch;
	
	public EntityField(String name, Class<?> type2, boolean anyMatch) {
		this.name = name;
		this.type =type2;
		this.anyMatch = anyMatch;
	}

	
}
