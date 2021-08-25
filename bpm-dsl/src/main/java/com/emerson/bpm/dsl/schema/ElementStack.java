package com.emerson.bpm.dsl.schema;

import java.util.Stack;

public class ElementStack<T> {

	Stack<T> userStack;	
	Stack<T> systemStack;	
	
	public ElementStack() {
		userStack = new Stack();
		systemStack = new Stack();
	}
	
	public void push(T elem) {		
		userStack.push(elem);
		systemStack.push(elem);
	}

	public T peek() {		
		return userStack.peek();
	}

	public T pop() {		
		return userStack.pop();
	}
	
	
	public Stack getStack() {
		return systemStack;
	}
	
}
