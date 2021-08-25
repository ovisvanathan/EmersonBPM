package com.emerson.bpm.solver.automata;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.Random;

import com.evatic.paddle.bpm.tree.PaddleTree;
import com.evatic.paddle.bpm.tree.UserData;

public class FSM<T, V>  implements StateMachine<T>, ProviderExt<V>, Observer {

		T [] inputdata;
		
		int nsize;
		int rsize;
		
		Bound<DataBuf> boundDataBuf;
				
		PaddleTree<T> dataTree;
		
		List<T []> generated = new LinkedList<T []>();
		
		CellIteratorAdapter<T [], IterData> cellIter;

		PropertyChangeSupport propSupport = new PropertyChangeSupport(this);
		
		boolean allowRepeats;
		
		boolean is_dup;
		int dupIndex;
		
		static Class klazz;

		private static int batchSize = StateMachine.BATCH_SIZE_DEFAULT;
		
		Queue queue = new LinkedList();

		enum State { INIT, START, FIX1, FIX2, FIX3, CYCLE, SWAP, END }
		State state;
		
		public FSM(T [] input, int n, int r, Class klazz, boolean allowRepeats) {
			this.state = State.INIT;

			this.cellIter = new CellIteratorAdapter(input, n, r, klazz, allowRepeats);
				
			this.cellIter.setProvider(this);
			this.klazz = klazz;
			this.inputdata = input.clone();
			this.nsize = n;
			this.rsize = r;
			this.allowRepeats = allowRepeats;
			
			this.state = State.START;

			dataTree = new PaddleTree(klazz);
					
		//	propSupport.addPropertyChangeListener(cellIter);
		}
		
		
	@SuppressWarnings("unchecked")
	public void run() {
		
		// store some data in databuf. some random combination will suffice
	
		
		Random rand = new Random();
		
		Map<Integer, Integer> intMap = new HashMap<Integer, Integer>();
		
		int [] xs = new int[rsize];

		for(int d=0;d<rsize;d++)
			xs[d] = d;
		
		int k = 0;
		
		int [] bufInds = new int[rsize];
		T [] databuf = null;
		while(k < rsize) {
			
			int num = (int) rand.nextInt(nsize);

			databuf = (T []) Array.newInstance(klazz, rsize);
			
			System.out.println("num = " + num);
			if(!intMap.containsKey(num)) {
				int e = xs[k];
				databuf[e] = inputdata[num];				
		//		autodata[e] = new Automata();
		//		autodata[e].setData(databuf);
		//		autodata[e].setIndex(num);
				bufInds[e] = num;				
				k++;
				intMap.put(num, k);			
			}
		
		}
		
		// FSM will prepare automata array and set on iterator		

		int [] ixa = new int[nsize];

		int [] xdups = new int[nsize];

		for(int j=0;j<nsize;j++) {
			ixa[j] = j;
		}

		for(int j=0;j<nsize;j++) {
			xdups[j] = 0;
		}

		
		boundDataBuf = new Bound() {

			@Override
			public void update(Object o) {
				
				/*
				  	ProviderExt pxt =
				   (ProviderExt) Proxy.newProxyInstance(ProviderExt.class.getClassLoader(),
			                new Class[] { ProviderExt.class },
			                handler);

					IterData idata = new IterData();
					idata.cellNum = 2;
					idata.itemIndex = 3;
					DataBuf result = (DataBuf) pxt.getResult(idata);
		
					//		this.setBoundObj(dobj);
 
				 */
			}
			
		};
		
		DataBuf dataBuf = new DataBuf();
		
		dataBuf.setIndices(ixa);
		
		dataBuf.setRunIndices(bufInds);
		
		dataBuf.setDupIndices(xdups);

		dataBuf.setDatabuf(databuf);
				
		dataBuf.addObserver(this);
		
		cellIter.setDataBuf(dataBuf);
				
		boundDataBuf.setBoundObj(dataBuf);
				
		setState(State.CYCLE);
		
	}

	private void print() {

		DataBuf dataBuf = boundDataBuf.get();

	//	for(int r=0;r<rsize;r++)
	//		System.out.print(dataBuf.getDatabuf()[r]);

	//	System.out.println();

	}

	private void collect() {
		DataBuf dataBuf = boundDataBuf.get();

		generated.add((T[]) dataBuf.getDatabuf());	
		
		T dataElem = (T) dataBuf.getData();
				
		dataTree.add(dataElem, true);
		
	}
	
