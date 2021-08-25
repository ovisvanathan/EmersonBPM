package com.emerson.bpm.solver.automata;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;

/*
 * Iterates over a list/set of type T but returns
 * an index into the list of type X usually integer. You could
 * choose to return any type you want also.
 * 
 */
public class MultiCellIterator<T, X> implements Iterator {
//, PropertyChangeListener {
	
	int nelems, relems, rlen;
	T [] input;
	
//	T [] pdata;

	CellIterator [] iters;
	
	int currentCellNo;
	
	boolean allowRepeats;
	
	boolean isCurrsentSet;
	
	int [] indexData;
	
	int [] bufIndices;
	
	int counter = 0;
	
	IteratorAdapter adapter;
	
	public IteratorAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(IteratorAdapter adapter) {
		this.adapter = adapter;
	}

	PropertyChangeSupport propSupport;
	
	public int getCurrentCell() {
		return this.currentCellNo;
	}

	public void setCurrentCell(int cell) {
		this.currentCellNo = cell;
		isCurrsentSet = true;
	}

	DataBuf automata;
	
	public DataBuf getAutomata() {
		return automata;
	}

	public void setAutomata(DataBuf dataBuf) {
		this.automata = dataBuf;
		
		update();
	}

	MultiCellIterator(T [] input, int n, int r, Class klazz, boolean allowRepeats) {
		this.input = input;
		this.nelems = n;
		this.relems = r;
		this.allowRepeats = allowRepeats;
				
		System.out.println("this.nelems " + this.nelems);
		System.out.println("this.relems " + this.relems);
		
		propSupport =  new PropertyChangeSupport(this); 
		
	//	pdata = input.clone();
		
		this.automata = automata;
	}
	
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(listener);
	}

	public Iterator iterator(T start, int col) {	
		return iters[col]; 
	}

  /*
   * Iterator that iterates multiple cells individually each time incrementing to point 
   * to the next value in the sequence. Currently the design is to point to the immediate
   * next value in the original sequence. This could however be changed to point to the next 
   * lexicographical value, or based on some other user provided sequence routine.
   * 
   * The key thing is the the underlying array is generic and hence the sequence tracking
   * is done by the iterator itself. Thus it is not possible for example after 4-5 iterations
   * to get the next value unless the current value and its sequence number is known or kept 
   * track of, even in multithreaded situations.
   *  
   * Another issue is that of duplicates. The iterator loops through the sequence of values
   * till it sees the original first value in the sequence in which case hasNext returns false.
   * Whether it continues to return false depends on setting the LoopForever property. If set, the
   * the next time hasNext is called it returns true and cycles around. The cyclecount property 
   * indicates the number of cycles.
   * 
   * That is however not the point. The point is whether duplicates should be capped early. It may seem 
   * like the best way but it isnt. Typical use of the iterator is for combinatorial applications
   * where you need all possible combinations e.g 5 out of 8, 4 out of 10 etc. In such cases, eliminating 
   * duplicates would mean that some combinations would not be there because duplicates checking is enforced.
   * e.g acddea. For example, the above would not be possible because the iterator would have looped until
   * acddef and omitted a because of the duplicate issue. 
   * 
   * acddea may not be a valid combination. But fcddea is. But that would mean that the latter half would have to be 
   * cycled again. 
   * 
   * If duplicates remained, fcddea would exist. If you didnt want duplicates, we could always prune duplicates later.
   * 
   * Typical usage is you create MultiCellIterator iter = new MultiCellIterator(T [] seq, int nelems, int relems).
   * 
   * This means you are selecting relems from nelems of the sequence seq. 
   * 
   * You then populate a databuf with r elements selected from the input. This could be in any order.
   * However, you must provide an Automata array of size relems that contains the index of the elements 
   * added to the databuf.
   * 
   * If your input is { x, y, w, t, p, r } and you put { y, t, p } in your databuf, you put { 1, 3, 4 } 
   * in your automata array and call
   * 
   * myCellIterator.setAutomata(autoarray);
   * 
   * This needs to be done every time databuf changes, which is every time hasNext returns true. Essentially
   * every iteration. A property event is provided for the purpose. Just call progress method on the FSM
   * which fires a property change event to the iterator with the automata in it. 
   * 
   * If for some reason you want duplicates removed early, call setPruneDuplicates on the iterator.
   * 
   */
  class CellIterator implements Iterator {

		int cellNo;
		AutoPointer [] ptrs;
		
		Object [][] iterdata;
		
		IterData idata;
		
		public CellIterator(int cellNo) {
			this.cellNo = cellNo;

			iterdata = new Object[relems][];
								
			ptrs = new AutoPointer[relems];

			init();
		}
		
		private void init() {
			
			/*
			for(int i=0;i<relems;i++) {

				T [] qdata = pdata.clone();

				rotate(qdata, 1, i);

				iterdata[i] = qdata;
				
				AutoPointer aptr = new AutoPointer(i, 0, 0);				
				ptrs[i] = aptr;
			}
			*/
			
			
		}
		
		@Override
		public boolean hasNext() {
			
			int [] xinds = automata.getRunIndices();
			
			int [] xdups = automata.getDupIndices();
			
			int x = xinds[this.cellNo];
			
			AutoPointer ptr = ptrs[this.cellNo];

			idata = adapter.newIterData();
			
			/*
			int origX = 0;
			// we will need origX later
			int cptr = ptr.getCurrPtr();
			if(cptr == 0) {
				origX = x;
				automata.setFirstIndex(origX);
				cptr = origX;
			}
			*/
			
		//	T [] pdata = (T[]) iterdata[this.cellNo];
			
			/*
			 * rotate isnt really needed as we have x. 
			 * and we could do input[x].  
			 * 
			 * What if the input were not available as is likely to be in many cases. The input is simply not 
			 * available, is too valuable to be made available or simply provided for a short while and
			 * then you were asked to return it but the application must however function without the input for an indefinite
			 * period.
			 * 
			 * we will see both cases.
			 */
			// rotate(pdata, x, x);

			x++;
			counter++;
			
			if(x  >= nelems) 
				x = 0;

			idata.itemIndex = x;
			idata.cellNum = this.cellNo;
			
			if(counter == nelems - 1) {

				idata.isDup = true;
				idata.dupIndex = counter;
			
				propSupport.firePropertyChange(
						new PropertyChangeEvent(
								this,
								"DUPLICATE_ITEM", null, counter));
			
			}
			boolean b = (counter == nelems)? false: true;
			
			if(!b) {
				counter = 0;
				x--;
			}

			xinds[this.cellNo] = x;


			// update automata to set if this item is a duplicate item
			// a non-zero value indicates a duplicate
			// the gives the index of the item that is duplicate
			// as 0 is a valid array index, this indicator 
			// is 1 based.
			if(idata.isDup) {
				xdups[this.cellNo] = ++idata.dupIndex;				
			}
			
			automata.setRunIndices(xinds);
						
			return b;
				
			// hasNext for case when input is not available and only the automata array of size n elems
			// that gives the index of each item in the input.
			
			
			
		}


		@Override
		public Object next() {

			int [] xinds = automata.getRunIndices();
			
			int x = xinds[this.cellNo];
			
			AutoPointer ptr = ptrs[this.cellNo];
			
	//		T [] rodata = (T[]) iterdata[x];

	//		if(ptr.cycleCount == 0) {
	//			return rodata[ptr.currPtr++];
	//		} 

//			Object retval = input[x];
	
			// input not available
			
			idata.itemIndex = x;
			
			return idata;
			
			
		}
		
		@Override	
		public void remove() {

		}

		public AutoPointer getAutoPointer(int k) {
			// TODO Auto-generated method stub
			return ptrs[k];
		}
	}

  	/*
  	 * fixdups runs before rotate to check and remove duplicate
  	 * elements if allowRepeats is set to false.
  	 * 
  	 */
  	private T [] fixDups(T [] pdata, int order, int origpos) {
  		
  		Object [] qdata = null;
  		if(!this.allowRepeats) {
  			
  			int x = pdata.length;
  			qdata = new Object[x-1];
  			 
  			 int j=0;
  			 for(int i=0;i<x;i++) {
  				 if(i == origpos) {
  					 //
  				 } else {
  					 qdata[j++] = pdata[i];
  				 }
  			 }  			
  		}
  		
  		return (T []) qdata;
  		
  	}
  
  
    /*
     * rotate method rotates array contents by order param times.
     * e.g rotate(a, 1) where a = { a,b,c,d,e,f } rotates 
     * a to b = { b,c,d,e,f,a }
     *      * 
     * Imagine a = { c,e,b,a,f } is our orig array. We build our dictionary
     * 
     *  c,0 = e,b,a,f,c
     *  
     *  Our dictionary will have 1 rows, one for each slot in the array.      
     *  
     *  Of course whether to include a[i] in the result depends on whether we want to 
     *  allow duplicates or not. 
     *       *  
     *  The origpos is key here. Our orig array was { c,e,b,a,f } and after rotation
     *  got changed to { e,b,a,f,c }. As We are using a generic array and cannot examine 
     *  the contents to see whats at position 2. The only solution is to track the number of times the slot has been
     *  rotated. We always rotate one position to the left. So all we need to 
     *  do is track how many times rotate has been called to figure out whats in the slot.
     *  
     *  That is ofcourse the caller's concern. Rotate itself only needs the origpos.
     *  You better have it ready.
     *  
     */
	private T [] rotate(T [] pdata, int order, int origpos) {
    	
    	if (pdata == null || pdata.length==0 || order < 0) {
    		throw new IllegalArgumentException("Illegal argument!");
    	}

    	/*
    	System.out.println(" data before rotate");
    	
    	for(int i=0;i<sdata.length;i++) 
    		System.out.println(sdata[i]);
    	*/
    	
    //	T [] pdata = fixDups(sdata, order, origpos);
    	
    	/*
    	if(!this.allowRepeats) {    		
    		int j = 0;
    		T [] pdata = sdata.clone(); 
    		for(int i=0;i<sdata.length;i++) {
    			if(i == origpos) {
    				pdata[j] = null;
    			} else {
    				pdata[j++] = sdata[i];  				
    			}
    		}    		
    	}
		*/
    	
    	/*
    	System.out.println(" data after rotate");

    	for(int i=0;i<sdata.length;i++) 
    		System.out.println(pdata[i]);
		*/

    	if(order > pdata.length){
    		order = order % pdata.length;
    	}
     
    	//length of first part
    	int a = pdata.length - order; 
     
    	reverse(pdata, 0, pdata.length-1);
    	reverse(pdata, a, pdata.length-1);
    	reverse(pdata, 0, a-1);
    	
    	
    	return pdata;
	    	
    }
	
	 private void reverse(T [] pdata, int left, int right){
	    	if(pdata == null || pdata.length == 1) 
	    		return;
	     
	    	while(left < right){
	    		T temp = pdata[left];
	    		pdata[left] = pdata[right];
	    		pdata[right] = temp;
	    		left++;
	    		right--;
	    	}	
	    }

	 /*
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {

		String pname = arg0.getPropertyName();
		
		if(pname.equals("AUTOMATA_EVENT")) {
			automata = (Automata []) arg0.getNewValue();
			update();
		}		
	}
	*/
	
	private void update() {		
		
		if(iters == null) {
			iters = new MultiCellIterator.CellIterator[relems];
			this.indexData = this.automata.getIndices();
			this.bufIndices = this.automata.getRunIndices();
			
			for(int k=0;k<relems;k++) {
				iters[k] = new CellIterator(k);	
			}
		}
			
	}

	@Override
	public boolean hasNext() {
		
		if(!isCurrsentSet) {
			System.out.println("Current iterator cell must be set between 0 .. r ");
			currentCellNo = 0;
	}
		
		Iterator currIter = iters[currentCellNo];
		
		return currIter.hasNext();
	}

	@Override
	public Object next() {
		// TODO Auto-generated method stub
		Iterator currIter = iters[currentCellNo];
		
		return currIter.next();
	
	}

	@Override	
	public void remove() {

	}
	
}
