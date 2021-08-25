package com.emerson.bpm.solver.automata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Manifest {

	List<Method> methods = new ArrayList<Method>();

	public Map<String, Object> getMMap() {
		return mmap;
	}

	public void setMmap(Map<String, Object> mmap) {
		this.mmap = mmap;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	Map<String, Object> mmap;
	
	public void setClassName(String name) {
		// TODO Auto-generated method stub
		
	}

	public void setNumMethods(int length) {
		// TODO Auto-generated method stub
		
	}

	public List<Method> getMethods() {
		// TODO Auto-generated method stub
		return methods;
	}

	public void setMethodMap(Map<String, Object> mmap) {
		this.mmap = mmap;
	}

}
