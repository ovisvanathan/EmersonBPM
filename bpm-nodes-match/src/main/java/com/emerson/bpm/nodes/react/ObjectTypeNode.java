/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes.react;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.subscribe.ObserverCallback;
import com.emerson.bpm.subscribe.ObserverEx;

/**
 *
 * @author salaboy
 */
public class ObjectTypeNode extends ReactiveNode implements ObserverCallback, FactHandle  {
    
    Class bean;
    Object beanInstance;
        
    List<SDKNode> childNodes = new ArrayList<>();
    
    static Topology topology;
    
    static Map<String, ObserverEx> observersMap = new HashMap();
    
    ObserverEx stubObserver;

	private List<Exception> exceptions;
    
	public static Topology getTopology() {
		return topology;
	}

	/*
	public ObjectTypeNode(String type) {
        this.type = type;    
    }
    */

	/*
	 * A Poor Man's implementation of rete algorithm.
	 * RETE sacrifices memory for performance.
	 * We built up a rete network for the various types and added
	 * observers to oberve the flow. 
	 * 
	 * However, the observer chain observers classes and not real facts 
	 * added to the rete or working memory.
	 * 
	 * This method below is a hack to retrive the previously registered 
	 * observer on the class type and add it as observer for the fact just added.
	 * 
	 * The observers do not observe facts directly but their observers. 
	 * 
	 * The observer mechanism itself is external to the object being observed.
	 * Therefore we do not add any observers from the code itself.
	 * It is the client's responsibility to add observers. That said,
	 * the framework handles this beautifully and user doesnt need to write any add 
	 * observers code etc.
	 * 
	 * 
	 */
    public ObserverEx getMonarchObserver() {  	
    //	if(objectTypeObserver == null)
	    
    	System.out.println("GET OBJ TP class = " + this.getObjectType());

    	if(this.stubObserver == null) {
    		
    		this.stubObserver = new MonarchObserver(this, this.getParent().getObjectType());    	 
    	}
    	/*
    	ObserverEx classObserver = null;
    	if(!observersMap.containsKey(this.getObjectType().getName())) {

    		System.out.println("GET OBJ TP NOT adding  ");
    		observersMap.put(this.getObjectType().getName(), objectTypeObserver);
    	} else {    		

    		System.out.println("GET OBJ TP else   ");

    		classObserver = (MonarchObserver) observersMap.get(this.getObjectType().getName());  		  		
    		
    		System.out.println("cob hc class " + classObserver.hashCode());

    	//	List<Observer> obrs = classObserver.getObservers();

    	//	System.out.println("otn followers size " + obrs);

    	//	for(Observer ob1 : obrs)
        //		Observable.just(objectTypeObserver).subscribe(classObserver);  		
    		
    	//	Observable.just(objectTypeObserver).subscribe(classObserver);  		

    		System.out.println("setting stub ");

    		this.stubObserver = classObserver;
    	}
    	*/
    	return this.stubObserver;	
    }

    /*
    public ObserverEx getObjectTypeObserver(SDKNode parent) {   	
    //	if(objectTypeObserver == null)
    	ObserverEx objectTypeObserver = new MonarchObserver(this, this, parent);
    	return objectTypeObserver;	
    }
    */
    	
    public boolean equals(Object o1) {
    	
    	if(this.bean.getName().equals(o1.getClass().getName())) 
    			return true;
    	else {
    		
    		if(o1.getClass().isAssignableFrom(ObjectTypeNode.class)) {
    			ObjectTypeNode ot1 = (ObjectTypeNode) o1;
        		if(this.beanInstance == ot1.beanInstance)
            		return true;
    		}
    	}

    	return false;
    }
    
