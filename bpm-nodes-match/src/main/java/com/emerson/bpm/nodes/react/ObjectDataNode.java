package com.emerson.bpm.nodes.react;

import com.emerson.bpm.api.SDKNode;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

/*
 * A simple wrapper around a data Object to pass around from observable to 
 * observer
 */
public class ObjectDataNode implements SDKNode {
	
	Object data;
	
	public ObjectDataNode(Object data) {
		this.data = data;
	}
	
	public Object getObject() {
		return this.data;
	}

	@Override
	public void onSubscribe(@NonNull Disposable d) {
		
	}

	@Override
	public void onError(@NonNull Throwable e) {
		
	}

	@Override
	public void onComplete() {
		
	}
	
}
