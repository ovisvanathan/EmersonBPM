package com.emerson.bpm.solver.automata;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Observable;

public class DataBuf<T>  extends Observable {
	
	T [] databuf;
	
	int [] indices;

	int [] runIndices;

	T [] trimmedValues;
	
	
	/*
	 * The dataBuf array contains unequals size tokens. e.g a, af, abc, abcde etc
	 * in fixed width cells.
	 * 
	 * Before we can process this we need to add them to array where each item 
	 * occupies only the space it needs
	 * 
	 * 0=[a], 1=[a,f], 2=[abc], 3=[abcde] and so on
	 */
	@SuppressWarnings("unchecked")
	public T getTrimmedValue() {
		
		Class klazz = FSM.getNodeClass();
	//	trimmedValues = (T []) Array.newInstance(klazz, databuf.length);
		
		trimmedValues = (T[]) Arrays.stream(databuf)
                .filter(s -> (s != null))
                .toArray(sz -> (T[]) Array.newInstance(klazz, sz));    
		
		String s2 = String.join(", ", Arrays.toString(databuf));
		
		return (T) s2;
	}
	
	public void setTrimmedValues(T[] trimmedValues) {
		this.trimmedValues = trimmedValues;
	}

	public int[] getRunIndices() {
		return runIndices;
	}

	public void setRunIndices(int[] runIndices) {
		this.runIndices = runIndices;
	}

	int [] dupIndices;
	
	int activeCellNum;

	int activeIndex;
	
	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		
		 synchronized (this) {
			 this.activeIndex = activeIndex;
		 }
		
		 setChanged();
		 notifyObservers();
		
	}

	public T [] getDatabuf() {
		return databuf;
	}

	public T getData() {
			return getTrimmedValue();
	}

	public void setDatabuf(T[] databuf) {
		this.databuf = databuf;
	}

	public int[] getIndices() {
		return indices;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	public int[] getDupIndices() {
		return dupIndices;
	}

	public void setDupIndices(int[] dupIndices) {
		this.dupIndices = dupIndices;
	}

	public int getActiveCellNum() {
		return activeCellNum;
	}

	public void setActiveCellNum(int activeCellNum) {
		this.activeCellNum = activeCellNum;
	}
	
	
	

}
