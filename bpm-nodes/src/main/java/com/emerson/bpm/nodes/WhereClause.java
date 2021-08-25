package com.emerson.bpm.nodes;

import java.util.List;
import java.util.Stack;

import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.nodes.match.ObjectToAlpha;
import com.emerson.bpm.nodes.match.el.AndPart;
import com.emerson.bpm.nodes.match.el.OrPart;
import com.emerson.bpm.nodes.match.el.WherePart;
import com.emerson.bpm.nodes.react.MonarchObserver;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.rel.SequenceFactory;
import com.emerson.bpm.subscribe.ObserverCallback;
import com.emerson.bpm.subscribe.ObserverEx;

import io.reactivex.rxjava3.core.Observable;

public class WhereClause extends JoinNode implements ObserverCallback {
	
	List<WherePart> whereParts;
	
	public List<WherePart> getWhereParts() {
		return whereParts;
	}

	MonarchObserver whereObserver;
	
	public WhereClause(List<WherePart> whereParts) {
		super();
		this.whereParts = whereParts;

		parseWhereClause();
	}

	private void parseWhereClause() {

		
		Stack<WherePart> groupStack = new Stack();
		List<WherePart> nodes = this.getWhereParts();

		 StringBuffer sbuf = new StringBuffer();
		 sbuf.append(" WHERE ");

		WherePart nexttoken = null;
		for(WherePart token : nodes) {
			
			if(token instanceof AndPart  || 
					token instanceof OrPart ) {				
				groupStack.add(token);
			} else {
						
				int sz = groupStack.size();
				
				if(sz > 0) {
					for(int i=sz;i>=0;i--) {
						
						WherePart prevtoken = groupStack.get(i);				

						prevtoken.parts.add(token);
						prevtoken = groupStack.pop();				
					
						WherePart prevprevtoken = groupStack.peek();											
						prevprevtoken.parts.add(prevtoken);
						
						break;
					}
				} else {
					// where withouit and or or
					// must be implicit and 
					AndPart andp = new AndPart();
					andp.parts.add(token);
			
					groupStack.add(andp);					
				}
							
			}
				

			int sz = groupStack.size();
			System.out.println("gpstack size = " + sz);
		
			
		
		}
		
	}

	public void constructNext() {
		this.sequenceSet = SequenceFactory.constructSequenceSet(this, null);		
		this.bind();
	}

    @SuppressWarnings("unchecked")
    @Override
	public void registerObservers() {

    	  try {

      		ObjectTypeNode otn = (ObjectTypeNode) getParent();
  		
      		System.out.println("Alphanode construct otn parent  =" + otn.getObject());
  	    		
      		
      		Observable ob1 = Observable.just(otn.getMonarchObserver());
      				
      		ob1.subscribe(getMonarchObserver());				
   		
      	//	Observable ob2 = Observable.just(otn.getObjectTypeObserver());
      	
      		Observable ob2fe = ob1.filter(e -> e.toString().equals("Heebeebs"));
      		
  			ob2fe.compose(ObjectToAlpha.objectToAlpha())
  		//	     .filter(e -> sequenceSet.accept(e))			
  		//			.filter(e -> e.toString().equals("Heebeebs"))
  						.subscribe(r -> this.getMonarchObserver());
  			
  		} catch (Exception e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}

    }

	@Override
	public ObserverEx getMonarchObserver() {
    //	if(joinObserver == null)  	
		whereObserver = new MonarchObserver(this, this.getClass());    			 	
	    return whereObserver;
	 }

	@Override
	public void onNextCB(Object value, SDKNode sdkNode) {
		// TODO Auto-generated method stub
		
	}  	


	
}
