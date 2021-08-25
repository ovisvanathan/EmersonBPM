package com.emerson.bpm.dsl.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.map.MultiValueMap;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.CMPRelation;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.dsl.Relation;
import com.emerson.bpm.dsl.util.AssertionException;
import com.emerson.bpm.dsl.util.NetworkBuilder;
import com.emerson.bpm.engine.Rete;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.RTMNode;
import com.emerson.bpm.nodes.match.DateComparator;
import com.emerson.bpm.nodes.match.DefaultComparator;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.rel.CMPRelationNode;
import com.emerson.bpm.nodes.rel.RelationNode;

public class RelationDSLBuilder implements NetworkBuilder {

	private static final int MAX_ALPHANODE_LIMIT = 76;

	Topology rete;
	
	String id;

	private List<REL> relations = new LinkedList();

	private List<String> actors = new ArrayList();

	private MultiValueMap<String, List<REL>> stopConditions = new MultiValueMap();

	private MultiValueMap<String, List<REL>> relationFacts = new MultiValueMap();	
	
	
	private boolean UseFSMAlways;

	private List aggregations;

	private List factstores;

	private MultiValueMap<String, List<REL>> cmpRelationFacts;

	private List<REL> cmprelations;
	
	public RelationDSLBuilder(String id, Topology rete2) {
		this.id = id;
		this.rete = rete2;
	}

	@Override
	public NetworkBuilder newRule(String id) {
		return this;
	}

	@Override
	public NetworkBuilder newRule(String id, boolean skipCheck) {
		return this;
	}

	public NetworkBuilder actor(String actor, AutonomousActor a) {
		this.actors.add(actor);
		return this;
	}

	@Override
	public NetworkBuilder newAlphaNode(Class class1) {
		return this;
	}

	@Override
	public NetworkBuilder query(String string, Class... classes) {
		return this;
	}

	@Override
	public NetworkBuilder fieldAlias(String arg0, String arg1) {

		return this;
	}

	@Override
	public NetworkBuilder fieldsAlias(String string, String[] strings) {

		return this;
	}

	@Override
	public NetworkBuilder buildComposite() {

		return this;
	}

	@Override
	public NetworkBuilder fetch(String string, String string2) {

		return this;
	}

	@Override
	public NetworkBuilder withCredentials(String string, String string2) {

		return this;
	}

	@Override
	public NetworkBuilder withCriteria(String string, String string2) {

		return this;
	}

	@Override
	public NetworkBuilder group() {

		return this;
	}

	@Override
	public NetworkBuilder or() {

		return this;
	}

	@Override
	public NetworkBuilder range(Date today, String string, String string2) {

		return this;
	}

	@Override
	public NetworkBuilder allTrue() {

		return this;
	}

	@Override
	public NetworkBuilder endGroup() {

		return this;
	}

	@Override
	public NetworkBuilder sort() {

		return this;
	}

	@Override
	public NetworkBuilder max() {

		return this;
	}

	@Override
	public NetworkBuilder median() {

		return this;
	}

	@Override
	public NetworkBuilder min() {

		return this;
	}

	@Override
	public NetworkBuilder avg() {

		return this;
	}

	@Override
	public NetworkBuilder count() {

		return this;
	}

	@Override
	public Topology build() {

		boolean hasRelations;

		this.rete = new Rete();
		
		if(this.relations.size() > 0) {
			hasRelations = true;
		}
		
		REL [] relobjs = new REL[relations.size()];
		int i=0;
		for(REL r : this.relations) {			
			
			int num = r.getNumParams();
			
			boolean isAssoc = r.isAssociative();
			
			if(UseFSMAlways)
				createFSMNetwork(r, isAssoc, num);						
			else if(num < MAX_ALPHANODE_LIMIT)					
					createReteNetwork(r, isAssoc, num);			
			else					
				createFSMNetwork(r, isAssoc, num);
							
		}

		for(REL r : this.cmprelations) {			
			
			int num = r.getNumParams();
			
			boolean isAssoc = r.getParent() != null;
			
			if(UseFSMAlways)
				createFSMNetwork(r, isAssoc, num);						
			else if(num < MAX_ALPHANODE_LIMIT)					
					createReteNetwork(r, isAssoc, num);			
			else					
				createFSMNetwork(r, isAssoc, num);
							
		}
		
		
		bind();
		
		return rete;
	}
			
	
	private void bind() {

		rete.setAggregations(this.aggregations);
		
		rete.setInlineFacts(this.relationFacts);

		rete.setCMPInlineFacts(this.cmpRelationFacts);

		rete.setFactstores(this.factstores);
		
	}

