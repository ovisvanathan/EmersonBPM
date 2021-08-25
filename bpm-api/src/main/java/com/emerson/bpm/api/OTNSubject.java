package com.emerson.bpm.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.emerson.bpm.api.AutonomousActor.SubjectType;
import com.emerson.bpm.util.CallableTask;
import com.emerson.bpm.util.SingleCallableTask;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public abstract class OTNSubject<T extends AutonomousActor> extends Observable implements Iterable, Observer {
	
	List<Object> observableItems;

	List<Observer> observers;

	SocketChannel client;
	ByteBuffer buffer;

	T data2;
	SubjectType subjectType;
	CallableTask data;
	Principal p;

	
	public OTNSubject() {
		this((T) AutonomousActor.DEFAULT);
	}
	
	public OTNSubject(T data) {
		this.data2 = data;
		this.p = data.getPrincipal();
		this.data = new SingleCallableTask(data.getPrincipal(), data);			
	}

	public OTNSubject(T data, SubjectType subjectType) {
		this.data2 = data;
		this.p = data.getPrincipal();
		
		this.data = new SingleCallableTask(data.getPrincipal(), data);
		this.subjectType = subjectType;
	}

	public T getSubject() {
		return this.data2;
	}

	public void setObservableItems(List<Object> observableItems) {
		this.observableItems = observableItems;
	}
	
//		public void addObject(Object data) {
//			this.data = data;
//		}

//		public void addObjects(List<Object> data) {
//			observableItems.addAll(data);
//		}

	public void addObserver(OTNSubject observer, boolean isMutual) {
		Observable.just(this.data2.getBusmq()).subscribe(observer);
		observers.add(observer);
		observer.getObservers().add(this);
	}

	public void addObservers(List<Observer> observers) {
		observers.addAll(observers);
	}

	//observer related
	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void subscribeActual(@NonNull Observer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete() {
		
	}

	@Override
	public void onError(@NonNull Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNext(@NonNull Object arg0) {

		getObservers().forEach(e -> e.onNext(arg0));
	}

	@Override
	public void onSubscribe(@NonNull Disposable arg0) {
		
	}

	public void scheduleForLaunch() throws IOException {

		this.client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
		this.buffer = ByteBuffer.allocate(256);

//			utils.broadcast(client, msg);
		
		this.p.getExecutor().submit(this.data);
		
	}

	public List<Observer> getObservers() {
		return this.observers;
	}

}
