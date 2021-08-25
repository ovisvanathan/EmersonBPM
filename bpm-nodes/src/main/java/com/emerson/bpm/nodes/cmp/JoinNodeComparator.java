package com.emerson.bpm.nodes.cmp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.EmptyBetaConstraints;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.match.FieldNameComparator;
import com.emerson.bpm.nodes.match.IncorrectParamsException;
import com.emerson.bpm.nodes.match.MissingFieldException;
import com.emerson.bpm.nodes.match.SequenceSet;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.react.ReactiveNode;
import com.emerson.bpm.nodes.rel.SequenceFactory;

public class JoinNodeComparator extends AlphaNodeComparator { // implements TupleSink {

	ReteNodeComparator a, b;

	JoinNode j1;
	
	SequenceSet sequenceSet;
	
	SDKNode left;
	SDKNode right;
	
	ClauseComparator cmp;

	boolean needsDeck = true;

	public JoinNodeComparator(AlphaNodeComparator a,
			AlphaNodeComparator b) {
		this.a = a;
		this.b = b;
		
	}

	/*
	 * For criteria based comparators
	 * @Param rete 
	 * @param The client class whose evaluate method we will call
	 * AlphaNodeComparator jc - Any tables, classes we will need to find
	 * CriteriaComparator - an instance of criteriaComparator usually FieldCriteriaComparator 
	 * 
	 * The code below looks up the client information from the supplied AlphaNode/JoinNode,
	 * which it then sets on the CriteriaComparator
	 * 
	 * The client need only supply the minimal information needed such as accountNUmber, name etc.
	 * which is then fed to the CriteriaComparator
	 * 
	 * If the client would like to fetch additional data e.g from db etc. then this api is not 
	 * suitable. Instead they should retrieve the data themselves from inside the ob1 class itself
	 * 
	 */
	public JoinNodeComparator(Topology rete, Class ob1, AlphaNodeComparator jc, ClauseComparator cmp) throws Exception {
		this.rete = rete;
		this.ob1 = ob1;
		
		this.cmp = cmp;
		
		ObjectTypeNode otn = (ObjectTypeNode) rete.getOrCreateOTN(ob1);
		
		node = new AlphaNode(cmp, otn);
		node.setParent(otn);
		
		j1 = new JoinNode(node, jc.getRealNode(), cmp);	
		
		construct();
	}
	

	
	//simple conjunctive join node
	public JoinNodeComparator(Topology rete, AlphaNodeComparator a1, AlphaNodeComparator a2) throws Exception {

		SDKNode left = a1.getRealNode();
		SDKNode right = a2.getRealNode();

		this.cmp = new FieldNameComparator(COMPARATOR.EXISTS);
		
		j1 = new JoinNode(left, right, this.cmp);		
		
		construct();
	}

	@SuppressWarnings("unchecked")
	public JoinNodeComparator(Topology rete, AlphaNodeComparator a1, AlphaNodeComparator a2, 
			 	ClauseComparator fieldNameComparator) {
		
		try {
			this.left = a1.getRealNode();
			
			this.right = (a2 == null)?
				new EmptyBetaConstraints() : a2.getRealNode();

			this.cmp = fieldNameComparator;

			j1 = new JoinNode(left, right, fieldNameComparator);		
			
			construct();
		} catch (MissingFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectParamsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}

	@SuppressWarnings("unchecked")
	public JoinNodeComparator(Topology rete, SDKNode a1, SDKNode a2, 
			 	ClauseComparator fieldNameComparator) {
		
		try {
			this.left = a1.getRealNode();
			
			this.right = (a2 == null)?
				new EmptyBetaConstraints() : a2.getRealNode();

			this.cmp = fieldNameComparator;

			j1 = new JoinNode(left, right, fieldNameComparator);		
			
			construct();
		} catch (MissingFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectParamsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> JoinNodeComparator(Topology rete, AlphaNodeComparator a1, AlphaNodeComparator a2, COMPARATOR cmp,
			String leftFieldName, Object rightFieldName) throws Exception {
				
		SDKNode left = a1.getRealNode();
		
		SDKNode right = (a2 == null)?
			new EmptyBetaConstraints() : a2.getRealNode();
			  
	}
	
	@Override
	public void construct() throws Exception {
		
		List<Class> leftClasses = EmersonUtils.doSearchForRootNode( new SDKNode [] { this.left });

		List<Class> rightClasses = EmersonUtils.doSearchForRootNode(new SDKNode []  { this.right });

		List<Class> classesNodes = new ArrayList<>();
		classesNodes.addAll(leftClasses);
		classesNodes.addAll(rightClasses);
		
		if(this.needsDeck) {
		
			RowTuple tuple = getParents();

			Object [] tableObjs = tuple.getItems();
						
			Object [] fieldNames = this.cmp.getFieldNames();
								
			try {
			
					Map<String, Class> mapFields = EmersonUtils.doFieldSearch(tableObjs, fieldNames);

					node.setSequenceSet(SequenceFactory.constructSequenceSet(mapFields, node));
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}

		}
		
		if(node != null)
			node.registerObservers();
		
		j1.registerObservers();
	}

		


	boolean fieldCheck(SDKNode node, String field) {
		AlphaNode anode = (AlphaNode) node;				
		Class obclass = anode.getObjClass();
		
		System.out.println("objclass = "+obclass.getName());
		
		Field [] fs = obclass.getFields();			
		for(Field f : fs) {					
			if(f.getName().equals(field)) {
				return true;
			}
		}
		
		return false;
	}
	
	public SDKNode getRealNode() {
		return j1;
	}

	
	public void addTupleSink(ReactiveNode recNode) {
		j1.addTupleSink(recNode);
	}
	
	/*
	@Override
	public void addParent(SDKNode node) {

		
	}
	*/
}
