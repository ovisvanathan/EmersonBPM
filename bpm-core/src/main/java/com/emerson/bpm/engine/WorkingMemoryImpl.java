package com.emerson.bpm.engine;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.Predicate;

import com.emerson.bpm.api.Agenda;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.api.WorkingMemory;
import com.emerson.bpm.util.ServiceFactory;

public class WorkingMemoryImpl implements WorkingMemory {
	
	Rete rete;
	
	Set wmes = new HashSet();
	
	Session session;
	
	public WorkingMemoryImpl() {

		session = ServiceFactory.getSession();
		Agenda agenda = new EmersonAgenda(session);	
		this.rete = new Rete(agenda);		
	}
	
	public Set getWmes() {
		return wmes;
	}

	public void setWmes(Set wmes) {
		this.wmes = wmes;
	}

	@Override
	public List getPredicates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Predicate> getPredicatesCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topology getRete() {	
		return this.rete;
	}

	@Override
	public int fireAllRules() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void insert(Object amal) {
		// TODO Auto-generated method stub
		
	}

	
	
}
