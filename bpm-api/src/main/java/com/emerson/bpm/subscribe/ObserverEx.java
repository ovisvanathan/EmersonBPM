package com.emerson.bpm.subscribe;

import java.util.List;

import com.emerson.bpm.api.SDKNode;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public interface ObserverEx<T> extends Observer<T> {

	default void observe(SDKNode otn) {}

	default void addObserver(Observer monarchObserver) {}

//	default List<Observer> getObservers() { return null; }

	default void onError(Throwable t) {}

	default void onComplete() {}


	@Override
	default public void onSubscribe(Disposable d) {
		
	}

	@Override
	default public void onNext(T t) {
		
	}

	default void refresh(SDKNode objectTypeNode) {}

	default void setSDKNode(SDKNode node) {}

	default Class getObjtype() { return null; }

}
