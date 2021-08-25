package com.emerson.bpm.nodes.react;

import java.util.LinkedList;
import java.util.List;

import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.subscribe.ObserverCallback;
import com.emerson.bpm.subscribe.ObserverEx;

import io.reactivex.rxjava3.disposables.Disposable;

public class MonarchObserver<T> extends EmersonSubject<T> implements ObserverEx<T> {
	
	SDKNode sdkNode;
	ObserverCallback callback;
	SDKNode child;
	
	Class objtype;

	public Class getObjtype() {
		return objtype;
	}

	private List followers = new LinkedList();

	public List getFollowers() {
		return followers;
	}

	/*
	public MonarchObserver(SDKNode sdkNode, ObserverCallback callback) {
		this.sdkNode = sdkNode;
		this.callback = callback;		
	}
	*/
	
	public MonarchObserver(SDKNode sdkNode, Class obtype) {
		this.sdkNode = sdkNode;
		this.objtype = obtype;			
	}

	/*
	public MonarchObserver(SDKNode sdkNode, ObserverCallback callback, SDKNode child) {
		this.sdkNode = sdkNode;
		this.callback = callback;
		this.child = child;
		
		child.setParent(sdkNode);
	}

	public MonarchObserver() {

	}
	*/

	@Override
	public void onNext(T value) {
      	System.out.println("monarchobserver this node =" + sdkNode.getClass().getName());      	
      	
      	if(sdkNode instanceof ObjectDataNode)
      		return;	

		System.out.println("monarchobserver onNext val =" + value);
		
		if(objtype != null)
			System.out.println("monarchobserver obtype =" + objtype.getName());
			
		if(value instanceof MonarchObserver) {
			SDKNode node = ((MonarchObserver)value).getSDKNode();
		
			System.out.println("monarchobserver onNext node =" + node);

			SDKNode ots = (SDKNode) node;
			
			System.out.println("monarchobserver onNext ots val =" + ots.getObject());

			if(ots.getObject() instanceof ObjectTypeNode)
				System.out.println("monarchobserver onNext ots val val=" + ((ObjectTypeNode) ots.getObject()).getObject());
			
			//	this.sdkNode = ots;

			// ((ObserverEx)value).setSDKNode(this.sdkNode);

		} else if(value instanceof ObjectTypeNode) {

			SDKNode ots = (SDKNode) value;

			System.out.println("monarchobserver onNext ots val =" + ots.getObject());

			System.out.println("monarchobserver OTN type =" + ots.getObjectType().getName());

			if(ots.getObjectType().isAssignableFrom(this.objtype)) {
				System.out.println("monarchobserver klazz match. setting...");
				this.sdkNode.setObject(value);
			} else {

				System.out.println("monarchobserver OTN type mismatch. disposing...");
				
				Disposable disposable = sdkNode.getDisposables().get(sdkNode);				
				if(disposable != null)
					disposable.dispose();				
			}
				
		//	((ObserverEx)value).setSDKNode(this.sdkNode);
			
		} else if(value instanceof ObjectDataNode) {

		//	this.sdkNode = (SDKNode) value;
			
		//	System.out.println("monarchobserver onNext ots val =" + this.sdkNode.getObject());
		} 
	//	else if(value instanceof Object) {
	//		this.sdkNode = new ObjectDataNode(value);
	//	}

			

		//  	callback.onNextCB(value, sdkNode);

      }

      @Override
      public void onError(Throwable e) {
    	  e.printStackTrace();
          System.out.println("error");
      }

      @Override
      public void onComplete() {
   //       System.out.println("Subscriber2 completed");
      }

		@Override
		public void onSubscribe(Disposable d) {
	
			
		}

	public SDKNode getSDKNode() {
		return this.sdkNode;
	}

	@Override
	public void observe(SDKNode otn) {
	
		try {
			ObserverEx obex = otn.getMonarchObserver();
		//	Observable.just(otn).subscribe(this);
			
		//	System.err.println("obex hc " + obex.hashCode());
			obex.addObserver(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public void refresh(SDKNode objectTypeNode) {
		this.sdkNode = objectTypeNode;
	}

	@Override
	public void setSDKNode(SDKNode node) {
		this.sdkNode = node;
	}

//	@Override
//	public void addObserver(MonarchObserver monarchObserver) {
//		this.followers.add(monarchObserver);
//	}

//	@Override
//	public List<Observer> getObservers() {
//		return this.followers;
//	}

}
