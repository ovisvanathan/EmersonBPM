package com.emerson.bpm.util;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.Principal;

public class SingleCallableTask<T extends AutonomousActor> implements CallableTask<T> {

	T data;
	Principal p;
	
	public SingleCallableTask(Principal p, T data) {
		this.data = data;
		this.p = p;
	}

	@Override
	public T call() throws Exception {				

		p.getExecutor().submit(this.data);
		return null;
	
	}
		
}
