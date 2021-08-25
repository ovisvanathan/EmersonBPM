/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import com.emerson.bpm.api.WorkingMemory;

/**
 *
 * @author salaboy
 */
public interface ObjectSink extends NetworkNode {
     public void assertObject(Handle factHandle,
                             PropagationContext propagationContext, WorkingMemory wm);
}
