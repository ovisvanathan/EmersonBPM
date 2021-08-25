package com.emerson.bpm.nodes.react;

import com.emerson.bpm.api.AutonomousActor;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.Subject;

public class EmersonSubject<T> extends Subject<T> {

	public EmersonSubject(AutonomousActor data) {
//		super(data);
	}

	public EmersonSubject() {
	}

	@Override
	public void onSubscribe(@NonNull Disposable d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNext(@NonNull T t) {
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

	@Override
	public boolean hasObservers() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasThrowable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public @Nullable Throwable getThrowable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void subscribeActual(@NonNull Observer observer) {
		// TODO Auto-generated method stub
		
	}

}
