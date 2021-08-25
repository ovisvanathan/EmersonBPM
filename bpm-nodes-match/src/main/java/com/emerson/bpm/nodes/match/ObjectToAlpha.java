package com.emerson.bpm.nodes.match;

import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.nodes.react.MonarchObserver;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;


public class ObjectToAlpha implements ObservableTransformer<MonarchObserver, SDKNode> {
	 
    public static ObjectToAlpha objectToAlpha() {
        return new ObjectToAlpha();
    }
 
    private ObjectToAlpha() {
        super();
    }
 
	@Override
	public ObservableSource<SDKNode> apply(Observable<MonarchObserver> upstream) {
		return upstream.map(x -> x.getSDKNode());
	}
}
	

