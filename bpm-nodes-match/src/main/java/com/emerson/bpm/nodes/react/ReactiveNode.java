package com.emerson.bpm.nodes.react;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.el.ELContext;

import org.reactivestreams.Subscriber;

import com.emerson.bpm.action.Consequence;
import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.OrderedSet;
import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.nodes.match.QueryData;
import com.emerson.bpm.nodes.match.el.ExpressionResolver;
import com.emerson.bpm.subscribe.ObserverEx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

public abstract class ReactiveNode<T> extends EmersonSubject implements SDKNode, AutonomousActor  {

	Vector<Subscriber> subscribers;
	
	protected static List objclasses;	

	protected static Map<Class, FactHandle> nodeObjectsMap = new HashMap();	

	protected Object nodeObject;	

	protected Class nodeType;	
	
	protected Map<SDKNode, Disposable> disposables = new HashMap();

	protected ExpressionResolver resolver;

	protected String name;
	
	protected REL relation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Map<SDKNode, Disposable> getDisposables() {
		return disposables;
	}

	public static List getObjclasses() {
		return objclasses;
	}

	protected SDKNode parent;	
	
	private static Topology rete;

	/* the Evaluator for Alpha and Join Nodes */
	protected ClauseComparator cmp;
	
	protected QueryData queryData;

	private List<Consequence> consequenceList = new LinkedList<>();
	
	protected static OrderedSet sequenceSet;
	
	protected static ConnectableObservable sdkNodeObservable;
	
	/*
	public static BlockingQueue<SDKNode> getNodesQueue() {
		return nodesQueue;
	}

	public static void setNodesQueue(BlockingQueue<SDKNode> nodesQueue) {
		ReactiveNode.nodesQueue = nodesQueue;
	}
	*/

	public ReactiveNode() {	
		super();
	}
	
	public ReactiveNode(ClauseComparator cmp) {	
		super(AutonomousActor.DEFAULT);
		this.cmp = cmp;
//		this.cmp.setSequenceSet(sequenceSet);
	}
	
	public ReactiveNode(Object o, Class c) {
			objclasses.add(o);			
			this.nodeObject = o;
			this.nodeType = c;

	}

	public ReactiveNode(Class c) {
		this.nodeType = (Class) c;		
	}

	public ReactiveNode(Object o) {
		this.nodeObject = o;		
		this.nodeType = o.getClass();		
	}
		
	public SDKNode getParent() {
		return parent;
	}
	
	/*
	public void addObjectSink(ReactiveNode node) {		
		try {
			nodesQueue.put(node);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addTupleSink(SDKNode node) {		
		try {
			nodesQueue.put(node);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

	/*
	protected SDKNode getObjectNode(Class objClass) throws ObjectNotFoundException {
		return rete.getObjectNode(objClass);
	}
	*/

	public Map<Class, FactHandle> getNodeObjects() {
		return this.nodeObjectsMap;
	}

	@Override
	public Object getObject() {
		return this.nodeObject;
	}

	@Override
	public SDKNode setObject(Object data) {
		this.nodeObject = data;
		return this;
	}
	
	@Override
	public Class getObjectType() {
		return this.nodeType;
	}

	/*
	public void addParent(ReactiveNode parent) {
		this.parent = parent;
	}
	*/

//	public abstract void addTupleSink(TupleSink sink);
	
		
	public RowTuple getParents() {
		return null;
	}
	
    private static Observable getNodeObservable() { 	
    	return getObservable(); 	
    }

    public static Observable getConnectableObservable() { 	
        
    	sdkNodeObservable = (ConnectableObservable) getNodeObservable();
    	sdkNodeObservable.publish();
    	return sdkNodeObservable;
    }
	
	private static Observable getObservable() {
	    return Observable.create(subscriber -> {
	 
	    	/*
	        subscriber.add(Subscriptions.create(() {
	        
	        }));
	        */
	    });
	}

	public void addConsequence(Consequence c) {
		this.consequenceList.add(c);
	}
	
	protected List<Consequence> getConsequences() {
		return this.consequenceList;
	}

	
	/*
		List<ReactiveNode> list = new ArrayList<>();

		if(this instanceof JoinNode) {
			JoinNode jn = (JoinNode) this;
			
			ReactiveNode.Pair ptoups = jn.getTuplePair();
			
			ReactiveNode toup1 = ptoups.first();
			ReactiveNode toup2 = ptoups.second();
			
			list.add(toup1);
			list.add(toup2);						
		} else {
			
			ReactiveNode node = getParent();
			list.add(node);			
		}
			
		return list.iterator();
	}
	
	
	public class Pair {
	
		ReactiveNode node1;
		ReactiveNode node2;
		
		public Pair(ReactiveNode node1, ReactiveNode node2) {
			this.node1 = node1;
			this.node2 = node2;
		}
		
		ReactiveNode first() {
			return this.node1;
		}

		ReactiveNode second() {			
			return this.node2;
		}

	}
	*/

    public ObserverEx getMonarchObserver() {
    	return null;
    }
    	
	public SDKNode getRealNode() {
		return null;
	}	

	public void addChild(SDKNode aplNode) {}

	public void setParent(SDKNode recNode) {
		this.parent = recNode;
	}

	public void setParents(SDKNode [] items) {
		
	}
		
	@Override
	public void bind() {
		try {
				this.sequenceSet.bind(this.cmp);
				this.cmp.setQueryData(this.queryData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void bind2(SDKNode contextNode) {
		try {
				this.sequenceSet.bind(this.cmp);				
				ELContext elctx = contextNode.getElContext();
				elctx.putContext(SDKNode.class, contextNode);
				this.cmp.setBindData(elctx);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public ClauseComparator getClauseComparator()  { 
		return this.cmp; 
	}

	
	@Override
	public OrderedSet getSequenceSet() { 
		return this.sequenceSet; 
	}

}
