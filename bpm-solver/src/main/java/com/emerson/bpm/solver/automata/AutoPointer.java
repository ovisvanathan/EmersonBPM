package com.emerson.bpm.solver.automata;

public class AutoPointer {
	
	int cellNo;
	
	int currPtr;
	
	int cycleCount;

	public int getCurrPtr() {
		return currPtr;
	}

	public void setCurrPtr(int currPtr) {
		this.currPtr = currPtr;
	}

	public int getCycleCount() {
		return cycleCount;
	}

	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}

	public AutoPointer(int cellNo, int currPtr, int cycleCount) {
		this.cellNo = cellNo;
		this.currPtr = currPtr;
		this.cycleCount = cycleCount;		
	}
}
