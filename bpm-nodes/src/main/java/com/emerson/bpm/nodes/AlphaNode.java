/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import java.util.ArrayList;
import java.util.List;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.TupleSink;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.dsl.FieldProvider;
import com.emerson.bpm.dsl.FieldProviderFactory;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.functor.el.ELEvaluator;
import com.emerson.bpm.nodes.el.ELExpressionResolver;
import com.emerson.bpm.nodes.match.FieldNameComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.match.PassThroughComparator;
import com.emerson.bpm.nodes.match.QueryData;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.nodes.match.sql.QbFactory;
import com.emerson.bpm.nodes.match.sql.QbSelect;
import com.emerson.bpm.nodes.match.sql.generic.QbFactoryImp;
import com.emerson.bpm.nodes.react.MonarchObserver;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.react.ReactiveNode;
import com.emerson.bpm.nodes.rel.SequenceFactory;
import com.emerson.bpm.nodes.row.NamedTuple;
import com.emerson.bpm.subscribe.ObserverCallback;
import com.emerson.bpm.subscribe.ObserverEx;
import com.emerson.bpm.util.ServiceFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

/**
 *
 * @author salaboy
 */
public class AlphaNode<T> extends ReactiveNode  implements TupleSink, ObserverCallback {
       
    Class objClass;
    
	List<SDKNode> childNodes = new ArrayList<>();

//	ConnectableObservable<T> alphaObservable;

	ObserverEx alphaObserver;

	private Class entityNode;

	Observable ob1;
	ConnectableObservable cob2;
		
	Class klazz;

	Object klazzInstance = null;
		
	public Object getKlazzInstance() {
		return klazzInstance;
	}


	UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	
	FieldProviderFactory factory = ServiceFactory.getFieldProviderFactory();
	
	/*
	 * No checks performed. Pass through Alpha Node
	 */
	public AlphaNode(Class klazz) {
		this(klazz, new PassThroughComparator());		
		construct();

	}
	
	
	/*
	 * No checks performed. Pass through Alpha Node
	 * same functionality as COMPARATOR.EXISTS 
	 */
	public AlphaNode(Class klazz, ClauseComparator comparator) {
		super(comparator);
		this.klazz = klazz;
		construct();

	}

	/*
	 * No checks performed. Pass through Alpha Node
	 * same functionality as COMPARATOR.EXISTS 
	 */
	public AlphaNode(Class klazz, FieldProvider fieldProvider) {
		super(ServiceFactory.getFieldProviderFactory().get(fieldProvider));
		this.klazz = klazz;
		construct();

	}

	/*
	 * No checks performed. Pass through Alpha Node
	 * same functionality as COMPARATOR.EXISTS 
	 */
	public AlphaNode(COMPARATOR comparator, SDKNode parent) {
		super(new FieldNameComparator(comparator));		
		setParent(parent);
		construct();

	}

	public AlphaNode(String s1, String s2) {
		super(new FieldNameComparator(s1, s2));			
		setParent(parent);
		construct();

	}

	
    /* Do not use this. Use below cons. */
	/*
	public AlphaNode(ClauseComparator comparator, String fieldName, Object value) {
		super(comparator);
        this.fieldName = fieldName;
        this.value = value;           

        construct();
     //   	Observable.just(ObjectTypeNode.getObjectTypeObserver()).subscribe(getMonarchObserver());				
	}
	*/

	/*
	public AlphaNode(COMPARATOR comparator, String fieldName, Object value, Class objClass) {
		this.comparator = comparator;
        this.fieldName = fieldName;
        this.value = value;           
        this.objClass = objClass;
		final AsyncSubject<Integer> subject = AsyncSubject.create();
        
		Observable.just(ObjectTypeNode.getObjectTypeObserver()).subscribe(getAlphaObserver());	
	}
	*/
	
