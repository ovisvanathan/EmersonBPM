/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.nodes.react.ReactiveNode;

/**
 *
 * @author salaboy
 */
public class EmptyBetaConstraints extends ReactiveNode implements BetaConstraints  {

    public Object getField() {
        return null;
    }

    public Object getRestriction() {
       return null; 
    }

    public COMPARATOR getComparator() {
        return null;
    }

	@Override
	public void addChild(SDKNode aplNode) {

		
	}

	@Override
	public void registerObservers() {

		
	}
    
}