	private Topology createFSMNetwork(REL r, boolean isAssoc, int num) {

		String relName = r.getName();
		String expr = r.getExpr();
				
		List<REL> relFacts = (List<REL>) this.relationFacts.get(r.getName());
		
		List<REL> stopConditions = (List<REL>) this.stopConditions.get(r.getName());

		
//		List<SDKNode> anodes = new LinkedList();
			
		List<AutonomousActor> params = r.getParams();
	
		if(params.size() >= 2) {
			
			AutonomousActor r1 = params.get(0);
			AutonomousActor r2 = params.get(1);
			
			ObjectTypeNode otn1 = new ObjectTypeNode(r);
			ObjectTypeNode otn2 = new ObjectTypeNode(r);
		
			JoinNode jc = new JoinNode(otn1, otn2, expr);
		
			otn1.addTupleSink(jc);
			otn2.addTupleSink(jc);			
		}								
		
		return rete;
	}
	
	
	private Topology createReteNetwork(REL r, boolean isAssoc, int num) {			
			
		String relName = r.getName();
		String expr = r.getExpr();
		
		//	relobjs[i].setNumParams(r.getNumParams());
		//	relobjs[i].setName(r.getName());
		//	relobjs[i].setAssociative(r.isAssociative());
						
			List<REL> stopConditions = (List<REL>) this.stopConditions.get(r.getName());


			if(this.relations.size() > 0) {
				for(REL rel : relations) {
	
					AlphaNode anode1 = new AlphaNode(rel);
	
					AlphaNode anode2 = null;
					if(isAssoc)
						anode2 = new AlphaNode(rel);
					
					SDKNode jc2 = null;
	
					jc2 = new JoinNode(relName, anode1, anode2, expr);
				
					anode1.addTupleSink(jc2);
					anode2.addTupleSink(jc2);
				
				}
			}
			
			if(this.cmprelations.size() > 0) {

				for(REL rel : cmprelations) {

					String relName2 = rel.getName();

					REL tmp = rel;
					REL parent = null;
					while( (parent = tmp.getParent()) != null)
						tmp = parent;
						
					assert(tmp != null);

					SDKNode ajc1 = null;				
					JoinNode jc2 = null;
					
					REL topmostParent = tmp;
					
					ajc1 = new AlphaNode(topmostParent);
	
					REL child = topmostParent.getChild();
				
					while(child != null) {

						if(child != null) {
	
							AlphaNode  anode2 = new AlphaNode(rel);
						
							jc2 = new JoinNode(relName, ajc1, anode2, expr);
					
							ajc1.addTupleSink(jc2);
							anode2.addTupleSink(jc2);

							ajc1 = jc2;
						}
					
						child = child.getChild();						
					}
					
					if(jc2 != null) {				
						RTMNode rnode = new RTMNode(relName2);					
						jc2.addTupleSink(rnode);
					}				
					
				}
			}
			
			
			/*

				int sz = anodes.size();
				for(int k=0;k<sz;k+=2) {
										
					SDKNode a1 = (AlphaNode) anodes.get(k);
	
					AlphaNode a2 = null;
					if(k+1 < sz)
						a2 = (AlphaNode) anodes.get(k+1);
				
					if(a2 == null) {
						jc3 = new JoinNode(relName, a1, jc2, expr);						
						a2 = (AlphaNode) jc3;
					} else	 {
						jc3 = new JoinNode(relName, a1, a2, expr);
					}
					
					a1.addTupleSink(jc3);
					a2.addTupleSink(jc3);
									
					jc2 = jc3;

					joinNodesList.add(jc3);
					
				}
									
				JoinNode jc4 = null;

				int m = joinNodesList.size();

				List<JoinNode> relJoins = new LinkedList();
				RTMNode rnode = null;

				if(m >= 2) {
				
					if(m % 2 == 0) {
						
						while(rnode == null) {
						
							for(int p=0;p<m;p+=2) {
								
								JoinNode jp1 = new JoinNode(joinNodesList.get(p),
										joinNodesList.get(p+1), expr);
															
								relJoins.add(jp1);
							}
							
							if(relJoins.size() == 1)
								rnode = new RTMNode(relName);							
						}
					
					} else {
						
						int p=0;
						while(rnode == null) {

							JoinNode jp1 = null;
							JoinNode jp2 = null;
							JoinNode jp3 = null;
							for(;p<m;p+=2) {
								
								if(p+1 < m)
									jp3 = new JoinNode(joinNodesList.get(p),
											joinNodesList.get(p+1), expr);
								else {
								
									jp1 = relJoins.get(relJoins.size()-1);
									
									jp2 = (JoinNode) joinNodesList.get(p);
									
									jp3 = new JoinNode(jp1,
											jp2, expr);
								
								}
									
								relJoins.add(jp3);							
							}
							
							if(relJoins.size() == 1)
								rnode = new RTMNode(relName);							
						}
					
					}

				}
				
				*/
				
							
		
		return rete;
	}

