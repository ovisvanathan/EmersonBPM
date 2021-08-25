package com.emerson.bpm.nodes.match;

import com.emerson.bpm.api.COMPARATOR;
import com.picasso.paddle.annotation.Component;

/*
 * A Comparator that does a join as well as provides for 
 * where clause checking
 */
@Component
public class PassThroughComparator extends DefaultComparator {
			
		public PassThroughComparator() {
			super(COMPARATOR.EXISTS);
		}

		@Override
		public boolean evaluate(Object o) throws Exception {
		
			
			return true;		
		}
		
}
