/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author salaboy
 */
public class BetaMemory {
//    private List<LeftTuple>   leftTupleMemory = new ArrayList<LeftTuple>();
    private List<RightTuple>  rightTupleMemory = new ArrayList<RightTuple>();

    public BetaMemory() {
    }
    

    /*
    public void addLeftTuple(LeftTuple tuple){
        leftTupleMemory.add(tuple);
    }

    public List<LeftTuple> getLeftTupleMemory() {
        return leftTupleMemory;
    }
    */

    public void addRightTuple(RightTuple tuple){
        rightTupleMemory.add(tuple);
    }


    public List<RightTuple> getRightTupleMemory() {
        return rightTupleMemory;
    }
    
    
}