	@Override
	public NetworkBuilder acceptAll(boolean b) {

		return this;
	}

	@Override
	public NetworkBuilder acceptAny(boolean b) {

		return this;
	}

	@Override
	public NetworkBuilder acceptMax(DefaultComparator yesNoComparator) {

		return this;
	}

	@Override
	public NetworkBuilder acceptMin(DefaultComparator yesNoComparator) {

		return this;
	}

	@Override
	public NetworkBuilder acceptMedian(DefaultComparator yesNoComparator) {

		return this;
	}

	@Override
	public NetworkBuilder acceptAvg(DefaultComparator yesNoComparator) {

		return this;
	}

	@Override
	public NetworkBuilder acceptCount(DefaultComparator yesNoComparator) {

		return this;
	}

	@Override
	public NetworkBuilder validate(String predicateName, Object... args) throws AssertionException {

		return this;
	}

	@Override
	public NetworkBuilder validateFalse(String predicateName) throws AssertionException {

		return this;
	}

	@Override
	public NetworkBuilder inlinefact(String string, String string2) {

		return this;
	}

	@Override
	public NetworkBuilder where(DateComparator dateComparator) {

		return this;
	}

	@Override
	public NetworkBuilder join(DefaultComparator fieldNameComparator) {

		return this;
	}

	@Override
	public NetworkBuilder joinOther(DefaultComparator comp) {

		return this;
	}

	@Override
	public NetworkBuilder joinselect(DefaultComparator fieldNameComparator) {

		return this;
	}

	@Override
	public NetworkBuilder joinval(DefaultComparator comp) {

		return this;
	}

	@Override
	public NetworkBuilder where() {

		return this;
	}

	@Override
	public NetworkBuilder date(String string, String string2, String string3) {

		return this;
	}

	@Override
	public NetworkBuilder where(String string) {

		return this;
	}

	@Override
	public NetworkBuilder not() {

		return this;
	}

	@Override
	public NetworkBuilder and() {

		return this;
	}


	@Override
	public NetworkBuilder newAlphaNode(String arg0) {

		return this;
	}

	@Override
	public NetworkBuilder newRelation(String relName, int num, String expr, boolean isAssoc) {
		this.relations.add(new Relation(relName, num, expr, isAssoc));
		return this;
	}
	
	@Override
	public Topology stopWhen(REL ontopParam) {
		REL paramRel = ontopParam.getRelation();
		REL stored = this.relations.get(this.relations.indexOf(paramRel));
		assert(stored != null);
		assert(paramRel.getNumParams() == stored.getRelation().getNumParams());
		this.stopConditions.put(paramRel.getName(), ontopParam);
		return rete;
	}

	@Override
	public Topology stopWhen(String relName, String ...vals) {
		assert(this.relations.contains(new Relation(relName)));
		REL stoprel = new RelationNode(relName, vals);
		this.stopConditions.put(relName, stoprel);
		return rete;
	}

	@Override
	public NetworkBuilder fact(REL rel) {

		if(rel instanceof CMPRelation) {
			this.cmpRelationFacts.put(rel.getName(), rel);		
			return this;			
		}
		
		this.relationFacts.put(rel.getName(), rel);		
		return this;
	}

	@Override
	public NetworkBuilder fact(String relName, Object ...args) {

		if(this.cmprelations.size()>0) {
			
			for(REL r : cmprelations) {
				
				String rname = r.getName();
				
				if(rname.equals(relName)) {
					for(Object arg : args) {					
						ObjectTypeNode otn = new ObjectTypeNode(arg);
						RelationNode argNode = new CMPRelationNode(r, otn);				
						this.cmpRelationFacts.put(rname, argNode);
					}
				}			
			}
		}
				
		return this;
	}

	@Override
	public NetworkBuilder newCMPRelation(Relation rel) {
		this.cmprelations.add(rel);
		return this;
	}


}