	private void collect(T data_item, int data_item_index) {

		if(StateMachine.MODE_SINGLE) {
			DataBuf dataBuf = boundDataBuf.get();
			
			generated.add((T[]) dataBuf.getDatabuf());		
			
			System.out.println("adding dataelem " + Arrays.asList(dataBuf.getDatabuf()).toString());
			
			T dataElem = (T) dataBuf.getData();
			
			dataTree.add(dataElem, true);
			
			propSupport.firePropertyChange(new PropertyChangeEvent(
					this, "NEW_DATA_AVAILABLE", null, dataBuf));
	
	
		} else if(StateMachine.MODE_BATCH){

			DataBuf dataBuf = boundDataBuf.get();
			
			boundDataBuf.clear();
			
			this.queue.add(dataBuf);
			
		//	DataBuf dataBuf = boundDataBuf.get();
			
			generated.add((T[]) dataBuf.getDatabuf());		

			int sz = this.queue.size();
			if(sz >= FSM.batchSize ) {

					T[] elemsArray = (T[]) Array.newInstance(klazz, sz);
					for(int t=0;t<queue.size();t++) {
						DataBuf dbuf = (DataBuf) queue.remove();
						T elem = (T) dbuf.getData();
						elemsArray[t] = elem;
					}
					
					dataTree.add(elemsArray, true);
					
					propSupport.firePropertyChange(new PropertyChangeEvent(
							this, "NEW_DATA_AVAILABLE", null, elemsArray));

					this.queue.clear();
			}
		}
		
	}

	
	@Override
	public void end() {
		
	}
	
	@Override
	public void progress() {

		DataBuf dataBuf = boundDataBuf.get();

		int currentCellNum = dataBuf.getActiveCellNum();
		
		T data_item = (T) dataBuf.databuf[currentCellNum];
		
		int dupix = dataBuf.dupIndices[currentCellNum];
		
		collect(data_item, currentCellNum);
		
		/*
		propSupport.firePropertyChange(
				new PropertyChangeEvent(
						this, "AUTOMATA_EVENT",
							null, databuf
						));
		*/
	}

	@Override
	public void setState(State s) {
		State oldState = state;
		state =  s;
		
		processStateChangeEvent(oldState, state);
		
	}


	private void processStateChangeEvent(State old, State newState) {

		if(old == State.START && newState == State.CYCLE)
			doCycle();
		
	}

	@Override
	public void start(int s, int r) {
		/*
		for(int i=s;i<r;i++) {			
			databuf[i] = inputdata[i];			
		}
		
		progress();
		*/
	}
	
	
	

	/*
	 * here we cycle through the possibilities for each cell
	 * of the automata databuf. We could start from cell 0.
	 * I have chosen to start from the last cell n-1
	 */ 
	private void doCycle() {
		
		int startptr = rsize-1;
		
		print();
		
		doCycleRecursive(startptr);
		
	}
	
	
	private void doCycleRecursive(int sptr) {
		
		cellIter.setCurrentCell(sptr);
		
		int itemIndex = 0;
		
		if(sptr == rsize-1) {
		
			while(cellIter.hasNext()) {
				
				cellIter.next();

				
				print();
				progress(); 
			}
	
			sptr--;
			System.out.println(sptr);
			doCycleRecursive(sptr);
			
		} else {
			
			cellIter.setCurrentCell(sptr);
			
			if(cellIter.hasNext()) {
				Object notused = (DataBuf) cellIter.next();
				print();
				progress(); 
			
				//if not last item, increment and recurse
				if(sptr < rsize-1) {
					doCycleRecursive(++sptr);
				} 
		
				
			} else {		

				sptr--;
				System.out.println(sptr);

				cellIter.setCurrentCell(sptr);

				doCycleRecursive(sptr);

			}
			
		}		
		
	}

	
	@Override
	public void update(Observable arg0, Object arg1) {
		
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(listener);
	}


	@Override
	public Object get() {
		// dummy method. do not call
		return null;
	}
	
	@Override
	public V getResult() {
		
		DataBuf dataBuf = boundDataBuf.get();
		IterData val = this.cellIter.newIterData();
				
		val.itemIndex = dataBuf.getActiveIndex();
		val.cellNum = dataBuf.getActiveCellNum();
		
		return (V) val;

	}

	@Override
//	@Transactional
//	@Undoable
	public V getResult(V idata) {
		
		DataBuf dataBuf = boundDataBuf.get();
		
		IterData val = (IterData) idata;
		
		dataBuf.setActiveCellNum(val.cellNum);
		dataBuf.setActiveIndex(val.itemIndex);		
		T [] dbuf = (T[]) dataBuf.getDatabuf();		
		dbuf[val.cellNum] = inputdata[val.itemIndex]; 		
		
		boundDataBuf.setBoundObj(dataBuf);
		
		return (V) dataBuf;
		
	}


	@Override
	public void undo() {
		
		
		
	}


	@Override
	public void redo() {

	}


	public void printOutput() {

		Iterator xter = this.dataTree.getIterator();
		
		System.out.println("########## PRINT TREE BEGIN ");
		
		while(xter.hasNext())
			System.out.println( ((UserData) xter.next()).getData());
		
		
		System.out.println("########## PRINT TREE END ");
		
	}


	public static Class getNodeClass() {
		// TODO Auto-generated method stub
		return klazz;
	}




	
}
