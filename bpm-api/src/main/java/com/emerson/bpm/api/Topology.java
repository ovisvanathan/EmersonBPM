package com.emerson.bpm.api;

import java.util.List;

import org.apache.commons.collections4.map.MultiValueMap;

import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.model.Person;

import io.reactivex.rxjava3.annotations.NonNull;


public interface Topology {

	FactHandle getOrCreateOTN(Object ob1);

	List<FactHandle> getOTNodes();

	void build();

	List<SDKNode> getRTMNodes();

	void build(SDKNode rtmn1);

	void assertFact(FactHandle person);

	FactHandle newFactHandle(Class class1);

	@NonNull
	Iterable getNodesQueue();

	void connect();

	void setInlineFacts(MultiValueMap<String, List<REL>> relationFacts);

	void setCMPInlineFacts(MultiValueMap<String, List<REL>> cmpRelationFacts);

	void setAggregations(List aggregations);

	void setFactstores(List factstores);

}