    /*
    public static Observer<Object> getObjectTypeObserverInstance() {
    	return new ObjectTypeObserverInstance();
    }
    
    static class ObjectTypeObserverInstance implements Observer {
    	
    	Object payload;
    	
    	public ObjectTypeObserverInstance() {
    		
    		PublishSubject otnSubject = (PublishSubject) PublishSubject.fromArray(new Object [] { this });		
    		otnSubject.subscribe(AlphaNode.getAlphaObserver());		
    	}
    	
            @Override
            public void onNext(Object value) {
            	//new fact added 
	        	System.out.println("objecttypeobserver onNext val =" + value);
	        	this.payload = value;           	
            }
     
            @Override
            public void onError(Throwable e) {
                System.out.println("error");
            }
     
            @Override
            public void onComplete() {
                System.out.println("Subscriber2 completed");
            }

			@Override
			public void onSubscribe(Disposable d) {
		
				
			}
    }
   	*/
    /*
    public ObjectTypeNode(Class<T> bean) {
    	this.bean = bean;
    	this.type = bean.getClass().getName();
    }
    */

    public ObjectTypeNode(Map map, Object obj) {  	
    	super(map, obj.getClass());    	
    }

    public ObjectTypeNode(Class c) {  	
    	super(c); 
    	this.nodeObjectsMap.put(c, this);
    }

    public ObjectTypeNode(Object o) {  	
    	super(o); 
    	FactHandle classNode = this.nodeObjectsMap.get(o.getClass());
    	this.childNodes = classNode.getChildNodes();
    	this.nodeObjectsMap.put(o.getClass(), this);
    }

    /*
    public void assertObject(Handle factHandle, PropagationContext propagationContext, WorkingMemory wm) {
        if(this.type.equals(factHandle.getObject().getClass().getCanonicalName())){
            sinkPropagator.propagateAssertObject(factHandle, propagationContext, wm);
        }
    }
    */

    public ObjectTypeNode(Object ob1, Topology topology) {
    	this.topology = topology;
    	if(ob1 instanceof Class) {
        	this.bean = (Class) ob1;
     //   	this.type = bean.getClass().getName();
    	} else {
        	this.bean = ob1.getClass();     		
        	this.beanInstance = ob1;
    //		this.type = ob1.getClass().getName();
    	}

    }

	public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	
	@Override
	public void addTupleSink(SDKNode recNode) {
		childNodes.add(recNode);
		recNode.setParent(this);		
		
		System.out.println("object typenode ATS  queue put.. ");
			
		recNode.constructNext();
		
		/*
		try {
			nodesQueue.put(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
				
	//	recNode.getMonarchObserver().observe(this);
		
	}
	
	
	@Override
	public void addChild(SDKNode recNode) {		
		childNodes.add(recNode);
		recNode.setParent(this);
	}

	@Override
	public void onNextCB(Object value, SDKNode sdkNode) {

		System.out.println("object typenode onnextcb value .. " +value);
		
	}


	@Override
	public void registerObservers() {

		
	}
	
	@Override
	public void registerObserversSilently() {

	}

	@Override
	public void constructNext() {

		
	}

	public ObserverEx getObserver(Class objectClass) {
		return this.observersMap.get(objectClass);
	}

	public boolean isEntity() {
		// TODO Auto-generated method stub
		return false;
	}


	/*
	public static void registerAllObservers() {

		Rete net = (Rete) getTopology();
		
		List<ObjectTypeNode> nodes = net.getOTNodes();

		for(ObjectTypeNode otn : nodes)
			net.visitAndRegister(otn);
		
		
	}
	*/
    
	public Object getObject() {
		return super.getObject();
	}

	@Override
	public void setException(Exception e) {
		this.exceptions.add(e);
	}

	@Override
	public boolean hasExceptions() {
		// TODO Auto-generated method stub
		return this.exceptions.size()>0;
	}

	@Override
	public List<Exception> getExceptions() {
		return this.exceptions;
	}

	@Override
	public SDKNode setObject(Object fact) {
		this.nodeObject = fact;
		this.nodeType = fact.getClass();
	
	//	this.stubObserver.refresh(this);
		return this;
	}

	public void setObserver(ObserverEx classObserver) {
		this.stubObserver = classObserver;
	}

	@Override
	public List getChildNodes() {
		return this.childNodes;
	}
	
}
