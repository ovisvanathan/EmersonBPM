package com.emerson.bpm.nodes;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.engine.EmersonSession;
import com.emerson.bpm.engine.SessionState;
import com.emerson.bpm.model.Account;
import com.emerson.bpm.model.Person;
import com.emerson.bpm.nodes.match.FieldNameComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.match.dto.FieldValueChecker;
import com.emerson.bpm.nodes.react.MonarchObserver;
import com.emerson.bpm.nodes.react.ObjectTypeNode;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.ConnectableObservable;

public class ConcurrencyTest {
	
	public static void main(String [] args) {
		
		
		try {

				ConcurrencyTest ct = new ConcurrencyTest();
			
				//	ct.testAlphaNodeObservable();
							
				//	ct.testAlphaNodeConnectableObservable();
				
				//	ct.testAlphaNodeAddTupleSink();
			
				ct.testAlphaNodeAddTupleSinkAndRegisterObservers();

				//	ct.testFilter();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	interface Holder {
		
	}
	
	public void testFilter() {
		
		@SuppressWarnings("unchecked")
		Observer<Holder> testObserver = new Observer() {

			@Override
			public void onSubscribe(Disposable d) {
		

			}

			@Override
			public void onNext(Object t) {
				count++;		
				
				System.out.println("testobs t ="+ t);
				System.out.println("testobs 2t ="+ t.getClass().getName());
						
			}

			@Override
			public void onError(Throwable e) {
		
				
			}

			@Override
			public void onComplete() {
		
				
			}
			
		};

		class IntegerHolder implements Holder {
			
			Integer item;
			
			Class intclass;
			
			Observer intObserver;
			
			public IntegerHolder() {
				intclass = Integer.class;
			}
			
			public IntegerHolder(int n) {
				intclass = Integer.class;
				item = n;
			}

			public Integer getItem() {
				return item;
			}

			public void setItem(Integer item) {
				this.item = item;
			}

			public Class getIntclass() {
				return intclass;
			}

			public void setIntclass(Class intclass) {
				this.intclass = intclass;
			}
			
			public IntegerHolder getIntegerObserver() {			    	
			    	return this;	
		    }
		}

		
		IntegerHolder  i1 = new IntegerHolder();
		
		Observable<IntegerHolder> iob = Observable.just(i1.getIntegerObserver());

		Observable<IntegerHolder> filteredObservable1 = iob.filter(e -> e.getItem() != null);
		
		Observable<IntegerHolder> filteredObservable2 = filteredObservable1.filter(e -> e.getItem() % 2 == 0);
				
		/*
		Observable<Integer> sourceObservable = Observable.range(1, 10);
		TestSubscriber<Integer> subscriber = new TestSubscriber();
		 
		Observable<Integer> filteredObservable = sourceObservable
		  .filter(i -> i % 2 != 0);
		*/
		
		filteredObservable2.subscribe(testObserver);					 
		
	}
	
	/*
	 *  Tests for top down rete without calling addTupleSink
	 *  
	 *  
	 *  
	 */
	int count = 0;
	@Test
	public void testAlphaNodeObservable() {
		
		ObjectTypeNode otn = new ObjectTypeNode(Person.class);
		
		AlphaNode an = new AlphaNode("name", "custName", COMPARATOR.EQUALS);

		Observable.just(otn).subscribe(an.getMonarchObserver());
		
		Set nodes = new HashSet();

		nodes.add(new Person("masko"));
			
		Observable ob1 = Observable.fromIterable(nodes);
		
//		ob1.subscribe(otn.getMonarchObserver());
		ob1.subscribe(an.getMonarchObserver());

		Observer testObserver = new Observer() {

			@Override
			public void onSubscribe(Disposable d) {
		

			}

			@Override
			public void onNext(Object t) {
				count++;		
				
				System.out.println("testobs t ="+ t);
				assert(t instanceof Person);
				assert(2 == count);
				
			}

			@Override
			public void onError(Throwable e) {
		
				
			}

			@Override
			public void onComplete() {
		
				
			}
			
		};
				
	//	Observable ob2 = Observable.just(ob1);
		
		ob1.subscribe(testObserver);
		
		Observable.just(an.getMonarchObserver()).subscribe(testObserver);
				
				
	}

    public static Observable getConnectableObservable(AlphaNode an) { 	
        
    	ConnectableObservable sdkNodeObservable = (ConnectableObservable) getObservable(an);
    	sdkNodeObservable.publish();
    	return sdkNodeObservable;
    }
	
	private static Observable getObservable(AlphaNode an) {

		return Observable.just(an.getMonarchObserver());
		/*
		return Observable.create(subscriber -> {
	 
	        subscriber.add(Subscriptions.create(() {
	        
	        }));
	    });
	    */
	}
	
	String ename = "sheeba";
	boolean done = false;
	
	/*
	 *  AlphaNode filtering test. Does not call addTupleSink, registerObservers etc.
	 *  while building rete. Use with caution. AlphaNode setParent is called quiet late
	 *  only during observer registration 
	 *  
	 *  	ob1.subscribe(otn.getObjectTypeObserver(an))
	 *  and construct is then called during setParent. 
	 *  
	 *  MonarchObserver.onNext is called immediately after which calls
	 *  alphaNode.onNext by which time construct should have finished.
	 *  This should happen quiet naturally and things should run fine.
	 *  
	 *  The benefit is you do not have to call addTupleSink for each node.
	 *  RegisterObservers is also called externally. So this method should really be used
	 *  only for testing.
	 *  
	 *  Even though the method name says ConnectableObservable, internally uses a filter similar to 
	 *  streams filter to filter nodes from reaching the next level based on some condition. This should, 
	 *  IMO, allow for a more natural flow such that when a condition is satisfied, the nodes flow through and 
	 *  otherwise do not.
	 *    
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAlphaNodeConnectableObservable() {

		
		System.out.println("commence test 2 ");

		ObjectTypeNode otn = new ObjectTypeNode(Person.class);

		AlphaNode an = new AlphaNode(Person.class);

		ObjectTypeNode ots = new ObjectTypeNode(Account.class);

		AlphaNode a2 = new AlphaNode(Account.class, new FieldValueComparator("name", "BCH1C", COMPARATOR.EQUALS));

		Set nodes = new HashSet();

		System.out.println("nodes len " + nodes.size());
				
		Observable ob1 = Observable.fromIterable(nodes);
		
	//	ob1.subscribe(otn.getMonarchObserver());
	
	//	for(Object o : nodes)
	//		subject.onNext(o);
		
		ConnectableObservable cob2 = ob1.publish();
		
		cob2.subscribe(an.getMonarchObserver());

		cob2.subscribe(a2.getMonarchObserver());

	//	Observable ob2 = Observable.just(otn.getMonarchObserver());
					
	//	ob2.compose(ObjectToAlpha.objectToAlpha())
		//     .filter(e -> ename.toString().equals("Heeba"))
	//	     	.subscribe(e -> an.getMonarchObserver().onNext(e));

	//	ob2.subscribe(an.getMonarchObserver());

		Observable ob3 = Observable.just(an.getMonarchObserver());

		ConnectableObservable cob3 = ob3.publish();

		JoinNode jc = new JoinNode(an, a2, new FieldNameComparator("name", "custName"));
		
		cob3.subscribe(jc.getMonarchObserver(an));

		Observable ob4 = Observable.just(a2.getMonarchObserver());

		ConnectableObservable cob4 = ob4.publish();

		cob4.subscribe(jc.getMonarchObserver(a2));
		
	//	ob3.compose(ObjectToAlpha.objectToAlpha())
	//		.subscribe(jc.getMonarchObserver());

	//	Observable.just(otn.getObjectTypeObserver()).subscribe(an.getMonarchObserver());		
		
	//	Observable.just(an.getMonarchObserver()).subscribe(jc.getMonarchObserver());		

		Observer testObserver = new Observer() {

			@Override
			public void onSubscribe(Disposable d) {
		

			}

			@Override
			public void onNext(Object t) {
				count++;		
				
				try {
					System.out.println("testobs only objectTypenode at first ="+ t.getClass().getName());
	
					System.out.println("testobs observer "+ t);

					if(t.getClass().isAssignableFrom(MonarchObserver.class)) {
	
						System.out.println("testobs observerx "+ t.getClass().getName());
						
						MonarchObserver mo = (MonarchObserver) t;
						
						System.out.println("testobs filter 1");
							
						SDKNode node = mo.getSDKNode();
						
						System.out.println("testobs filter 2");
				
						if(node != null)
							done = true;
						
						ClauseComparator comp = node.getClauseComparator();
	
						System.out.println("testobs filter 3");

						
					}

				} catch(Exception e) {
					e.printStackTrace();
				}
					

			}

			@Override
			public void onError(Throwable e) {
		
				
			}

			@Override
			public void onComplete() {
		
				
			}
			
		};
			
		
		try {
				Thread.sleep(1000);

			int ch;
			while ((ch = System.in.read()) != -1)
			{
			    if (ch != '\n' && ch != '\r')
			    {
			    	if(ch == 'y') {

			    		FactHandle otn1 = new ObjectTypeNode(new Person("masko"));

			    		FactHandle ots1 = new ObjectTypeNode(new Account("IBCC", "IBC228", "MHCB105A", "masko", 100.0));

						nodes.add(otn1);
	
						nodes.add(ots1);


			    		cob2.connect();
			    		cob3.connect();
			    		cob4.connect();

			    	}
			    }
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
	//	Observable ob2 = Observable.just(ob1);
		
		ob3.subscribe(testObserver);
		
	//	Observable.just(jc.getMonarchObserver()).subscribe(testObserver);

	//	Observable.just(JoinNode.getJoinObserver()).subscribe(testObserver);
		
	}

	public static class ObjectToAlpha implements ObservableTransformer<MonarchObserver, SDKNode> {
		 
	    public static ObjectToAlpha objectToAlpha() {
	        return new ObjectToAlpha();
	    }
	 
	    private ObjectToAlpha() {
	        super();
	    }
	 
		@Override
		public ObservableSource<SDKNode> apply(Observable<MonarchObserver> upstream) {
			return upstream.map(x -> x.getSDKNode());
		}
	}


	
	/*
	 *  AlphaNode filtering test. 
	 *  	   
	 *  This time calls addTupleSink which calls setParent and also construct which is the way it should be.
	 *  
	 *  Even though the method name says ConnectableObservable, internally uses a filter similar to 
	 *  streams filter to filter nodes from reaching the next level based on some condition. This should, 
	 *  IMO, allow for a more natural flow such that when a condition is satisfied, the nodes flow through and 
	 *  otherwise do not.
	 *    
	 */
	@Test
	public void testAlphaNodeAddTupleSink() {

		
		System.out.println("commence test 2 ");

		ObjectTypeNode otn = new ObjectTypeNode(Person.class);

		AlphaNode an = new AlphaNode(Person.class);

		ObjectTypeNode ots = new ObjectTypeNode(Account.class);

		AlphaNode a2 = new AlphaNode(Account.class, new FieldValueComparator("name", "BCH1C", COMPARATOR.EQUALS));

		otn.addTupleSink(an);
		
		ots.addTupleSink(a2);
		
		Set nodes = new HashSet();

		nodes.add(new Person("masko"));
			
		Observable ob1 = Observable.fromIterable(nodes);
		
		ob1.subscribe(otn.getMonarchObserver());

		Observable ob2 = Observable.just(otn.getMonarchObserver());

		ob2.compose(ObjectToAlpha.objectToAlpha())
		   //  .filter(e -> e.toString().equals("Heebeebs"))
		     	.subscribe(e -> an.getMonarchObserver().onNext(e));

		JoinNode jc = new JoinNode(an, a2, new FieldNameComparator("name", "custName"));

		an.addTupleSink(jc);
		a2.addTupleSink(jc);
		
		Observable ob3 = Observable.just(an.getMonarchObserver());
				
		ob3.subscribe(jc.getMonarchObserver());
		
	//	ob3.compose(ObjectToAlpha.objectToAlpha())
	//		.subscribe(jc.getMonarchObserver());

	//	Observable.just(otn.getObjectTypeObserver()).subscribe(an.getMonarchObserver());		
		
	//	Observable.just(an.getMonarchObserver()).subscribe(jc.getMonarchObserver());		

		Observer testObserver = new Observer() {

			@Override
			public void onSubscribe(Disposable d) {
		

			}

			@Override
			public void onNext(Object t) {
				count++;		
				
				try {
					System.out.println("testobs only objectTypenode at first ="+ t.getClass().getName());
	
					System.out.println("testobs observer "+ t);

					if(t.getClass().isAssignableFrom(MonarchObserver.class)) {
	
						System.out.println("testobs observerx "+ t.getClass().getName());
						
						MonarchObserver mo = (MonarchObserver) t;
						
						System.out.println("testobs filter 1");
							
						SDKNode node = mo.getSDKNode();
						
						System.out.println("testobs filter 2");
						
						ClauseComparator comp = node.getClauseComparator();
	
						System.out.println("testobs filter 3");

						
					}

				} catch(Exception e) {
					e.printStackTrace();
				}
					

			}

			@Override
			public void onError(Throwable e) {
		
				
			}

			@Override
			public void onComplete() {
		
				
			}
			
		};
				
	//	Observable ob2 = Observable.just(ob1);
		
		ob3.subscribe(testObserver);

		RTMNode rtmnode = new RTMNode("ATS");
		
		jc.addTupleSink(rtmnode);
		
	//	Observable.just(jc.getMonarchObserver()).subscribe(testObserver);

	//	Observable.just(JoinNode.getJoinObserver()).subscribe(testObserver);
		
	}

	
	
	
	/*
	 *  AlphaNode filtering test. 
	 *  	   
	 *  This time calls addTupleSink and registerObservers with no external observers.
	 *  This should work fine. lets see..
	 *    
	 */
	@Test
	public void testAlphaNodeAddTupleSinkAndRegisterObservers() {
		
		System.out.println("commence test 2 ");
		
		Session sess = EmersonSession.begin(SessionState.StatefulSession);
		
		Topology rete = sess.getWorkingMemory().getRete();
		
		ObjectTypeNode otn = new ObjectTypeNode(Person.class);		
				
	//	FactHandle otn = rete.newFactHandle(Person.class);
		
		AlphaNode an = new AlphaNode(Person.class);
		
		ObjectTypeNode ots = new ObjectTypeNode(Account.class);

	//	FactHandle ots = rete.newFactHandle(Account.class);

	//	AlphaNode a2 = new AlphaNode(Account.class, new FieldValueChecker("acnum", "BCH1C", COMPARATOR.EQUALS));

		AlphaNode a2 = new AlphaNode(Account.class, "${account.acnum} == 'BCH1C'");
				// new FieldValueComparator("acnum", "BCH1C", COMPARATOR.EQUALS));

		otn.addTupleSink(an);
		
		ots.addTupleSink(a2);
		
		System.out.println("adding object ");

		rete.assertFact(new ObjectTypeNode(new Person("gambos")));
		
		rete.assertFact(new ObjectTypeNode(
				new Account("ICICI Bank", "ICICI00001521", "BCH1C", "masko", 187624.57)));
		
		JoinNode jc = new JoinNode(an, a2, "${person.name} == ${account.custName}");
		//fieldnamecomparator
		
		an.addTupleSink(jc);

		a2.addTupleSink(jc);
		
		RTMNode rnode = new RTMNode("person account rule");

		jc.addTupleSink(rnode);

		
		rete.build();
		
	}
	
}
