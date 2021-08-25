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

import static org.apache.commons.collections4.functors.TruePredicate.truePredicate;

import java.util.Collection;

import org.apache.commons.collections4.Predicate;

/**
 * Predicate implementation that returns true if all the
 * predicates return true.
 * If the array of predicates is empty, then this predicate returns true.
 * <p>
 * NOTE: In versions prior to 3.2 an array size of zero or one
 * threw an exception.
 * </p>
 *
 * @since 3.0
 */
public final class AllPredicate<T> extends AbstractQuantifierPredicate<T> {

    /** Serial version UID */
    private static final long serialVersionUID = -3094696765038308799L;

    /**
     * Factory to create the predicate.
     * <p>
     * If the array is size zero, the predicate always returns true.
     * If the array is size one, then that predicate is returned.
     *
     * @param <T> the type that the predicate queries
     * @param predicates  the predicates to check, cloned, not null
     * @return the {@code all} predicate
     * @throws NullPointerException if the predicates array is null
     * @throws NullPointerException if any predicate in the array is null
     */
    public static <T> Predicate<T> allPredicate(final Predicate<? super T>... predicates) {
        validate(predicates);
        if (predicates.length == 0) {
            return truePredicate();
        }
        if (predicates.length == 1) {
            return coerce(predicates[0]);
        }

        return new AllPredicate<>(copy(predicates));
    }

    /**
     * Factory to create the predicate.
     * <p>
     * If the collection is size zero, the predicate always returns true.
     * If the collection is size one, then that predicate is returned.
     *
     * @param <T> the type that the predicate queries
     * @param predicates  the predicates to check, cloned, not null
     * @return the {@code all} predicate
     * @throws NullPointerException if the predicates array is null
     * @throws NullPointerException if any predicate in the array is null
     */
    public static <T> Predicate<T> allPredicate(final Collection<? extends Predicate<? super T>> predicates) {
        final Predicate<? super T>[] preds = validate(predicates);
        if (preds.length == 0) {
            return truePredicate();
        }
        if (preds.length == 1) {
            return coerce(preds[0]);
        }
        return new AllPredicate<>(preds);
    }

    /**
     * Constructor that performs no validation.
     * Use {@code allPredicate} if you want that.
     *
     * @param predicates  the predicates to check, not cloned, not null
     */
    public AllPredicate(final Predicate<? super T>... predicates) {
        super(predicates);
    }

    /**
     * Evaluates the predicate returning true if all predicates return true.
     *
     * Original evaluate. Has a Bug. All predicates evaluated against
     * same object incorrectly.
     *
     * @param object  the input object
     * @return true if all decorated predicates return true
     */
    /*
    @Override
    public boolean evaluate(final T object) {
        for (final Predicate<? super T> iPredicate : iPredicates) {
            if (!iPredicate.evaluate(object)) {
                return false;
            }
        }
        return true;
    }
    */
  

    /**
     * Evaluates the predicate returning true if all predicates return true.
     *
     * Evaluates compound predicate 
     * Input args must match Predicate args
     * 
     * Simple case is 1 param for each arg
     * if each predicate itself is a compound predicate, then as many args as needed
     * in sequence.
     * 
     * ex. if predicate A needs 2 args and predicate B needs 3 args then
     * input must be an array of 5 args.
     * 
     *
     * @param object  the input object
     * @return true if all decorated predicates return true
     */
    @Override
    public boolean evaluate(final T arg) {
    	
    	boolean result = true;
    	int k=0;
    	int q=0;
		if(!arg.getClass().isArray()) {		
			Object og = (Object) arg; 
		
			Predicate p0 = iPredicates[0];
			return p0.evaluate(og); 
		
		} else {			
			
			Object [] args = (Object []) arg;			
		
			int x=0;
			for(Predicate px : iPredicates) {
				
				if(px instanceof NArgPredicate) {
					
					NArgPredicate ngx = (NArgPredicate) px;
					
					result = result && ngx.evaluateMulti(args);
									
				} else {
					result = result && px.evaluate(args[x++]);
				}
			}
			
			return result;			
		}		 

    }

    
    /**
     * Evaluates the predicate returning true if all predicates return true.
     *
     * Predicates are chainable. The output of the first becomes the input of the 
     * next and so on. When it comes time to evaluate, only the first input and the last 
     * result need be supplied. The remaining are obtained by evaluating the 
     * predicates in order.
     *
     * @param object  the input object
     * @return true if all decorated predicates return true
     */
//    @Override
    public boolean evaluateChain(T [] arg) {

    	T [] args = (T []) arg;
    	boolean result = false;
    	if(iPredicates.length == 0) {
    		// single predicate. just evaluate
    		Predicate iPredicate = (Predicate) iPredicates[0];
    		if (!iPredicate.evaluate(args[0])) {
                return false;
            }
    		
    		return true;
    	} else {
    		//predicate chain. unwind chain
    		
    		int len = iPredicates.length;
    		int x = 0;

    		Predicate prev = null;
    		Object param = args[0];  			
    		while (x < len) {
    			
    			Predicate iPredicate = (Predicate) iPredicates[x];
    	        			
    			result = result && !iPredicate.evaluate(param);  			
        		if(iPredicate instanceof IdentityPredicate) 
        			param = ((IdentityPredicate)iPredicate).getValue();
    		
        		
        		x++;
    		}
    		
    		return result;
   		
    	}

    }

    public static <T> Predicate<T> coerce(final Predicate<? super T> predicate) {
        return (Predicate<T>) predicate;
    }
    
   
    
    
    
}
