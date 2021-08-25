package com.emerson.bpm.solver.automata;

public class Automata<T> {

	T [] data;
	
	int [] indices;
	
	int[] xdups;
	
	public int[] getDupIndices() {
		return xdups;
	}

	public void setDupIndices(int[] xdups) {
		this.xdups = xdups;
	}

	int [] databufIndices;
	
	public int[] getDatabufIndices() {
		return databufIndices;
	}

	public void setDatabufIndices(int[] databufIndices) {
		this.databufIndices = databufIndices;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	int firstIndex;

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	T [] newData;
	
	public T[] getNewData() {
		return newData;
	}

	public void setNewData(T[] newData) {
		this.newData = newData;
	}

	AutoPointer autoPtr;
	
	public AutoPointer getAutoPtr() {
		return autoPtr;
	}

	public void setAutoPtr(AutoPointer autoPtr) {
		this.autoPtr = autoPtr;
	}

	public T [] getData() {
		return data;
	}

	public void setData(T [] t) {
		this.data = t;
	}

	public void setDups(int[] xdups) {
		this.xdups = xdups;	
	}

	
}
