package com.emerson.bpm.nodes.cmp;

import java.util.List;
import java.util.Map;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.match.IncorrectParamsException;
import com.emerson.bpm.nodes.match.MissingFieldException;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.nodes.match.sql.QbFactory;
import com.emerson.bpm.nodes.match.sql.generic.QbFactoryImp;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.rel.SequenceFactory;
import com.emerson.bpm.util.IncorrectParamNamesException;


public class AlphaNodeComparator extends ReteNodeComparator {

	Class ob1;
	String key;
	Object value;
	//COMPARATOR cmp;
	
	SDKNode node;
	Topology rete;

	ClauseComparator cmp;
	
	public AlphaNodeComparator() {
	}
	
	/*
	public AlphaNodeComparator(Rete rete, Class ob1, COMPARATOR cmp, String key, Object value) {
		this.rete = rete;
		this.ob1 = ob1;
		this.key = key;
		this.value = value;
		this.cmp = cmp;
		
		ObjectTypeNode otn = rete.getOrCreateOTN(ob1);
		
		an = new AlphaNode(cmp, this.key,
					this.value, ob1);
		an.setParent(otn);
	}
	*/
	
	public SDKNode getRealNode() {
		return node;
	}

	/*
	public AlphaNodeComparator(Rete rete, Class ob1, COMPARATOR cmp) {
		this.rete = rete;
		this.ob1 = ob1;
		this.cmp = cmp;
		
		ObjectTypeNode otn = rete.getOrCreateOTN(ob1);
		
		an = new AlphaNode(cmp, ob1);
		an.setParent(otn);
	}
	*/

	public AlphaNodeComparator(Topology rete, Class ob1, ClauseComparator cmp) { //throws MissingFieldException, IncorrectParamsException {
		this.rete = rete;
		this.ob1 = ob1;
		this.cmp = cmp;
		
		ObjectTypeNode otn = (ObjectTypeNode) rete.getOrCreateOTN(ob1);
		
		node = new AlphaNode(cmp, otn);
		node.setParent(otn);
	
		try {
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
	
	@Override
	public void construct()  throws Exception {

		
		Object parent = getParent();
		
		Object [] fieldNames = this.cmp.getFieldNames();

		Object [] fieldArgs = this.cmp.getWhereArgs();
		
		QbFactory f = new QbFactoryImp();
		
		List<Object []> wlist = ELUtils.convertObjectArrayToList(f, fieldArgs);
							
		try {					
				
				Map<String, Class> mapFields = 
						EmersonUtils.doFieldSearch(new Object [] { parent }, fieldNames);

				node.setSequenceSet(SequenceFactory.constructSequenceSet(mapFields, node));
				
		} catch (IncorrectParamNamesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		node.registerObservers();

	}

	@Override
	public void addTupleSink(SDKNode recNode) {
		node.addTupleSink(recNode);
	}
	
}
