package com.emerson.bpm.dsl.client;

import java.util.function.Function;

import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.util.NetworkBuilder;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class ClientReteBuilder<T> implements Observer {

	void reset() {}

	T getArg() { return null; }

	void clientBuildNetwork(Function arg) {}
	
	public void onError(Throwable t) {}

	@Override
	public void onSubscribe(Disposable d) {

		
	}

	@Override
	public void onNext(Object t) {

		
	}

	@Override
	public void onComplete() {

		
	}

	Object getCB() { return null; }
			
	public static NetworkBuilder builder(String id) {
		return new RulesNetworkBuilder(id);
	}

	public static NetworkBuilder builder2(String id, Topology rete) {
		return new RelationDSLBuilder(id, rete);
	}
	
	public Topology buildNetwork() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
