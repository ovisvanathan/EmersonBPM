/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import java.util.List;

/**
 *
 * @author salaboy
 */
public interface Tuple{    
    
    Handle get(int pattern);

    //FactHandle get(Declaration declaration);

    List<Handle> getFactHandles();

    int size();

	char[] getResult();

}
