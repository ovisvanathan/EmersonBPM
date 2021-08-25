package com.emerson.bpm.solver.automata;

import java.util.ArrayList;
import java.util.List;

public class FSMRunner<T> {

	T [] input;
	int n, r;	
	int len;
	int start;
	
	
	
	// Data list 1 for each position from 0 to n-1
	List<T []> listByPosn = new ArrayList();	
	
	T [] mirrotInput;

	int [] stopat;
	
	FSM fsm;
	
	Combinative parent;
	
	public FSMRunner(T [] input, int n, int r, Class klazz, Combinative c) {
		this.input = input;
		this.n = n;
		this.r = r;
		this.parent = c;
		
		fsm = new FSM(input, n, r, klazz, c.allowRepeats());
		
		fsm.addPropertyChangeListener(
				this.parent
				);
					
	
		
	}
			
	public void run2() {
		// a, b, c, d, e, f
		/*
	1	State.init[no data]
			
    1a. set start = 0 get 3 tokens abc
			
	2	State.fillfirst [ abc ]

	3	State.fix12cycle3 [ abd - abh ]
			
	3a. collect 2 and 3
	
	4	State.fix1cycle2and swap [  acd - ahg ]		

	5	State.cycle1 [ bcd - hhg ]
				
	6	State.goto 3

	7. set start = 1 get 3 tokens bcd
	
	8 State.goto 2
		*/
		
	}

	public void run() {		
		fsm.run();
	
	
		fsm.printOutput();
	
	}

	
	

}
