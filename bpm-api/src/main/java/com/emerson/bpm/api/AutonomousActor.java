package com.emerson.bpm.api;

import java.util.Deque;
import java.util.Observable;
import java.util.Observer;

import org.checkerframework.checker.nullness.qual.NonNull;

import io.reactivex.rxjava3.disposables.Disposable;

public interface AutonomousActor<T> extends Runnable, Observer, ValueProvider<T> {
	
	public enum SubjectType {	
		SINGLE,
		MULTI,
		MULTITUDE, NONE			
	}

	AutonomousActor DEFAULT = new AutonomousActor() {

		@Override
		public void update(Observable o, Object arg) {
			
		}

		@Override
		public void run() {
			
		}

		@Override
		public SubjectType getSubjectType() {
			return null;
		}

		@Override
		public Principal getPrincipal() {
			return null;
		}

		@Override
		public Object getKey() {
			return null;
		}

		@Override
		public Object getValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setValue(Object val) {
			// TODO Auto-generated method stub
			
		}
		
	};

	default public Object getKey() { return null; }

	default public T getValue() { return null; }

	default public void setValue(Object val) {}

	default void update(Observable o, Object b) {}
	
	default void start() { run(); }

	default void run() {}
	
	default T getEntity(String key) {
		return null;
	}
	
	default SubjectType getSubjectType() { return null; }

	default Principal getPrincipal() { return null; }
			
	default Deque getBusmq() {
		return null;
	}
	
	default int processMessage() { return 0; }
	
	default public void onComplete() {}
		
	default public void onError(@NonNull Throwable arg0) {}
		
	default public void onNext(@NonNull Object arg0) {}

	default public void onSubscribe(@NonNull Disposable arg0) {}

}
