package com.emerson.bpm.nodes.cmp;

import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.nodes.match.IncorrectParamsException;
import com.emerson.bpm.nodes.match.MissingFieldException;
import com.emerson.bpm.util.ServiceFactory;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

public class ReteNodeComparator  implements SDKNode {

	protected UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	public void construct()  throws Exception {}

	@Override
	public void registerObservers() {

		
	}

	@Override
	public void onSubscribe(@NonNull Disposable d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(@NonNull Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
	}

}
