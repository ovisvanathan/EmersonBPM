package com.emerson.bpm.api;

public interface GameAI<T> {

	public ChannelManager getScoManager();

	public T getEntity(String idkey);

	public void initSelectors(Registrant pub, String[] pubevents);

}
