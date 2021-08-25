/**
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the License); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at
 
       httpwww.apache.orglicensesLICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an AS IS BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.emerson.bpm.functor;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;

import com.emerson.bpm.api.Evaluator;

/**  Defines a functor interface implemented by classes that perform a predicate
  test on an object.
  p
  A {@code Predicate} is the object equivalent of an {@code if} statement.
  It uses the input object to return a true or false value, and is often used in
  validation or filtering.
  p
  p
  Standard implementations of common predicates are provided by
  {@link PredicateUtils}. These include true, false, instanceof, equals, and,
  or, not, method invocation and null testing.
  p
 
  @param T the type that the predicate queries
 
  @since 1.0
 */
@FunctionalInterface
public interface Predicate2<T> extends Predicate<T> {

    
   /* Predicates are chainable. The output of the first becomes the input of the 
     * next and so on. When it comes time to evaluate, only the first input and the last 
     * result need be supplied. The remaining are obtained by evaluating the 
     * predicates in order.     
      @param object  the object to evaluate, should not be changed
      @return true or false
      @throws ClassCastException (runtime) if the input is the wrong class
      @throws IllegalArgumentException (runtime) if the input is invalid
      @throws FunctorException (runtime) if the predicate encounters a problem
    */ 

	boolean evaluate(T object);

	default boolean evaluateChain(T ... seq)  { return false; }

	default boolean evaluate(T ... object) { return false; }

	default Evaluator getEvaluator() { return null; }

}