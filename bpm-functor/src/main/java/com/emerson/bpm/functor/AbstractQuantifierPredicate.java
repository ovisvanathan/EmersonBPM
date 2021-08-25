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
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

/**
 * Abstract base class for quantification predicates, e.g. All, Any, None.
 *
 * @since 4.0
 */
public abstract class AbstractQuantifierPredicate<T> implements PredicateDecorator<T>, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = -3094696765038308799L;

    /** The array of predicates to call */
    protected final Predicate<? super T>[] iPredicates;

    /**
     * Constructor that performs no validation.
     *
     * @param predicates  the predicates to check, not cloned, not null
     */
    public AbstractQuantifierPredicate(final Predicate<? super T>... predicates) {
        iPredicates = predicates;
    }

    /**
     * Gets the predicates.
     *
     * @return a copy of the predicates
     * @since 3.1
     */
    @Override
    public Predicate<? super T>[] getPredicates() {
        return copy(iPredicates);
    }

    /**
     * Clone the closures to ensure that the internal reference can't be messed with.
     *
     * @param closures  the closures to copy
     * @return the cloned closures
     */
    @SuppressWarnings("unchecked")
    static <E> Closure<E>[] copy(final Closure<? super E>... closures) {
        if (closures == null) {
            return null;
        }
        return (Closure<E>[]) closures.clone();
    }
    
    /**
     * Clone the predicates to ensure that the internal reference can't be messed with.
     * Due to the {@link Predicate#evaluate(T)} method, Predicate<? super T> is
     * able to be coerced to Predicate<T> without casting issues.
     *
     * @param predicates  the predicates to copy
     * @return the cloned predicates
     */
    @SuppressWarnings("unchecked")
    static <T> Predicate<T>[] copy(final Predicate<? super T>... predicates) {
        if (predicates == null) {
            return null;
        }
        return (Predicate<T>[]) predicates.clone();
    }

    
    /**
     * Copy method
     *
     * @param transformers  the transformers to copy
     * @return a clone of the transformers
     */
    @SuppressWarnings("unchecked")
    static <I, O> Transformer<I, O>[] copy(final Transformer<? super I, ? extends O>... transformers) {
        if (transformers == null) {
            return null;
        }
        return (Transformer<I, O>[]) transformers.clone();
    }
    
    /**
     * Validate the predicates to ensure that all is well.
     *
     * @param predicates  the predicates to validate
     */
    static void validate(final Predicate<?>... predicates) {
        Objects.requireNonNull(predicates, "predicates");
        for (int i = 0; i < predicates.length; i++) {
            if (predicates[i] == null) {
                throw new NullPointerException("predicates[" + i + "]");
            }
        }
    }

    /**
     * Validate the predicates to ensure that all is well.
     *
     * @param predicates  the predicates to validate
     * @return predicate array
     */
    static <T> Predicate<? super T>[] validate(final Collection<? extends Predicate<? super T>> predicates) {
        Objects.requireNonNull(predicates, "predicates");
        // convert to array like this to guarantee iterator() ordering
        @SuppressWarnings("unchecked") // OK
        final Predicate<? super T>[] preds = new Predicate[predicates.size()];
        int i = 0;
        for (final Predicate<? super T> predicate : predicates) {
            preds[i] = predicate;
            if (preds[i] == null) {
                throw new NullPointerException("predicates[" + i + "]");
            }
            i++;
        }
        return preds;
    }

  
}
