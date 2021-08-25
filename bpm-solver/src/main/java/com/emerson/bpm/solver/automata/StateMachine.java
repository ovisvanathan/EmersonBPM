package com.emerson.bpm.solver.automata;

public interface StateMachine<T> {

	boolean MODE_SINGLE = false;
	boolean MODE_BATCH = false;
	int BATCH_SIZE_DEFAULT = 5;

	public void start(int s, int n2);

	public void end();
	
	public void setState(FSM.State s);

	void progress();
	
}
