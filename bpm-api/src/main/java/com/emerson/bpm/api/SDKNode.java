package com.emerson.bpm.api;

import java.util.List;
import java.util.Map;

import javax.el.ELContext;

import com.emerson.bpm.subscribe.ObserverEx;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

public interface SDKNode extends Observer {

	default Class [] getObjClasses() { return null; }

	default SDKNode getRealNode() { return null; }

	default void addChild(SDKNode rtm) {}

	default void setParent(SDKNode joinNode) {}

	default Object getObject() { return null; }

	default public SDKNode setObject(Object data) { return this; }
	
	default Class getObjectType()  { return null; }

	default SDKNode getParent()  { return null; }

	default  RowTuple getParents() {return null; }

	default void setSequenceSet(OrderedSet deck) throws Exception {}

	default void addTupleSink(SDKNode recNode) {}

	default OrderedSet getSequenceSet() { return null; }

	default ClauseComparator getClauseComparator()  { return null; }

	default ObserverEx getMonarchObserver()  { return null; }

	default public ObserverEx getMonarchObserver(Object inst) { return null; }

		
	default void registerObservers() {}

	default void registerObserversSilently() {}

	default void bind() {}

	default List<SDKNode> getChildNodes() { return null; }

	default void constructNext() {}

	default ObserverEx getObserver(Object object) {  return null;  }

	@Override
	default public void onNext(Object value) {}

	default RuleQuery getRuleQuery() { return null; }

	default char[] getResult()  { return null; }

	default void setParents(SDKNode[] items) {}

	default void addParent(SDKNode alphaNode) {}

	default int getTuplicity() { return 1; }

	default SDKNode getLeft()  { return null; }

	default SDKNode getRight() { return null; }

	default ConnectableObservable[] getCOB()  { return null; }

	default Map<SDKNode, Disposable> getDisposables()  { return null; }

	default Class getKlazz() { return null; }

	default Map<Class, FactHandle> getNodeObjects()  { return null; }

	default void bind2() {}

	default void bind2(SDKNode node) {}

	default ELContext getElContext()  { return null; }

}
