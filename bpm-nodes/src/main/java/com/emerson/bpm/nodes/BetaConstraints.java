/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import com.emerson.bpm.api.COMPARATOR;

/**
 *
 * @author salaboy
 */
public interface BetaConstraints {
    public Object getField();
    public Object getRestriction();
    public COMPARATOR getComparator();
    
}
