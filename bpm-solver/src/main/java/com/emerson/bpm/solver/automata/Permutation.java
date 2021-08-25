package com.emerson.bpm.solver.automata;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
//Java program to print all combination of size r in an array of size n

class Permutation<T>  {

	Class<?> klazz;
	PostCallback cb;
	BlockingQueue<T []> queue;
	boolean batchMode;
	
	int BATCH_SIZE = 10;
	int MAX_SIZE = 10;
	
	Automata autoem;
	
	List<T []> batchList = new LinkedList<T []>();
	
	Combinative parent;
	
	public Permutation(Class<?> klazz) {
		this(klazz, new DefaultPostCB(), new ArrayBlockingQueue(1024), false, null);
	}

	public Permutation(Class<?> klazz, PostCallback callback, 
			BlockingQueue queue, boolean batchMode, Combinative parent) {
		this.klazz = klazz;
		this.cb = callback;
		this.queue = queue;
		this.parent = parent;
		
		this.autoem = new Automata();
		
	//	QueueMonitor monitor = new QueueMonitor(queue, autoem);

	//	monitor.start();

		this.batchMode = batchMode;
	}
	
 /* arr[]  ---> Input Array
 data[] ---> Temporary array to store current combination
 start & end ---> Staring and Ending indexes in arr[]
 index  ---> Current index in data[]
 r ---> Size of a combination to be printed */
 public void combinationUtil(T arr[], T data[], int start,
                             int end, int index, int r)
 {
     // Current combination is ready to be printed, print it
     if (index == r)
     {
    	 
		 try {
			 
			 
			 synchronized(autoem) {

				 this.queue.put(data);		

				 this.autoem.notify();
			 }
			
		 } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
     }
     
     // select 3 items out of 8 input arr
     
     
//     [ a, b, c, d, e, f, g, h ]
     
     
    
     
     
     
     

     // replace index with all possible elements. The condition
     // "end-i+1 >= r-index" makes sure that including one element
     // at index will make a combination with remaining elements
     // at remaining positions
     for (int i=start; i<=end && end-i+1 >= r-index; i++)
     {
         data[index] = arr[i];
         combinationUtil(arr, data, i+1, end, index+1, r);
     }
 }
 
 private T [] new_T(int r) {
	 
		 final T[] Tinst = (T []) Array.newInstance(klazz, r);
	 	 
		 return Tinst;
	 	 
 }

 // The main function that prints all combinations of size r
 // in arr[] of size n. This function mainly uses combinationUtil()
 public void printCombination(T arr[], int n, int r)
 {
     // A temporary array to store all combination one by one
     T data[]=new_T(r);

     // Print all combination using temprary array 'data[]'
     combinationUtil(arr, data, 0, n-1, 0, r);
 }
 
 public void run(T [] arr, int n, int r) {

 //    int n = arr.length;
 //    printCombination(arr, n, r);
     
     FSMRunner fsmrun = new FSMRunner(arr, n, r, klazz, this.parent);
     fsmrun.run();
	 
 }

 static class DefaultPostCB<T> implements PostCallback<T> {

	@Override
	public void postCallback(T[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postCallback(List<T[]> data) {
		// TODO Auto-generated method stub
		
	}
	 
 }
 
 public class QueueMonitor extends Thread {
	    
	    private Queue m;
	    Automata autoem;
	    
	    public QueueMonitor(Queue m, Automata autoem){
	        this.m=m;
	        this.autoem = autoem;
	    }

	    @Override
	    public void run() {
	        String name = Thread.currentThread().getName();

	        while(true) {
 	        
		        synchronized (autoem) {
		        	
		        	try{
		        		autoem.wait();
		            }catch(InterruptedException e){
		                e.printStackTrace();
		            }
		            System.out.println(name+" waiter thread got notified at time:"+System.currentTimeMillis());
		            //process the message now
		        	if(m.size() >= MAX_SIZE) {
		        		List<T []> c = new LinkedList<T []>();
		        		c.addAll(m);
		        		cb.postCallback(c);
		        	}
	
		            System.out.println(name+" processed: "+m.size());
		        }
		        
	        }
	        
	        
	    }

	}
 
 
 

}