	/*
	 * For bottom-up construction when addTupleSink is not called and therefore parent is not available
	 * until the tree is constructed. The class of the parent ObjectTypeNode is specified
	 * as parameter
	 * 
	 * Bare minimal constructort for alpha node that makes a field comparison
	 * 
	 */
//	public AlphaNode(ClauseComparator comparator, String fieldName, Object value, Class objClass) {
	public AlphaNode(String fieldName, Object value, COMPARATOR comparator) {
		
		super(new FieldValueComparator(fieldName, value, comparator));		
//        this.objClass = objClass;
		construct();
		
	//	Observable.just(ObjectTypeNode.getObjectTypeObserver()).subscribe(getMonarchObserver());	
	}
	
	
	public AlphaNode(Object [] whereClause) {
		
		super(new FieldValueComparator(whereClause));		
//        this.objClass = objClass;
		construct();
		
	//	Observable.just(ObjectTypeNode.getObjectTypeObserver()).subscribe(getMonarchObserver());	
	}


	/*
	 *  For bottom up construction, we call getObjectNode
	 */	
	public AlphaNode(ClauseComparator cmp) {
		super(cmp);
  //      this.objClass = objClass; 	    		
		construct();

	}

	/*
	 *  For bottom up construction, we call getObjectNode
	 */	
	public AlphaNode(ClauseComparator cmp, SDKNode parent) {
		super(cmp);
		this.setParent(parent);
  //      this.objClass = objClass; 	    		
		construct();

	}

    /*
     * Top-down construction
     */
	/*
    public AlphaNode(ClauseComparator cmp) {
		super(cmp);				
    }
	*/

	/*
	 *  Constructor that allows user to set evaluators using el expressions.
	 */
	@SuppressWarnings("unchecked")
	public AlphaNode(Class class1, String expr) {
		this.klazz = class1;
		resolver = ELExpressionResolver.getResolverInstance(
								new Class [] { this.klazz }, expr);	
		this.cmp = resolver.getEvaluator();
	}


	public AlphaNode(REL relation) {
		this.relation = relation;		
	}


	public void construct() {

	}
	
	@Override
	public SDKNode setObject(Object data) {
		this.nodeObject = data;
		this.nodeType = data.getClass();
		
		return this;
	//	this.alphaObserver.setSDKNode(this);
	}
	
	@Override
	public Class getObjectType() {
		ObjectTypeNode otn1 = (ObjectTypeNode) getParent();
		return (Class) otn1.getObjectType();
	}

