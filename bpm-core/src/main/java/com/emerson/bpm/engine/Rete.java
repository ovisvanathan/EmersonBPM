package com.emerson.bpm.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.collections4.map.MultiValueMap;

import com.emerson.bpm.api.Agenda;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.RTMNode;
import com.emerson.bpm.nodes.react.ObjectRegistry;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.util.IteratorIterator;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;

public class Rete implements Topology { /// extends SDKNode {
		
	Map<Class, Set> typeNames = new HashMap<Class, Set>();
			
	List<FactHandle> myNodes = new ArrayList<>();
	List<SDKNode> myAlphaNodes = new ArrayList<>();
	List<SDKNode> myJoinNodes = new ArrayList<>();
	List<SDKNode> myRTMNodes = new ArrayList<>();
	
	Agenda agenda;
		
	ObjectTypeNode agendaNode;
	
	boolean isConnected;

	private List aggregations;

	private MultiValueMap<String, List<REL>> inlineFacts;

	private List factstores;

	private MultiValueMap<String, List<REL>> inlineFactsCMP;
	
	protected static BlockingQueue nodesQueue = new ArrayBlockingQueue<>(10);

	
	/*
	 * Start Rete without Agenda such as for testing etc.
	 */
	public Rete() {
	//	builder = new ReteBuilderImpl(this);					
	//	builder.loadIt();
	//	Observable.fromIterable(SDKNode.getNodesQueue()).subscribe(builder);		
	}
	
	public Rete(Agenda agenda) {
		this.agenda = agenda;
		agendaNode = new ObjectTypeNode(Agenda.class);
	}

	/*
	 * assertFact where client builds the network
	 */
	public void assertFact(FactHandle fact) {

	//	update(factHandle);

		//Fact added directly to rete does not cause Agenda activation,
		// WorkingMemory firing of rules etc.
	
		//While the net result is the same and all rules fire when fireALlRules is called,
		// whether the fact is added in wme or rete, subtle differences exsist
		// between the two.

		this.nodesQueue.add(fact);
		
		if(!isConnected)
			doConnect(fact);
					
	}
	

	private void doConnect(FactHandle fact) {

		loadData();
		
		Collection<FactHandle> factNodes = agendaNode.getNodeObjects().values();
		List<SDKNode> alphaConnectables = new ArrayList();
		for(FactHandle fct : factNodes) {			
			List<SDKNode> anodes = fct.getChildNodes();			
			for(SDKNode anode : anodes) {	

				if(fact != null)
					anode.bind2(anode.setObject(fact.getObject()));
					
				ConnectableObservable [] cobs = anode.getCOB();				
				ConnectableObservable cob = cobs[0];
				alphaConnectables.add(anode);
			}			
		}
		
		Map<SDKNode, Disposable> disposables = null;
		Disposable disposable = null;
		for(SDKNode node : alphaConnectables) {
			if(disposables == null)
				disposables = node.getDisposables();
			
			ConnectableObservable [] cobs = node.getCOB();				
			ConnectableObservable cob = cobs[0];		
			disposable = cob.connect();

			isConnected = true;
			disposables.put(node, disposable);
		}
	
		
	}

	private Iterator<FactHandle> loadData() {

		if(this.inlineFacts != null) {

			List<Iterator> iters = new ArrayList();
			Set keyset = this.inlineFacts.keySet();
			Iterator<String> itx = keyset.iterator();
			while(itx.hasNext()) {
				String key = itx.next();
				List<REL> rels = (List<REL>) this.inlineFacts.get(key);
				
				iters.add(rels.iterator());
			}
			
			return 
					new IteratorIterator(
							(Iterator []) iters.toArray(new Iterator [] {}));
			
		} else if(this.inlineFactsCMP != null) {

			
			
			
		} else if(this.factstores != null) {
			
		//	for(Factstore<> f : factstores)
		//		f.fetch();
			
		}
				
			
		return null;
		
		
	}

	/*
	 * For top-down construction
	 * @Param none
	 */
	public void build() {
		
		
	}

