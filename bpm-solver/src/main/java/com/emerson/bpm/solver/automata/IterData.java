package com.emerson.bpm.solver.automata;

/*
 * Although class is called IterData, it is the control variable
 * on which the state of the system depends on. It contains only
 * the variables that cannot be deduced easily without violating encapsulation
 * etc. All other parameters are calculated from the value of the
 * control variable.
 */
public class IterData {
	
	int cellNum;
	
	int itemIndex;
	
	boolean isDup;
	
	int dupIndex;
	
}