	@Override
	public Object getObject() {
		return this.nodeObject;
	}
	

//	@Override
	public void constructNext() {
	
		System.out.println("Alphanode construct enter =");
	
		try {					
	
				if(this.cmp instanceof ELEvaluator) {					
					this.sequenceSet = SequenceFactory.constructSequenceSet(this, null);					
					this.bind2();										
				} else if(this.cmp instanceof PassThroughComparator) {

					this.queryData = null;
					this.sequenceSet = SequenceFactory.constructSequenceSet(this, null);					
					this.bind();
				
				} else {
			
						SDKNode objNode = this.getParent();
		
						FactHandle tableObject = EmersonUtils.getObjectNodeForClassNode(objNode);
						
						String objName = this.klazz.getName();
		
						String alias = ELUtils.createAlias(objName, 0);
						
						Object [] fieldNames = this.cmp.getFieldNames();
						
						Object [] whereArgs = this.cmp.getWhereArgs();
		
						QbFactory f = new QbFactoryImp();
						QbSelect  sel = f.newSelectQuery();
		
						List<Object []> wlist = null;
						int x = 0;
						if(whereArgs != null) {
							wlist = ELUtils.convertObjectArrayToList(f, whereArgs);
							x = wlist.size();
						}
						
						if(this.cmp instanceof FieldValueComparator) {
							// no join, only comparison
							sel.select(f.newStdField( (String) fieldNames[0]))
							.fromTableObject("p1", alias, tableObject.getObject())
							.where()
							.forEach(x, wlist);
							
							this.queryData = (QueryData) ELUtils.convertSelectToQueryData(sel, false, false);
						
						} else if(this.cmp instanceof FieldNameComparator) {					
							// simply passthrough
							// depends on cmp is EXISTS or EQUALS									
							
							if(whereArgs == null) {
								this.cmp = new PassThroughComparator();					
								this.queryData = null;					
							} else {
								
								sel.select(f.newStdField( (String) fieldNames[0]))
								.fromTableObject("p1", alias, tableObject.getObject())
								.where()
								.forEach(x, wlist);
								
								this.queryData = (QueryData) ELUtils.convertSelectToQueryData(sel, false, false);						
							}
							
						}
	
							this.sequenceSet = SequenceFactory.constructSequenceSet(this, null);
				
							this.bind();
				}
				

				registerObservers();

				} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerObservers() {

	    try {

				Session session = ServiceFactory.getSession();				
	
				ob1 = Observable.fromIterable(session.getWorkingMemory().getRete().getNodesQueue());

		    	Observable ooblf = ob1.filter(x -> x.getClass().isAssignableFrom(this.getKlazz()));

		    	Observable ooblf2 = ob1.filter(x -> sequenceSet.accept(x));
			
				cob2 = ooblf2.publish();
				
	//			cob2.filter(x -> sequenceSet.accept(x));
				
				cob2.subscribe(this.getMonarchObserver());
					    	
	    	/*
	    	
    		ObjectTypeNode otn = (ObjectTypeNode) getParent();
		
    		System.out.println("Alphanode construct otn parent  =" + otn.getObject());
	    		 		
    		//for direct observable

    		PublishSubject<MonarchObserver> subject = PublishSubject.create();
    		
    		Observable ob1 = Observable.just(otn.getMonarchObserver());
 
    		cob2 = ob1.publish();
    		
    		//added 25042021 for connectable observable
    	//	ConnectableObservable cob2 = Observable.just(otn.getObjectTypeObserver()).publish();
    		
    	//	ob1.subscribe(getMonarchObserver());				
 		
    	//	Observable ob2 = Observable.just(otn.getObjectTypeObserver());
   
    		
 //   		Observable ob2fe = ob1.filter(e -> e.toString().equals("Heebeebs"));
    		
    		
		//	ob1.compose(ObjectToAlpha.objectToAlpha())
		//	     .filter(e -> sequenceSet.accept(e))			
		//			.filter(e -> e.toString().equals("Heebeebs"))
		//				.subscribe(r -> this.getMonarchObserver());
	
    		ob1.subscribe(this.getMonarchObserver());
    		*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void registerObserversSilently() {

		System.out.println("Alphanode regobs silent enter =");

	    try {

    		ObjectTypeNode otn = (ObjectTypeNode) getParent();
		
     //   	Observable.just(otn.getObjectTypeObserver(this)).subscribe(getMonarchObserver());				
 		
    //		Observable ob2 = Observable.just(otn.getObjectTypeObserver(this));

    	//	Observable ob2f = ob2.filter(e -> sequenceSet.accept(e));
    	//	Observable ob2fe = ob2.filter(e -> testfunc(e));
    		
    	//	ob2fe
		 //    	.subscribe(this.getMonarchObserver());

		//	ob2.compose(ObjectToAlpha.objectToAlpha())
		//	     .filter(e -> sequenceSet.accept(e))			
			//		.filter(e -> e.toString().equals("Heebeebs"))
		//				.subscribe(r -> this.getMonarchObserver().onNext(r));
			
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

	
	
    private static boolean testfunc(Object e) {
    	System.out.println("testfunc ret false");    	
    	return false;
	}

	private static Observable getAlphaObservable() { 	
    	return getObservable(); 	
    }

	    
	private static Observable getObservable() {
	    return Observable.create(subscriber -> {
	 
	    	/*
	        subscriber.add(Subscriptions.create(() {
	        
	        }));
	        */
	    });
	}

    @Override
	public ObserverEx getMonarchObserver() {    	
    //	if(alphaObserver == null)
    	if(alphaObserver == null) {
    		 		 					
    		alphaObserver = new MonarchObserver(this, this.klazz);
    	}		
    	return alphaObserver;
    }
    
