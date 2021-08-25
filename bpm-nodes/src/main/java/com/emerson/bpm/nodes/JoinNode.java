/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.TupleSink;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.dsl.FieldProvider;
import com.emerson.bpm.functor.el.ELEvaluator;
import com.emerson.bpm.nodes.el.ELExpressionResolver;
import com.emerson.bpm.nodes.match.FieldJoinComparator;
import com.emerson.bpm.nodes.match.FieldNameComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.match.QueryData;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.nodes.match.sql.QbField;
import com.emerson.bpm.nodes.match.sql.generic.QbFactoryImp;
import com.emerson.bpm.nodes.react.MonarchObserver;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.react.ReactiveNode;
import com.emerson.bpm.nodes.rel.SequenceFactory;
import com.emerson.bpm.nodes.row.RowUtils;
import com.emerson.bpm.sql.QbSQLFactory;
import com.emerson.bpm.sql.QbSQLSelect;
import com.emerson.bpm.subscribe.ObserverCallback;
import com.emerson.bpm.subscribe.ObserverEx;
import com.emerson.bpm.util.ServiceFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

/**
 *
 * @author salaboy
 */
public class JoinNode extends ReactiveNode implements TupleSink, RowTuple, ObserverCallback {

    protected BetaConstraints constraint;

  //  Pair tuplePair;    
    SDKNode left, right;

    public SDKNode getLeft() {
		return left;
	}

	public SDKNode getRight() {
		return right;
	}

	ConnectableObservable cobLeft;
	
	ConnectableObservable cobRight;

	static int observerCount;
	
	ObserverEx [] joinObservers;
	
	SDKNode [] rightNodes;

	UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

//    SDKNode [] leftNodes;
//    SDKNode [] rightNodes;
    
    int tuplicity;
        
    public int getTuplicity() {
		return tuplicity;
	}

//	String leftFieldName, rightFieldName;

//	String [] leftFieldNames;
 //   String [] rightFieldNames;

    List<SDKNode> childNodes = new ArrayList<>();

    RowTuple parents;
        
    ObserverEx leftObserver;

    ObserverEx rightObserver;

	private boolean multiJoin;

	private static Class leftAlphaNode;

	private Class rightAlphaNode;

    public boolean isMultiJoin() {
		return multiJoin;
	}

	public void setMultiJoin(boolean multiJoin) {
		this.multiJoin = multiJoin;
	}

	public RowTuple getParents() {
		return this.parents;
	}

	public enum JOIN_NODE_TUPLICITY {
    	ONE(2),  // +1 means left, right
    	TWO(3),  // means 3 and so on
    	THREE(4),
    	FOUR(5),
    	FIVE(6),
    	SIX(7),
    	SEVEN(8),
    	EIGHT(9),
    	MANY (447); // +1 means left, right
    
    	private int val;
    	
    	private JOIN_NODE_TUPLICITY(int val) {
    		this.val = val;
    	}
	
	};
     
	/*
    public JoinNode(BetaConstraints constraint) {
		super(constraint);		
    }
    */
    /*
    public JoinNode() {
    	super(null);

    	//connectable subscription
    //	AlphaNode.getConnectableObservable().subscribe(getJoinObserver());
    //	JoinNode.getConnectableObservable().subscribe(getJoinObserver());
    	
	//	Observable.just(AlphaNode.getAlphaObserver()).subscribe(getJoinObserver());
	//	Observable.just(JoinNode.getJoinObserver()).subscribe(getJoinObserver());
    	construct();

    }
    */

    public JoinNode() {
    	
    }

    @SuppressWarnings("unchecked")
    public JoinNode(SDKNode left, SDKNode right, String field1, String field2) {
    	super(new FieldNameComparator(COMPARATOR.EQUALS));
    
    	this.left = left;
    	this.right = right;
    		
    	findAlphaNodes();
    	
  //  	this.leftFieldName = field1;
   // 	this.rightFieldName = field2;
    	
    	tuplicity = 2;
    	
    	this.parents = createRow(left, right);
    	    	
		construct();
	//	Observable.just(AlphaNode.getMonarchObserver()).subscribe(getJoinObserver());
	//	Observable.just(JoinNode.getJoinObserver()).subscribe(getJoinObserver());
   
    }

