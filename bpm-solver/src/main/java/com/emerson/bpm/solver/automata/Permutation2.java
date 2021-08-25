package com.emerson.bpm.solver.automata;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


/*
 * Initializes a pseudo random variable that generates all possible 
 * combinations one at a time which is accumulated or streamed to client 
 * program. 
 * 
 * Designed to provide maximum entropy solution where the input 
 * values are uncorrelated and yet runs in log(N) time. 
 * 
 * The client does not need to store anything although the psuedo random
 * varaiable needs to. Actually the PRV delegates to a Hashset that 
 * eliminates duplicates. 
 * 
 * The PRV uses random.nextGaussian which generated values with mean 0 
 * and STD of 1.0. The PRV selects from a normal distribution and hence
 * only values within 1 σ are expected. To alleviate, you can configure
 * 3 or more PRV with mean/std adjusted.
 * 
 */
class Permutation2<T, V> implements Combinative {

	Permutation p;
	String sval;
	
	boolean alldone = false;
	
	int NUM_PERMS;
	String errMsg = "";
	
	V xt = null;
	
	BlockingQueue queue;
	
	public static final boolean ALLOW_REPEATS = false;
	
	public Permutation2() {
		
	}
	
	public void start(final T [] input, final int r, final int n) {
		
			
			boolean added = false;
			
			Runnable xr = new Runnable() {
				
				public void run() {

					while(!alldone) {
					
						int x = nextRand();
						
						int fcount = 0;
						int count =0;
			
						next_perm(input, n, r, x);
						
					}
				}
				
			};
			
			
			Runnable reader = new Runnable() {
			
				public void run() {
				
					try {
						
						xt = (V) getData();
						
						setadd(xt);
					} catch (SizeFullException e) {
						// TODO Auto-generated catch block
						
						alldone = true;
						e.printStackTrace();
						return;
					} 
					
					execfunc(xt);					
			
				}
				
				
			};
				
				
				
				
				xr.run();
				
			
		System.out.println(errMsg);
		
	}

	private void execfunc(V x) {

		String s = (String) x;
		System.out.println(s);
		
	}

	/*
	private synchronized Object next_perm_data(T [] input, int r, int x) {

		next_perm(input, n, r, x);
		
		return getData();
		
	}
	*/
	
	private Object getData() {
		// TODO Auto-generated method stub
		return sval;
	}

	private void next_perm(T [] input, int n, int r, int x) {
		
		// get the xth permutation method		
		
		PostCB cb = new PostCB();
		
		queue = new ArrayBlockingQueue(1024);
		  
		if(p == null)
			p = new Permutation(Character.class, cb, queue, true, this);
		 
		 p.run(input, n, r);
		
		
		
		 
	}
	
	
	class PostCB<T> implements PostCallback<T> {

		public void postCallback(T [] data) {

			Character [] xd = new Character[1];
			List<Character> lc = (List<Character>) Arrays.asList(data);
			Character [] xdata = 
					(Character []) 
						lc.toArray( xd );
			sval = String.valueOf(xdata);
		
		}
		
		public void postCallback(List<T []> dataList) {

			for(T [] data : dataList) {
				
				Character [] xd = new Character[1];
				List<Character> lc = (List<Character>) Arrays.asList(data);
				Character [] xdata = 
						(Character []) 
							lc.toArray( xd );
				sval = String.valueOf(xdata);
				
			}		
		}
		
	}

	private boolean setadd(V vobj) throws SizeFullException {

		HashSet<V> hset = new HashSet<V>();

		int hsize = hset.size();
		
		if(hsize == NUM_PERMS)
			throw new SizeFullException("Hashset size full");
		
		boolean b = hset.add(vobj);
		
		return b;
	}

	private int nextRand() {
		double rand = new Random().nextGaussian() * 4; // from -4.0 to + 4.0 

		double rand2 = rand + 4;  // from 0 to 8.0;
		
		double rand3 = rand - 1;  // from 0 to 7.0; mean 3.5 std: 4

		if(rand3 < 0) rand3 = 0;
		if(rand3 > 7) rand3 = 7;
		
		return (int) rand3;
	}


	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		
		String propName = arg0.getPropertyName();
		
		if(propName.equals("NEW_DATA_AVAILABLE")) {
			
			DataBuf dbuf = (DataBuf) arg0.getNewValue();
			 
			try {
					this.queue.put(dbuf);		
			//		this.queue.notify();
					
				 } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}

	@Override
	public boolean allowRepeats() {
		// TODO Auto-generated method stub
		return this.ALLOW_REPEATS;
	}



	


}