	/*
	 * For bottomup construction
	 * @Param Rule terminal Node to build tree from
	 */
	public void build(SDKNode rtmn1) {
		visitAndAdd(rtmn1);
	}
	
	/*
	 * We load Tree bottoms up. Client builds entire tree and adds only RTMNode
	 * to Rete. We climb up the tree upto ObjectTypeNode. 
	 * 
	 * or we can do nothing
	 */
	/*
	public void add(SDKNode rtm) {		
		visitAndAdd(rtm);
	}
	*/
	
	public void visitAndAdd(SDKNode rtm) {		

			SDKNode parent = rtm.getParent();				
			parent.addChild(rtm);				

			if(parent instanceof JoinNode) {
				JoinNode jn = (JoinNode) parent;
				RowTuple rt = jn.getParents();
			
				Object [] items = rt.getItems();
				int len = rt.getLength();
				if(len == 2) {
		
					SDKNode left = (SDKNode) items[0];
					SDKNode right = (SDKNode) items[1];
					
					left.addChild(rtm);
					right.addChild(rtm);
					
					rtm.setParents( (SDKNode []) items);
				}
				
			} else if(parent instanceof AlphaNode) {
				parent.addChild(rtm);							
				rtm.setParent(parent);				
				visitAndAdd(rtm);			
			} else if(parent instanceof ObjectTypeNode) {
		//		getOrCreateOTN(parent);
			}	
		
	}

	/*
	 * Object ob1 can be of Class type or Object type
	 * Initially when rules are loaded, the ObjectTypeNodes are
	 * constructed from classes. 
	 * 
	 * Then when facts are added, they are stored in a linkedHashset
	 * as part of their typename.
	 * 
	 */
	public FactHandle getOrCreateOTN(Object ob1) {
			
		if(ob1 instanceof ObjectTypeNode) {
			ObjectTypeNode omt = (ObjectTypeNode) ob1;		

			int i = myNodes.indexOf(omt);
			ObjectTypeNode otn = (ObjectTypeNode) myNodes.get(i);
			if(otn != null)
				return otn;
		}
	
		FactHandle otn = null;
		int i = myNodes.indexOf(ob1);
		if(i > 0) {
			otn = (ObjectTypeNode) myNodes.get(i);
			if(otn != null)
				return otn;
		} else {
			otn = ObjectRegistry.get(ob1);
			myNodes.add(otn);
			return otn;
		}
		
		return otn;
	}

	/*
	SDKNode getObjectNode(Class objClass) throws ObjectNotFoundException {

		int i = myNodes.indexOf(objClass);

		if(i<0)
			throw new ObjectNotFoundException("Object Node not found for class "+ objClass.getName());
		
		return myNodes.get(i);
	}
	*/

	
	public synchronized void update(Object fact) {

		Set totalset = new LinkedHashSet();
		Set set = null;
		
		FactHandle otn = ObjectRegistry.get(fact);
		
		System.out.println("rete totalset fact= " + ((Integer)  otn.getObject()));
	
		System.out.println("rete myNodes = " + myNodes);

		/*
		for(SDKNode rt : myNodes) {						
			if(!typeNames.containsKey(rt.getClass())) {
				set = new LinkedHashSet();
				set.add(otn);
				typeNames.put(rt.getClass(), set);				
			}	else {
				set = typeNames.get(rt.getClass());
				set.add(otn);
			}			
			totalset.addAll(set);
		}
		*/
		
		totalset.add(otn);

		System.out.println("rete totalset = " + totalset);

		
	//	PublishSubject otnSubject = (PublishSubject) PublishSubject.fromIterable(totalset);		
	//	otnSubject.subscribe(otn.getObjectTypeObserver());		

		final AsyncSubject<Integer> subject = AsyncSubject.create();
	//	PublishSubject.fromIterable(totalset).subscribe(otn.getObjectTypeObserver());

		Observable.fromIterable(totalset).subscribe(otn.getMonarchObserver());

		
	}

