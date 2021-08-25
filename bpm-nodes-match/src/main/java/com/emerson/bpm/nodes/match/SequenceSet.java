package com.emerson.bpm.nodes.match;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.Evaluator;
import com.emerson.bpm.api.OrderedSet;
import com.emerson.bpm.api.SetFilter;
import com.emerson.bpm.dsl.REL;

/**
 * 
 */
public class SequenceSet<T, E extends Evaluator> extends LinkedSequenceSet<T> implements OrderedSet, SetFilter {

	T node;
	
	protected Evaluator evaluator;
	
	REL relation;
	
	Set history = (Set) new HashSet();
	
    public E getEvaluator() {
		return (E) evaluator;
	}

    @Override
	public void setEvaluator(Object evaluator) {
		this.evaluator = (E) evaluator;
	}

	/**
     * Default constructor
     */
    public SequenceSet() {
    }
	
    /**
     * Default constructor
     */
    public SequenceSet(T item) {
    	this.node = item;
    }

    public REL getRelation() {
		return relation;
	}

	public void setRelation(REL relation) {
		this.relation = relation;
	}

	public T getNode() {
		return node;
	}

	public SequenceSet(T item, REL relation) {
    	this.node = item;
    	this.relation = relation;
    }

    /*
    public SequenceSet(int n, E evaluator) {
    	this.item = item;
    	this.evaluator = evaluator;
    }
    */

	public boolean accept(Object e) throws Exception {
		System.out.println("seq accept");
		
	//	if(!e.toString().equals("Heebeebs"))
	//		return false;
	
		boolean b = evaluator.evaluate(e);
	
		System.out.println("seq accept returns " + b);
		
		return b;
	}

	@Override
	public void sort(Comparator c) {
		
	}

	public Set getHistory() {
		return history;
	}

	public boolean ready() {
		return true;
	}

	public void bind(ClauseComparator defaultComparator) {
		this.evaluator = (E) defaultComparator;
	}


}