    /*
     * JoinNodes can be made from 2 Alpha Nodes or 
     * a JoinNode and an Alpha Node. The left node is 
     * either an alpha or join nod and the right node
     * is usually an alpha node.
     *  
     */
    private void findAlphaNodes() {

    	
    	if(this.left instanceof AlphaNode) {
    		// no issue. just get parent and get its class
    		
    		SDKNode otn = this.left.getParent();
    		if(otn instanceof ObjectTypeNode) {
    			leftAlphaNode = (Class) otn.getObjectType();    			
    		}
    		
    	} 
    	
    	if(this.right instanceof AlphaNode) {
    		// no issue. just get parent and get its class
    		
    		SDKNode otn = this.right.getParent();
    		if(otn instanceof ObjectTypeNode) {
    			rightAlphaNode = (Class) otn.getObjectType();    			
    		}
    		
    	} 
    	    	
	}

	/*
     * Joins 2 joinNodes
     * No need to call construct
     */
    @SuppressWarnings("unchecked")
    public JoinNode(SDKNode left, SDKNode right) {
    	super(new FieldNameComparator(COMPARATOR.EXISTS));
    
    	this.left = left;
    	this.right = right;
    	
    	tuplicity = 2;
    	
    	this.parents = createRow(left, right);
    	    	
		construct();
    //	Observable.just(AlphaNode.getMonarchObserver()).subscribe(getJoinObserver());
	//	Observable.just(JoinNode.getJoinObserver()).subscribe(getJoinObserver());   
    }

    /*
     * Joins k Nodes
     * No need to call construct
     */
    @SuppressWarnings("unchecked")
    public JoinNode(SDKNode left, SDKNode ... right) {
    	super(new FieldNameComparator(COMPARATOR.EXISTS));
    
    	multiJoin = true;
    	
    	this.left = left;

    	this.rightNodes = right;
    	
    	assert(right != null);
    	
    	tuplicity = right.length;
    	
    	this.parents = createRow(left, rightNodes);
    	    	
   // 	this.cmp = new FieldNameComparator(COMPARATOR.EXISTS);

	//	construct();
    //	Observable.just(AlphaNode.getMonarchObserver()).subscribe(getJoinObserver());
	//	Observable.just(JoinNode.getJoinObserver()).subscribe(getJoinObserver());   
    }

    
    /*
     * 
     */
    public JoinNode(SDKNode left, SDKNode right, ClauseComparator comparator) {
    	super(comparator);

    	this.left = left;
    	this.right = right;

    	this.tuplicity = 2;
        	
    	this.parents = createRow(left, right);
    	    	
		construct();    	
	//	Observable.just(AlphaNode.getMonarchObserver()).subscribe(getJoinObserver());
	//	Observable.just(JoinNode.getJoinObserver()).subscribe(getJoinObserver());   
    }
    
	/*
	 * No checks performed. Pass through Alpha Node
	 * same functionality as COMPARATOR.EXISTS 
	 */
    public JoinNode(SDKNode left, SDKNode right, FieldProvider fieldProvider) {
		super(ServiceFactory.getFieldProviderFactory().getComparator(fieldProvider));

    	this.left = left;
    	this.right = right;

    	this.tuplicity = 2;
        	
    	this.parents = createRow(left, right);
    }


    /*
     * JoinNode performing role of Alpha Node. Do AlphaNodes follow JoinNodes?
     * I believe they do, eventually. But right now, we combine the 2 of them.
     */
    public JoinNode(Class criteriaClass, SDKNode right, ClauseComparator cmp) {
    	super(criteriaClass);
        	        
    	this.left = left;
    	this.right = right;
    	
    	tuplicity = 1;
    	
    	this.parents = createRow(left, right);
    	
   // 	this.leftFieldName = leftFieldName;
    	
    	this.cmp = cmp; 	    	
		construct();
 	
    }
    
    /*
     * Constructor that accepts an expression
     */
	public JoinNode(SDKNode an, SDKNode a2, String expr) {

		resolver = ELExpressionResolver.getResolverInstance(
				new Class [] { an.getKlazz(), a2.getKlazz() }, expr);
	
    	this.cmp = resolver.getEvaluator(); 	    	

	}

	public JoinNode(String relName, SDKNode sdkNode, SDKNode sdkNode2, String expr) {
		// TODO Auto-generated constructor stub
	}