    /*
		{
	        @Override
	        public void onNext(Object value) {
	        	System.out.println("alphanodeobserver onNext val =" + value);

	        	
	        	
	        	
	        	if(value.getClass().isAssignableFrom(Integer.class)) {
	        		//all slots filled. lets evaluate
	       	        		
	        		try {
						if(cmp.evaluate()) {}
					} catch (ValueNotSetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        			
	        	} else {
	        		
		        	Class keyClass = value.getClass();
		        	
		        	if(deck != null) {
		        		if(!deck.containsValue(keyClass)) {
			        		deck.put(keyClass, value);
			        		deck.incr();
			        	}
			        }
		        	
	        	}
	        	
	        }
	 
	        @Override
	        public void onError(Throwable e) {
	            System.out.println("error");
	        }
	 
	        @Override
	        public void onComplete() {
	     //       System.out.println("Subscriber2 completed");
	        }

			@Override
			public void onSubscribe(Disposable d) {
		
				
			}
	    };
	    
		return alphaObserver;
	}
	
	*/
    
    
    /*
    public void assertObject(Handle factHandle, PropagationContext propagationContext, WorkingMemory wm) {
        try {
            Object object = factHandle.getObject();
            Class clazz = object.getClass();
            Method method = clazz.getMethod("get"+fieldName);
            Object result = method.invoke(object);
       
            if(((String)result).equals(value)){
                sinkPropagator.propagateAssertObject(factHandle, propagationContext, wm);
            }
            
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AlphaNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(AlphaNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(AlphaNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(AlphaNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AlphaNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    */
    
    
	@Override
	public RowTuple getParents() {
		return new NamedTuple(this.getParent());
	}

	public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SDKNode getSource() {
    	return this.getParent();
	}

    public void setParent(SDKNode parent) {	
    	this.parent = parent;	
    }

    public SDKNode getParent() {

		return this.parent;
	}

    @Override
    public Class getKlazz() {
		return this.klazz;
	}


	public Object getObjectSource() {
    	return ((ObjectTypeNode) this.getParent()).getObject();
	}

	@Override
	public void addTupleSink(SDKNode recNode) {
		childNodes.add(recNode);
	//	recNode.setParent(this);
		recNode.addParent(this);    		

		/*
		try {
			nodesQueue.put(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		recNode.constructNext();

	//	recNode.getMonarchObserver().observe(this);

	}

	@Override
	public void addChild(SDKNode recNode) {
		childNodes.add(recNode);
		recNode.setParent(this);
	}

    public Class getObjClass() {
		return objClass;
	}

	@Override
	public void onNextCB(Object value, SDKNode sdkNode) {

		if(value.getClass().isAssignableFrom(Integer.class)) {
      		//all slots filled. lets evaluate
     	        		      			
      	} else {
      		System.out.println("alphanode onnextcb else " + value.getClass().getName());
	        	Class keyClass = value.getClass();
	        	
	        	if(value.getClass().isAssignableFrom(ObjectTypeNode.class)) {

						System.out.println("alphanode value is monarch obs. filtering... " + value.getClass().getName());
					
						Observable ob2 = Observable.just(value); 

						System.out.println("alphanode sequenceSet = "+sequenceSet);

						System.out.println("alphanode val = "+ ((ObjectTypeNode)value).getObject());

						try {
								boolean result = sequenceSet.accept(value);
								System.out.println("alphanode result = "+ result);

				//			ob2.filter(e -> (result == true));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	        		
						
	        	} else {
	        		
	        		System.out.println("alphanode Onnext here .. " + value.getClass().getName());
										
	        		SDKNode node = ((MonarchObserver) value).getSDKNode();
	        		
	        		System.out.println("alphanode Onnext sdknode .. " + node.getClass().getName());
	        		
	        		ObjectTypeNode otn = (ObjectTypeNode) node;
	        		
	        		Object o = otn.getObject();
	        		
	        		if(o == null)
	        			System.out.println("o is null .. ");
	        		
	        		
	        	}
				
      	}
	   	
	}


	public void connect() {
		cob2.connect();
	}


	@Override
	public ConnectableObservable [] getCOB() {
		return new ConnectableObservable [] { this.cob2 };
	}

	
}
