package com.emerson.bpm.dsl.util;

import java.util.function.Function;

import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.Record;

public interface NetworkBuilder2 {
	
	public Topology build();

	public NetworkBuilder2 bindVar(String string, String object);

	NetworkBuilder2 bind(String key, Record dslrec);

	NetworkBuilder2 fact(String key, Object val);

	public NetworkBuilder2 newQuery();

	public NetworkBuilder2 makeRule(String string, String string2);

	public NetworkBuilder2 makeRule(String string);

	public NetworkBuilder2 withCredentials(String key, String val);

	public NetworkBuilder2 withCriteria(Function<Object, Object> criteriaLambda);

	public NetworkBuilder2 fetchRule(String string, String string2) throws Exception;

	public NetworkBuilder2 stop();

	public NetworkBuilder2 verify(String string);

	public NetworkBuilder2 bindFunc(String string, String string2) throws Exception;

	public NetworkBuilder2 query(String queryName, Class... expr);



	
}
