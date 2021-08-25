/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import java.util.List;

import com.emerson.bpm.nodes.react.ReactiveNode;

/**
 *
 * @author salaboy
 */
public abstract class BetaNode extends ReactiveNode {
     /** The left input <code>TupleSource</code>. */
    //protected LeftTupleSource leftInput;

    /** The right input <code>TupleSource</code>. */
	
	
 //   protected ObjectSource    rightInput;

    protected BetaConstraints constraint;
    
    protected BetaMemory memory;
    
    public BetaNode(BetaConstraints constraint) {
        this.constraint = constraint;
        this.memory = new BetaMemory();        
    }

    public BetaConstraints getConstraint() {
        return constraint;
    }

    public void setConstraint(BetaConstraints constraint) {
        this.constraint = constraint;
    }

    /*
    public LeftTupleSource getLeftInput() {
        return this;
    }

    public ObjectSource getRightInput() {
        return rightInput;
    }

    public void setRightInput(ObjectSource rightInput) {
        this.rightInput = rightInput;
    }
    */

    public BetaMemory getMemory() {
        return memory;
    }
    
    /*
    public List<LeftTuple> getLeftTuples(){
       return getMemory().getLeftTupleMemory();
    }
    */
    
    public List<RightTuple> getRightTuples(){
        return getMemory().getRightTupleMemory();
    }
    
    
}
