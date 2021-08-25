package com.emerson.bpm.solver.automata;

import java.util.List;

public interface PostCallback<T> {
	public void postCallback(T [] data);

	public void postCallback(List<T []> data);

}
