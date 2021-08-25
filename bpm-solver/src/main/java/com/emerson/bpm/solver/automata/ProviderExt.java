package com.emerson.bpm.solver.automata;

import javax.inject.Provider;

public interface ProviderExt<V> extends Provider {
	
	public V getResult(V idata);

	public V getResult();

	public void undo();
	
	public void redo();
	
	/*
	public int getActiveIndex();
	
	public int getCurrentCell();
	
	public int [] getIndices();

	public int [] getRunIndices();

	public int [] getDupIndices();

	public T [] getDataBuf();
	*/
	
}
