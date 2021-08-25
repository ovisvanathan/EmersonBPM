package com.emerson.bpm.util;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.Principal;

public class MultiCallableTask<T extends AutonomousActor> implements CallableTask<T> {

	T data;
	Principal p;
	
	public MultiCallableTask(Principal p, T data) {
		this.data = data;
		this.p = p;
	}


	@Override
	public T call() throws Exception {				

		p.getExecutorMulti().submit(this.data);
		return null;
	
	}
		
}

