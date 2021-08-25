package com.emerson.bpm.nodes;

import java.util.ArrayList;
import java.util.List;

import com.emerson.bpm.action.Consequence;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.RuleQuery;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.TupleSink;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.event.StateChangeEvent;
import com.emerson.bpm.event.StateChangeListener;
import com.emerson.bpm.functor.IdentitiesPredicate;
import com.emerson.bpm.model.Action;
import com.emerson.bpm.nodes.react.MonarchObserver;
import com.emerson.bpm.nodes.react.ReactiveNode;
import com.emerson.bpm.nodes.row.RowUtils;
import com.emerson.bpm.subscribe.ObserverCallback;
import com.emerson.bpm.subscribe.ObserverEx;
import com.emerson.bpm.util.ServiceFactory;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 *
 * @author salaboy
 */
public class RTMNode extends RuleTerminalNode implements TupleSink, StateChangeListener, ObserverCallback {

    private Action action;
    
    RowTuple parents;
    
    static Object payload;

    RTM_STATE rtmState = RTM_STATE.HOLD;

    private ObserverEx rtmObserver;
            
	UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	
	List<ObserverEx> nodeObservers = new ArrayList();
	
    @SuppressWarnings("unchecked")
	public RTMNode(String rule) {
    	super(rule);
        
	//	Observable.just(AlphaNode.getMonarchObserver()).subscribe(getRTMObserver());
	//	Observable.just(JoinNode.getMonarchObserver()).subscribe(getRTMObserver());
	    	
    }

    @SuppressWarnings("unchecked")
	public RTMNode(String rule, Class [] list) {
    	super(new RuleQuery(rule, list));
        
	//	Observable.just(AlphaNode.getMonarchObserver()).subscribe(getRTMObserver());
	//	Observable.just(JoinNode.getMonarchObserver()).subscribe(getRTMObserver());
	    	
    }

    public RowTuple getParents() {
    	return this.parents;
    }
    
    public void constructNext() {

    	try {
			RowTuple rowtp = this.getParents();
			
			SDKNode [] parents = rowtp.getItems();
			
			for(SDKNode p : parents) {
				
				if(p instanceof JoinNode) {
			
					SDKNode left = p.getLeft();

					SDKNode right = p.getRight();
					
					((FactHandle) left.getObject()).getObject().getClass().getName();
					
					String itemName = ((FactHandle) left.getObject()).getObject().getClass().getSimpleName();
					
					String suffix = "Eligible";
					
					IdentitiesPredicate idenp = 
							new IdentitiesPredicate(
									EmersonUtils.getPredicate("Valid", itemName, this, suffix));
					
					Observable obx1 = Observable.just(p.getMonarchObserver(p.getLeft()));
				
					obx1.subscribe(this.getRTMObserver());
				
					Observable obx2 = Observable.just(p.getMonarchObserver(p.getRight()));
					
					obx2.subscribe(this.getRTMObserver());
		
					
					ObserverEx leftObserver = p.getMonarchObserver(left);

					ObserverEx rightObserver = p.getMonarchObserver(right);
									
					nodeObservers.add(leftObserver);
					nodeObservers.add(rightObserver);

				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
    	registerObservers(); 	
    }
	
	@Override
	public void registerObservers() {

    	SDKNode parent = getParent();
		
	//	obrtm.compose(ObjectToAlpha.objectToAlpha())
	//			.subscribe(this.getMonarchObserver());			
				 	
    	for(ObserverEx observer : nodeObservers) {    		
        	Observable obrtm = Observable.just(observer);
    		obrtm.subscribe(getRTMObserver());
    	}
    	
		
	}
    
	public Observer getRTMObserver() {
		if(this.rtmObserver == null)
			this.rtmObserver = getObserver();
		
		return this.rtmObserver;
	}
	
	private ObserverEx getObserver() {
		
		return new ObserverEx() {
	        @Override
	        public void onNext(Object value) {
	        	System.out.println("RTMnodeobserver onNext val =" + value);

	        	RTMNode.this.payload = value;
	        	
	        	fireStateChanged(new StateChangeEvent(null, RTMNode.this.payload));
	        	
	        }
	 
	        @Override
	        public void onError(Throwable e) {
	            System.out.println("error");
	        }
	 
	        @Override
	        public void onComplete() {
	//            System.out.println("Subscriber2 completed");
	        }

			@Override
			public void onSubscribe(Disposable d) {
		
				
			}
	    };
	}

	public void setParent(SDKNode node) {
		
		if(this.parents == null)
			this.parents = RowUtils.createRowTuple(parent, this);   	
		else {
			this.parents = RowUtils.createRowTuple(this.parents.getItems()[0], node, this);   	
		}
	}
	
	
	protected void fireStateChanged(StateChangeEvent stateChangeEvent) {
		this.onStateChange(stateChangeEvent);
	}

	public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//	@Override
	public void addChild(ReactiveNode aplNode) {

		
	}

	public void execute() {
		System.out.println(" got this val in run = " + this.payload);

		if(rtmState == RTM_STATE.READY) {		
			List<Consequence> cx = getConsequences();
			for(Consequence x : cx)
				x.execute();
			
		}

		rtmState = RTM_STATE.HOLD;
		
	}
	
   /*
    public void assertLeftTuple(LeftTuple leftTuple, PropagationContext context, WorkingMemory wm) {
        //Create ActivationItem and place it into the agenda that should be contained inside the working memory
        Agenda agenda = wm.getAgenda();
        agenda.addActivation(new Activation(this.rule, action, leftTuple));
        
    }
    */
    
    public ObserverEx getMonarchObserver() {

    	if(this.rtmObserver == null)
    		rtmObserver = new MonarchObserver(this, this.getClass());
    	
    	return rtmObserver;
    }

	@Override
	public void onStateChange(StateChangeEvent evt) {

		rtmState = RTM_STATE.READY;
		
	}
	
	
	public FactHandle getObject() {
		return (FactHandle) super.getObject();
	}
	
}