	public synchronized void update(SDKNode otn) {
		
		System.out.println(" rete got otn " + otn);
		//ObjectTypeNodes are top level nodes
		// alpha and join nodes represent rules
		// rtm nodes are execution points
		if(otn instanceof ObjectTypeNode) {
		
			this.myNodes.add((ObjectTypeNode) otn);		
			for(FactHandle rt : myNodes) {			
				if(!typeNames.containsKey(rt.getClass())) {
					Set set = new LinkedHashSet();
					typeNames.put(rt.getClass(), set);				
				}			
			}
			
		} else if(otn instanceof AlphaNode) {
			this.myAlphaNodes.add((AlphaNode) otn);		
		} else if(otn instanceof JoinNode) {
			this.myJoinNodes.add((JoinNode) otn);		
		} else if(otn instanceof RTMNode) {
			this.myRTMNodes.add((RTMNode) otn);		
		}
	}
	
	public List<SDKNode> getRTMNodes() {
		return myRTMNodes;
	}

	
	public void visitAndRegister(SDKNode otn) {

		List<SDKNode> children = otn.getChildNodes();

		for(SDKNode child : children) {

			System.out.println(" VARG = " + child.getClass().getName());
			
			if(!(child instanceof ObjectTypeNode))
				child.registerObservers();

			if(child instanceof AlphaNode) {			
				child.registerObservers();
			}

			for(SDKNode c : child.getChildNodes())
				visitAndRegister(c);
		}
		
	}
	

	@Override
	public List<FactHandle> getOTNodes() {
		return this.myNodes;
	}

	/*
	 * Rules can have many rtmNodes. For bottom up tree
	 * construction we need to join them together into a single RTMNode
	 * that provides summary results. This is more for the tree structure as
	 * no summary facility was requested
	 */
	public void build(SDKNode [] runtimeNodes) {

		int n = runtimeNodes.length;
		JoinNode rtmJoin = new JoinNode(runtimeNodes[0],
				 Arrays.copyOfRange(runtimeNodes, 1, n-1));
		
		RTMNode rnode = new RTMNode("rtm-join nodes");
		
		rtmJoin.addTupleSink(rnode);
		
		build(rnode);
		
	}

	/*
	 *  The principal method that runs when wm.fireAllRules is called.
	 *  There are 2 modes we can implement this
	 *  a. The data flow part
	 *  b. The action consequence part
	 *  
	 *  Rules fire internally when facts are added. The agenda
	 *  implements a valve that can either let the flow through or
	 *  block it. When the valve is open, the rete nodes are evaluated
	 *  and data flow begins. However fireAllRules may or may not have been called.
	 *  
	 *  
	 *  The observer mechanism is what causes data to flow from node to node.
	 *  The valve is what controls the observers. fireAllRules calls execute
	 *  on the rete nodes. This requires that we sync the observer data flow and 
	 *  execute so data flows only after execute has been called. This could be a limiting
	 *  factor and should be implemented only if needed.
	 *  
	 *  Otherwise, execute only controls the action consequence part. Subsequent rules may depend 
	 *  on the actions getting executed first. If so they will remain in wait state. 
	 *  
	 *  
	 */
	public void execute() {
		
		
	}

	@Override
	public FactHandle newFactHandle(Class class1) {
		FactHandle handle = ObjectRegistry.get(class1);	
		return handle;
	}

	@Override
	public @NonNull Iterable getNodesQueue() {
		return this.nodesQueue;
	}

	@Override
	public void connect() {

		
		
	}

	@Override
	public void setAggregations(List aggregations) {
		this.aggregations = aggregations;
	}

	@Override
	public void setInlineFacts(MultiValueMap<String, List<REL>> relationFacts) {
		this.inlineFacts = relationFacts;
	}

	@Override
	public void setFactstores(List factstores) {
		this.factstores = factstores;
	}

	@Override
	public void setCMPInlineFacts(MultiValueMap<String, List<REL>> cmpRelationFacts) {
		this.inlineFactsCMP = cmpRelationFacts;
	}	
	
//	public void setLayout(ReteLayout treeTableLayout) {
//		this.treeTableLayout = treeTableLayout;
//	}



}
