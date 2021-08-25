package com.emerson.bpm.api;

import java.util.List;
import java.util.Map;

import com.emerson.bpm.subscribe.ObserverEx;

public interface FactHandle {

	ObserverEx getMonarchObserver();

	Object getObject();

	void addTupleSink(SDKNode an);

	void setException(Exception e);

	boolean hasExceptions();

	List<Exception> getExceptions();

	SDKNode setObject(Object fact);

	default List getChildNodes() { return null; }

	Map<Class, FactHandle> getNodeObjects();
}

