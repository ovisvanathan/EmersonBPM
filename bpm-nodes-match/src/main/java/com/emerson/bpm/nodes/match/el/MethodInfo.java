package com.emerson.bpm.nodes.match.el;

public class MethodInfo {
	
	String name;
	Class<?> returnType;
	Class<?>[] parameterTypes;
	
	public MethodInfo(String name, Class<?> returnType, Class<?>[] parameterTypes) {
		this.name = name;
		this.returnType = returnType;
		this.parameterTypes = parameterTypes;
	}

}
