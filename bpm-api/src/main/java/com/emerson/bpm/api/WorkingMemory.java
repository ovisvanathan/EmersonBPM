package com.emerson.bpm.api;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.Predicate;

import com.emerson.bpm.model.Person;

public interface WorkingMemory {

	List getPredicates();

	Collection<Predicate> getPredicatesCollection();

	Topology getRete();

	int fireAllRules();

	void insert(Object amal);

}
