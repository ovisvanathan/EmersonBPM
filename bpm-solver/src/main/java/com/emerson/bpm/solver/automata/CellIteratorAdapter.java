package com.emerson.bpm.solver.automata;


public class CellIteratorAdapter<T, X> implements IteratorAdapter {

	MultiCellIterator mcl;
	DataBuf dataBuf;
	
	int cellNo;
	
	int n, r;	
	Class klazz;
	boolean allowRepeats;	
	
	T [] input;
	
	ProviderExt provider;
	
	public ProviderExt getProvider() {
		return provider;
	}

	public void setProvider(ProviderExt provider) {
		this.provider = provider;
	}

	public CellIteratorAdapter(MultiCellIterator mcl) {
		this.mcl = mcl;
	}

	public CellIteratorAdapter(T [] input, int n, int r, 
			Class klazz, boolean allowRepeats) {
		this.input = input;
		this.n = n;
		this.r = r;
		this.klazz = klazz;
		this.allowRepeats = allowRepeats;
		
		mcl = new MultiCellIterator(input, n, r, klazz, allowRepeats);
		mcl.setAdapter(this);
	}

	public DataBuf getDataBuf() {
		return dataBuf;
	}

	public void setDataBuf(DataBuf dataBuf) {
		this.dataBuf = dataBuf;		
		mcl.setAutomata(dataBuf);
	}

	public int getCurrentCell() {
		return this.cellNo;
	}

	public void setCurrentCell(int cell) {
		this.cellNo = cell;
		mcl.setCurrentCell(cellNo);
	}

	@Override
	public boolean hasNext() {
		 return mcl.hasNext();
	}

	@Override
	public Object next() {
		X idata = (X) mcl.next();	
		DataBuf dbuf = null;
		synchronized(this) {

			
			dbuf = (DataBuf) provider.getResult(idata);
			
			/*
			dataBuf.setActiveCellNum(idata.cellNum);
			dataBuf.setActiveIndex(idata.itemIndex);		
			T [] dbuf = (T[]) dataBuf.getDatabuf();		
			dbuf[cellNo] = input[idata.itemIndex]; 		
			
			*/
		}
		
		return dbuf;
	}

	@Override
	public IterData newIterData() {
		// TODO Auto-generated method stub
		return new IterData();
	}
	
	@Override	
	public void remove() {

	}
}
