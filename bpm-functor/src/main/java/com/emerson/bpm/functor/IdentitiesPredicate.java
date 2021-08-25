/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emerson.bpm.functor;

import java.io.Serializable;

import org.apache.commons.collections4.Predicate;

/**
 * Predicate implementation that returns true if the input is the same object
 * as the one stored in this predicate.
 *
 * @since 3.0
 */
public class IdentitiesPredicate implements Predicate, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = -89901658494523293L;

    /** The value to compare to */
    private final Object [] iValues;

    private String predicateName;
    
    public String getPredicateName() {
		return predicateName;
	}

	/**
     * Factory to create the identity predicate.
     *
     * @param <T> the type that the predicate queries
     * @param object  the object to compare to
     * @return the predicate
     * @throws Exception 
     */
    public IdentitiesPredicate(final Object ... args) throws Exception {
        if (args == null || args.length < 1) {
        	throw new Exception("args is null");
        } else if (args.length == 1) {
        	throw new Exception("args len is 1. Did you mean to create IdentityPredicate");
        } else
        	this.iValues = args;
    }

    /**
     * Factory to create the identity predicate.
     *
     * @param <T> the type that the predicate queries
     * @param object  the object to compare to
     * @return the predicate
     * @throws Exception 
     */
    public IdentitiesPredicate(String predicateName, final Object ... args) throws Exception {
        if (args == null || args.length < 1) {
        	throw new Exception("args is null");
        } else if (args.length == 1) {
        	throw new Exception("args len is 1. Did you mean to create IdentityPredicate");
        } else
        	this.iValues = args;

        this.predicateName = predicateName;
    }

    /**
     * Evaluates the predicate returning true if the input object is identical to
     * the stored object.
     *
     * @param object  the input object
     * @return true if input is the same object as the stored value
     */
    @Override
    public boolean evaluate(final Object object) {

    	if(object.getClass().isArray()) {
    		Object [] args = (Object []) object;
    	
    		for(Object arg : args) {
    			
    			for(Object val : iValues) {
    				
    				if(arg != val)
    					return false;
    			}
    			
    		}
    	}

    	return true;
    }

    /**
     * Gets the value.
     *
     * @return the value
     * @since 3.1
     */
    public Object getValue() {
        return iValues;
    }

}
