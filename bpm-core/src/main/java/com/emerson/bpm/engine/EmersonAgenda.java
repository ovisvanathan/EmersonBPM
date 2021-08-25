package com.emerson.bpm.engine;

import com.emerson.bpm.api.Agenda;
import com.emerson.bpm.api.Session;

public class EmersonAgenda implements Runnable, Agenda {

	Session session;
	
	public Session getSession() {
		return session;
	}

	public EmersonAgenda(Session sess) {
		this.session = sess;
	}

	@Override
	public void run() {

		
			
		
		
	}

	
	
	
	
}
