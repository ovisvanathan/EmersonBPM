package com.emerson.bpm.functor;

import org.apache.commons.collections4.Predicate;

/*
 * A predicate that accepts M predicates taking n args, makes sure the number of
 * parameters matches the total requires args.
 */
public class NArgPredicate {

	int n;
		
	Predicate [] ps;
	
	public NArgPredicate(Predicate [] ps, Object [] obj, int n) {
		
		assert(obj.length == n);
	
		this.ps = ps;
		this.n = n;
	}


	public int getNoOfArgs() {
		return n;
	}



	public boolean evaluateMulti(Object... pargs) {
	
		int k=0;
		int p=0;
		for(Predicate pq : ps) {

			if(pq instanceof NArgPredicate) {
								
				int q=0;
				int arglen = getNoOfArgs();		
				Object [] parr2 = new Object[arglen];
				for(p=k;p<k+arglen;p++) {
					parr2[q++] =  pargs[p];
				}
				
				pq.evaluate(parr2);
				k = p;
				
			} else {
				
				return pq.evaluate(pargs);
			}

		}
		
		return false;
		
	}
	
}
	
	