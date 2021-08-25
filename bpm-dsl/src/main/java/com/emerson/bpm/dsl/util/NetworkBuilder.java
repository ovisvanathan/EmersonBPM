package com.emerson.bpm.dsl.util;

import java.util.Date;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.dsl.Relation;
import com.emerson.bpm.nodes.match.DateComparator;
import com.emerson.bpm.nodes.match.DefaultComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.rel.RelationNode;
import com.emerson.bpm.util.Duration;
import com.emerson.bpm.util.Duration.Unit;

public interface NetworkBuilder {

	public NetworkBuilder newRule(String id);

	public NetworkBuilder newRule(String id, boolean skipCheck);

	public NetworkBuilder newAlphaNode(Class class1);

//	public NetworkBuilder newAlphaNode(String arg0, Class... c);

	public NetworkBuilder query(String string, Class... classes);

	public NetworkBuilder fieldAlias(String arg0, String arg1);

	public NetworkBuilder fieldsAlias(String string, String[] strings);

	public NetworkBuilder buildComposite();

	default public NetworkBuilder alias(String string) { return null; }

	public NetworkBuilder fetch(String string, String string2);

	public NetworkBuilder withCredentials(String string, String string2);

	public NetworkBuilder withCriteria(String string, String string2);

	default public NetworkBuilder consolidate(String string, Duration duration, Unit months) {
		return null;
	}

	public NetworkBuilder group();

	default public NetworkBuilder verify(Class<FieldValueComparator> class1, String string, COMPARATOR gt, int i) { return null; }

	public NetworkBuilder or();

	public NetworkBuilder range(Date today, String string, String string2);

	public NetworkBuilder allTrue();

	public NetworkBuilder endGroup();

	public NetworkBuilder sort();

	public NetworkBuilder max();

	public NetworkBuilder median();

	public NetworkBuilder min();

	public NetworkBuilder avg();

	public NetworkBuilder count();

	public Topology build();

	public NetworkBuilder acceptAll(boolean b);

	public NetworkBuilder acceptAny(boolean b);

	public NetworkBuilder acceptMax(DefaultComparator yesNoComparator);

	public NetworkBuilder acceptMin(DefaultComparator yesNoComparator);

	public NetworkBuilder acceptMedian(DefaultComparator yesNoComparator);

	public NetworkBuilder acceptAvg(DefaultComparator yesNoComparator);

	public NetworkBuilder acceptCount(DefaultComparator yesNoComparator);

	default public NetworkBuilder fact(String factName, Object... args)  { return null; }

	public NetworkBuilder fact(REL rel);
	
	public NetworkBuilder validate(String predicateName, Object... args) throws AssertionException;

	public NetworkBuilder validateFalse(String predicateName) throws AssertionException;

	default public NetworkBuilder filter(String string, String string2, COMPARATOR gt) {
		return null;
	}

	public NetworkBuilder inlinefact(String string, String string2);

	public NetworkBuilder where(DateComparator dateComparator);

	public NetworkBuilder join(DefaultComparator fieldNameComparator);

	public NetworkBuilder joinOther(DefaultComparator comp);

	public NetworkBuilder joinselect(DefaultComparator fieldNameComparator);

	public NetworkBuilder joinval(DefaultComparator comp);

	public NetworkBuilder where();

	public NetworkBuilder date(String string, String string2, String string3);

	public NetworkBuilder where(String string);

	public NetworkBuilder not();

	public NetworkBuilder and();

	default public String getText()  {	return null; }


	default public String getDRL() {	return null; }

	NetworkBuilder newAlphaNode(String arg0);

	public Topology stopWhen(String relName, String... val1);

	public NetworkBuilder newRelation(String relName, int i, String expr, boolean b);

	public NetworkBuilder actor(String string, AutonomousActor a);

	Topology stopWhen(REL ontopParam);

	public NetworkBuilder newCMPRelation(Relation rel);

}
