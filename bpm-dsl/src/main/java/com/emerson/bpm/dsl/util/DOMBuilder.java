package com.emerson.bpm.dsl.util;

import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.dsl.Relation;
import com.emerson.bpm.functor.FunctorUtils;
import com.emerson.bpm.nodes.match.DateComparator;
import com.emerson.bpm.nodes.match.DefaultComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.util.XMLUtils;
import com.emerson.bpm.xml.XJRDocument;
import com.emerson.bpm.xml.XJRDocumentBuilderFactory;

public class DOMBuilder implements NetworkBuilder {

	XJRDocument xjrdoc;

	Element stateNode;
	Element beans;
	Element predicates;
	Element modelsNode;
	Element factsNode;

	Queue<Element> nodesQueue = new ArrayBlockingQueue(100);

	Queue<Element> activationQueue = new ArrayBlockingQueue(100);

	NetworkRule networkRule;

	public DOMBuilder() {

		try {
			DocumentBuilderFactory dbf = XJRDocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			xjrdoc = (XJRDocument) builder.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create the root element node

		stateNode = xjrdoc.createElement("WMEState");
		xjrdoc.appendChild(stateNode);

		beans = xjrdoc.createElement("beans");
		stateNode.appendChild(beans);

		predicates = xjrdoc.createElement("predicates");
		stateNode.appendChild(predicates);

		factsNode = xjrdoc.createElement("facts");
		stateNode.appendChild(factsNode);

	}

	class NetworkRule {

		String name;
		boolean doPreCheck;
		String id;

		public NetworkRule(String id, String name) {
			this(id, name, false);
		}

		public NetworkRule(String id, String name, boolean doPreCheck) {
			this.name = name;
			this.id = id;
			this.doPreCheck = doPreCheck;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return this.name;
		}
	}

	public NetworkBuilder newRule(String id, String name) {
		networkRule = new NetworkRule(id, name);
		// networkRules.add(nwrule);
		return this;
	}

	public NetworkBuilder newRule(String id, String name, boolean skipCheck) {
		networkRule = new NetworkRule(id, name, skipCheck);
		// networkRules.add(nwrule);

		// rete = new Rete();

		return this;
	}

	@Override
	public NetworkBuilder newAlphaNode(Class class1) {

		Object bean = null;

		try {
			bean = class1.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		XMLUtils.registerLoadClasses(new Class[] {

		});

		Object klazzObj = null;
		try {
			klazzObj = class1.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> pojoMap = ELUtils.convertPojoToMap(klazzObj);

		String className = class1.getName();

		int dpos = className.lastIndexOf(".");

		String simpleName = className.substring(dpos + 1);

		Element modelNode = xjrdoc.createModelElement(simpleName);

		nodesQueue.add(modelNode);
		activationQueue.add(modelNode);
		return this;
	}

	@Override
	public NetworkBuilder newAlphaNode(String arg0) {

		Element modelNode = xjrdoc.createModelElement(arg0);
		nodesQueue.add(modelNode);
		activationQueue.add(modelNode);
		return this;
	}

	@Override
	public NetworkBuilder fieldAlias(String arg0, String arg1) {

		return null;
	}

	@Override
	public NetworkBuilder fieldsAlias(String string, String[] strings) {

		return null;
	}

	@Override
	public NetworkBuilder join(DefaultComparator comp) {

		Element joinNode = xjrdoc.createJoinElement(comp);

		Element mnode = nodesQueue.remove();
		joinNode.appendChild(mnode);

		while (nodesQueue.size() > 0) {
			Element knode = nodesQueue.remove();
			joinNode.appendChild(knode);
		}

		nodesQueue.add(joinNode);
		activationQueue.add(joinNode);

		return this;
	}

	@Override
	public NetworkBuilder buildComposite() {

		Element rtmNode = xjrdoc.createRTMElement(FunctorUtils.makePredicate(this.networkRule.getName()));

		stateNode.appendChild(rtmNode);

		Element jnode = nodesQueue.remove();
		rtmNode.appendChild(jnode);

		while (nodesQueue.size() > 0) {
			Element jknode = nodesQueue.remove();
			rtmNode.appendChild(jknode);
		}

		nodesQueue.add(rtmNode);

		return this;
	}

	@Override
	public NetworkBuilder alias(String string) {

		return null;
	}

	@Override
	public NetworkBuilder fetch(String string, String string2) {

		return null;
	}

	@Override
	public NetworkBuilder withCredentials(String string, String string2) {

		return null;
	}

	@Override
	public NetworkBuilder withCriteria(String string, String string2) {

		return null;
	}

	@Override
	public NetworkBuilder group() {

		return null;
	}

	@Override
	public NetworkBuilder verify(Class<FieldValueComparator> class1, String string, COMPARATOR gt, int i) {

		return null;
	}

	@Override
	public NetworkBuilder or() {

		return null;
	}

	@Override
	public NetworkBuilder range(Date today, String string, String string2) {

		return null;
	}

	@Override
	public NetworkBuilder allTrue() {

		return null;
	}

	@Override
	public NetworkBuilder endGroup() {

		return null;
	}

	@Override
	public NetworkBuilder sort() {

		return null;
	}

	@Override
	public NetworkBuilder max() {

		return null;
	}

	@Override
	public NetworkBuilder median() {

		return null;
	}

	@Override
	public NetworkBuilder min() {

		return null;
	}

	@Override
	public NetworkBuilder avg() {

		return null;
	}

	@Override
	public NetworkBuilder count() {

		return null;
	}

	@Override
	public Topology build() {

		return null;

	}

	@Override
	public NetworkBuilder acceptAll(boolean b) {

		return null;
	}

	@Override
	public NetworkBuilder acceptAny(boolean b) {

		return null;
	}

	@Override
	public NetworkBuilder acceptMax(DefaultComparator yesNoComparator) {

		return null;
	}

	@Override
	public NetworkBuilder acceptMin(DefaultComparator yesNoComparator) {

		return null;
	}

	@Override
	public NetworkBuilder acceptMedian(DefaultComparator yesNoComparator) {

		return null;
	}

	@Override
	public NetworkBuilder acceptAvg(DefaultComparator yesNoComparator) {

		return null;
	}

	@Override
	public NetworkBuilder acceptCount(DefaultComparator yesNoComparator) {

		return null;
	}

	@Override
	public NetworkBuilder fact(String factName, Object... args) {

		return null;
	}

	@Override
	public NetworkBuilder validate(String predicateName, Object... args) throws AssertionException {

		return null;
	}

	@Override
	public NetworkBuilder validateFalse(String predicateName) throws AssertionException {

		return null;
	}

	@Override
	public NetworkBuilder newRule(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder newRule(String id, boolean skipCheck) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	@Override
	public NetworkBuilder newAlphaNode(String arg0, Class... c) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

	@Override
	public NetworkBuilder query(String string, Class... classes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder filter(String string, String string2, COMPARATOR gt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder inlinefact(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder where(DateComparator dateComparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder joinOther(DefaultComparator comp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder joinselect(DefaultComparator fieldNameComparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder joinval(DefaultComparator comp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder where() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder date(String string, String string2, String string3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder where(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder not() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder and() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDRL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder fact(REL rel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topology stopWhen(REL ontopAC) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder newRelation(String relName, int i, String expr, boolean b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder actor(String string, AutonomousActor a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Topology stopWhen(String relName, String... val1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NetworkBuilder newCMPRelation(Relation rel) {
		// TODO Auto-generated method stub
		return null;
	}

}