	public void construct() {
	
		/*
    	RowTuple tuple = getParents();

    	SDKNode [] items = tuple.getItems();
    	
    	if(items.length == 2) {
    	
    		SDKNode left = items[0];
    	
    		Observable<TBS> ob3 = (Observable<TBS>) Observable.just(left.getMonarchObserver());

    		ob3.subscribe(this.getMonarchObserver(left.getKlazzInstance()));

    		SDKNode right = items[0];
        	
    		Observable<VBS> ob4 = (Observable<VBS>) Observable.just(right.getMonarchObserver());
	
    		ob4.subscribe(this.getMonarchObserver(right.getKlazzInstance()));	
    	}
    	*/

	}
    	   
    public void constructNext() {

		System.out.println("joinnode construct enter =");
		
		try {								
				if(this.cmp.get() == COMPARATOR.EXISTS)
					return;
	
				if(this.cmp instanceof ELEvaluator) {					
		
						this.sequenceSet = SequenceFactory.constructSequenceSet(this, this.relation);					
						this.bind2();										
				
				} else  {
			
						RowTuple tuple = this.getParents();
		
						SDKNode [] items = tuple.getItems();
		
						int n = items.length;
		
						Object [] tableObjects = new Object[n];
		
						String [] aliases = new String[n];
						String [] tableNames = new String[n];
						for(int k=0;k<n;k++) {
							SDKNode node = items[k];
							FactHandle tableObject = EmersonUtils.getObjectNodeForClassNode(node);
							tableObjects[k] = ((FactHandle)tableObject).getObject();
							String objName = node.getClass().getName();
							aliases[k] = ELUtils.createAlias(objName, k);
							tableNames[k] = ELUtils.createAlias("item", k);
						}
						
						Object [] fieldNames = this.cmp.getFieldNames();
						
						QbSQLFactory f = new QbFactoryImp();
						QbSQLSelect  sel = f.newSelectQuery();
		
						if(this.cmp instanceof FieldJoinComparator) {
		
							sel.select(f.newStdField((String) fieldNames[0]), f.newStdField((String) fieldNames[1]))
							.fromTableObject(tableNames[0], aliases[0], tableObjects[0])
							.join(tableNames[1], aliases[1], tableObjects[1], 
									f.newQualifiedField(aliases[1], (String) fieldNames[1]), 
									f.newQualifiedField(aliases[0], (String) fieldNames[0]));					
		
							this.queryData = (QueryData) ELUtils.convertSelectToQueryData(sel, false, false);
								
						} else if(this.cmp instanceof FieldNameComparator) {					
		
							sel.select(f.newStdField((String) fieldNames[0]), f.newStdField((String) fieldNames[1]))
							.fromTableObject(tableNames[0], aliases[0], tableObjects[0])
							.join(tableNames[1], aliases[1], tableObjects[1], 
									f.newQualifiedField(aliases[1], (String) fieldNames[1]), 
									f.newQualifiedField(aliases[0], (String) fieldNames[0]));					
		
							this.queryData = (QueryData) ELUtils.convertSelectToQueryData(sel, true, false);
		
						} else if(this.cmp instanceof FieldValueComparator) {					
							// possible join and where clause
							this.queryData = null;			
		
							/*	
							if(this.cmp.getClass().isAssignableFrom(NameComparison.class)) {
								// also join. is filedNameValComparator
								
								Object [] whereArgs = this.cmp.getWhereArgs();
							
								int x=whereArgs.length;
								
								List<Object []> wlist = EmersonUtils.convertObjectArrayToList(f, whereArgs);
								
								sel.select(f.newStdField( (String) fieldNames[0]), f.newStdField((String) fieldNames[1]))
								.fromTableObject(tableNames[0], aliases[0], tableObjects[0])
								.join(tableNames[1], aliases[1], tableObjects[1], 
										f.newQualifiedField(aliases[1], (String) fieldNames[1]), 
										f.newQualifiedField(aliases[0], (String) fieldNames[0]))
								.where()
								.forEach(x, wlist);
								
								this.queryData = (QueryData) EmersonUtils.convertSelectToQueryData(sel, false, false);
								
							} else {
							*/
								//		only fieldvaluecomparator
		
								Object [] whereArgs = this.cmp.getWhereArgs();
		
								sel.select(f.newStdField((String) fieldNames[0]))
								.fromTableObject(tableNames[0], aliases[0], tableObjects[0])
								.where()
								.where((QbField) this.cmp.qbfield(), this.cmp.qbwhere(), this.cmp.qbvalue());
		
								this.queryData = (QueryData) ELUtils.convertSelectToQueryData(sel, false, false);
							
						}

						this.sequenceSet = SequenceFactory.constructSequenceSet(this, null);						
						this.bind();

				}
		//		this.sequenceSet = SequenceBuilder.buildSequence(this);
			
			
				registerObservers();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		registerObservers();

    }

    
    private boolean isAssociative() {
		return this.relation.isAssociative();
	}

	public FactHandle getObjectNodeForClassNode(SDKNode node) {
    	
    	if(node instanceof AlphaNode) {
    		AlphaNode anode = (AlphaNode) node;
        	FactHandle pnode = (FactHandle) node.getParent();

        	Map<Class, FactHandle> nodesMap = pnode.getNodeObjects();
        	
        	Class nodeClass = node.getKlazz();
    	
        	return nodesMap.get(nodeClass);
    	}
    	
    	return null;
    	
    }
    
    @SuppressWarnings("unchecked")
    @Override
	public void registerObservers() {
    	
    	RowTuple tuple = getParents();
    	
    	SDKNode [] items = (SDKNode[]) tuple.getItems();
    	
    	int n = items.length;
    	
    	joinObservers = new ObserverEx[n];
    	
    	if(observerCount != n) {
    	
			    	int k=0;
			    	
			    	Observable [] obPrev = items[k++].getCOB();
			    	
			    //	Observable obleft = Observable.just(items[k++].getMonarchObserver());
				
			    //	ConnectableObservable cobLeft = obleft.publish();
			    	
			    			
					obPrev[0].filter(x -> ((SDKNode)x).getKlazz().isAssignableFrom(joinObservers[0].getObjtype()));
			    			
			    	obPrev[0].filter(x -> sequenceSet.accept(x));
			    	
			    	obPrev[0].subscribe(this.getMonarchObserver(this.left));
			    	
			    	observerCount++;
			    //	Observable ooblf = obleft.filter(x -> sequenceSet.accept(x));
			    	
				//	ooblf.compose(ObjectToAlpha.objectToAlpha())
				//			.subscribe(this.getMonarchObserver(this.left));
			
					/*
					if(tuplicity > 2) {
			
				    	Observable [] rightObservers = new Observable[items.length-1];
			
				    	int t=0;
				    	for(int i=1;i<items.length;i++) {
				    		SDKNode xnode = items[i];
			
				    		Observable rbobs = Observable.just(xnode.getMonarchObserver());
				    		rightObservers[t] = rbobs;
				    		
				    		Observable rbfts = rbobs.filter(p -> sequenceSet.accept(p));
				    		
				    		
				    		rbfts.compose(ObjectToAlpha.objectToAlpha())
				    				.subscribe(this.getMonarchObserver());
				    	
				    		t++;
				    	}
				    	
					} else {
					*/
			
			    	Observable [] obNext = items[k++].getCOB();
			
			    		//	Observable obright = Observable.just(rightNode.getMonarchObserver());
					
						   // 	ConnectableObservable cobRight = obright.publish();
			    			
			    			obNext[0].filter(x -> ((SDKNode)x).getKlazz().isAssignableFrom(joinObservers[1].getObjtype()));
				    	
					    	obNext[0].filter(x -> sequenceSet.accept(x));
						    	
					    	obNext[0].subscribe(this.getMonarchObserver(this.right));
					    	observerCount++;
			
							  //  	Observable obrx1 = obright.filter(p -> sequenceSet.accept(p));
							    	
							//		obrx1.compose(ObjectToAlpha.objectToAlpha())
							//				.subscribe(this.getMonarchObserver(this.right));    	
			    	
					// }
    	}		    	
					    	
    }

	@Override
	public ObserverEx getMonarchObserver(Object inst) {

		if(inst == this.left) {
			if(leftObserver == null)
				leftObserver = new MonarchObserver(this, ((SDKNode)inst).getKlazz());    			 
			
			System.out.println(" leftObserver id = " + leftObserver.hashCode());
			
			joinObservers[0] = leftObserver;
			
			return leftObserver;

		} else if(inst == this.right) {

			if(rightObserver == null)
				rightObserver = new MonarchObserver(this, ((SDKNode)inst).getKlazz());    			 	

			System.out.println(" rightObserver id = " + rightObserver.hashCode());

			joinObservers[1] = rightObserver;
			
			return rightObserver;
		}
		
		return null;
	 }  	

	@Override
	public ObserverEx getMonarchObserver() {

	//	joinObserver = ObjectRegistry.getObserver(this);
 		
	//	if(joinObserver == null)  {	 
    //		joinObserver = new MonarchObserver(this, getObject());    			 	
    //	}

		return null;
	 }  	


    private RowTuple createRow(SDKNode left, SDKNode ... rightTuples) {
    	this.parent = left;   	
    	return RowUtils.createRowTuple(left, rightTuples, this);   	
	}

	public void setBetaConstraints(BetaConstraints constraint) {
    	this.constraint = constraint;
    }
    
    /*
    public void assertObject(Handle factHandle, PropagationContext propagationContext, WorkingMemory wm) {
        RightTuple rightTuple = new RightTuple(factHandle, this);
        getMemory().addRightTuple(rightTuple); //?? this to the rightTupleSink????
        for (LeftTuple leftTuple : getMemory().getLeftTupleMemory()) {
            if(constraint instanceof EmptyBetaConstraints){
                System.out.println("Left Tuple = "+leftTuple);
                System.out.println("Right Tuple = "+rightTuple);
                for (LeftTupleSink sink : sinks) {
                    sink.assertLeftTuple(leftTuple, propagationContext, wm);
                }
            }
            
            //if it matches with the rigth tuple propagate
//            for (LeftTupleSink sink : sinks) {
//                sink.assertLeftTuple(leftTuple, propagationContext, wm);
//            }
        }
    }
    */

    public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
    public void addParent(SDKNode node) {
    	
    	if(this.parent == null)
    		this.parent = node;
    	else
    		this.tuplePair = new Pair(this.parent, node);
    		
    }
    
	public Pair getTuplePair() {
		return this.tuplePair;
	}	

    /*
    public void assertLeftTuple(LeftTuple leftTuple, PropagationContext propagationContext, WorkingMemory wm) {
        getMemory().addLeftTuple(leftTuple);
       
        for (RightTuple rightTuple : getMemory().getRightTupleMemory()) {
            if(constraint instanceof EmptyBetaConstraints){
                System.out.println("Left Tuple = "+leftTuple);
                System.out.println("Right Tuple = "+rightTuple);
                for (LeftTupleSink sink : sinks) {
                    sink.assertLeftTuple(leftTuple, propagationContext, wm);
                }
            }
            
           
        }
        //if it matches with the left tuple propagate
//        for (LeftTupleSink sink : sinks) {
//            sink.assertLeftTuple(leftTuple, context, wm);
//        }
    }
    */

	@Override
	public void addTupleSink(SDKNode recNode) {
		childNodes.add(recNode);
		recNode.setParent(this);
	
		/*
		
		try {
			nodesQueue.put(this);
			
			nodesQueue.put(recNode);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			*/
		
		recNode.constructNext();
		
	//	recNode.getMonarchObserver().observe(this);

	}
    
	/*
	@Override
	public void addChild(SDKNode recNode) {
		childNodes.add(recNode);
		recNode.setParent(this);
	}
	*/

	public ClauseComparator getComparator() {
		return this.cmp;
	}

	public List<SDKNode> getChildNodes() {
		return this.childNodes;
	}

	@Override
	public void onNextCB(Object value, SDKNode sdkNode) {

		System.out.println("joinnode onnextcb val ="+ value.getClass().getName());
		System.out.println("joinnode onnextcb node ="+ sdkNode.getClass().getName());
		
	}

	@Override
	public Object getObject() { 
		return this.nodeObject;		
	}

	@Override
	public SDKNode setObject(Object data) {
		this.nodeObject = data;
		this.nodeType = data.getClass();
		return this;
	}

	
	@Override
	public Class getObjectType() { 
		return null;
	}
	
	public Class [] getObjClasses() { 
		return new Class [] { this.leftAlphaNode, this.rightAlphaNode }; 
	}

	@Override
	public ConnectableObservable[] getCOB() {
		return new ConnectableObservable [] { cobLeft, cobRight };
	}

}
