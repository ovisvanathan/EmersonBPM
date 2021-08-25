package com.emerson.bpm.dsl.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.collections4.Predicate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.emerson.bpm.api.AutonomousActor;
import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.RuleQuery;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.Topology;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.dsl.Relation;
import com.emerson.bpm.dsl.util.AssertionException;
import com.emerson.bpm.dsl.util.FieldAlias;
import com.emerson.bpm.dsl.util.NetworkBuilder;
import com.emerson.bpm.dsl.util.NetworkRule;
import com.emerson.bpm.functor.FunctorUtils;
import com.emerson.bpm.functor.NamedPredicate;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.RTMNode;
import com.emerson.bpm.nodes.WhereClause;
import com.emerson.bpm.nodes.match.DateComparator;
import com.emerson.bpm.nodes.match.DefaultComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.match.el.AndPart;
import com.emerson.bpm.nodes.match.el.OrPart;
import com.emerson.bpm.nodes.match.el.WherePart;
import com.emerson.bpm.nodes.react.ObjectQueryNode;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.react.ReactiveNode;
import com.emerson.bpm.util.Duration;
import com.emerson.bpm.util.ServiceFactory;

public class RulesNetworkBuilder implements NetworkBuilder {

	private List<NetworkRule> networkRules;

	Document doc;

	Topology rete;

	Element contextNode;

	private List<ObjectTypeNode> otnNodes = new ArrayList();

	private List<SDKNode> alphaNodes = new ArrayList();

	private List<SDKNode> joinNodes = new ArrayList();

	private List<SDKNode> rtmNodes = new ArrayList();

	private List<FieldAlias> fieldAliases;

	private Queue nodesQueue = new ArrayBlockingQueue(100);

	protected NetworkRule currentRule;

	private List predicatesCollection = new ArrayList();

	private Session session;
	
	private int count;

	private boolean whereContext;

	private List<WherePart> whereParts;

	public RulesNetworkBuilder(String id) {

		session = ServiceFactory.getSession();
		// TODO Auto-generated constructor stub
//		createDOMDocumentFromScratch();
	}

	public NetworkBuilder newRule(String id) {
		this.currentRule = new NetworkRule(id);
		networkRules.add(currentRule);
		return this;
	}

	public NetworkBuilder newRule(String id, boolean skipCheck) {
		this.currentRule = new NetworkRule(id, skipCheck);
		networkRules.add(currentRule);

		rete = session.getWorkingMemory().getRete();

		return this;
	}
	
	public NetworkBuilder newAlphaNode(Class class1) {
		return this;
	}

	
	/*
	public NetworkBuilder newAlphaNode(Class class1) {

		
		Map<String, Object> beanPropsMap = FunctorUtils.convertPojoToMap(class1);
		
		ObjectTypeNode otn1 = new ObjectTypeNode(beanPropsMap);
		AlphaNode aph2 = new AlphaNode();
		otn1.addTupleSink(aph2);

		currentRule.addRulePart(currentRule.new RulePart(aph2));

		SDKNode rtmNode = null;
		SDKNode jtnNode = null;
		while (nodesQueue.size() > 0) {
			rtmNode = (SDKNode) nodesQueue.remove();
			jtnNode = new JoinNode(rtmNode, aph2);
			rtmNode.addTupleSink(jtnNode);
			aph2.addTupleSink(jtnNode);
			nodesQueue.add(jtnNode);
			return this;
		}

		nodesQueue.add(aph2);
		return this;
	}
	*/

	/*
	 * public NetworkBuilder newAlphaNode(String arg0) {
	 * 
	 * ObjectTypeNode otn1 = new ObjectTypeNode(arg0); AlphaNode aph2 = new
	 * AlphaNode(); otn1.addTupleSink(aph2);
	 * 
	 * currentRule.addRulePart(new RulePart(aph2));
	 * 
	 * SDKNode rtmNode = null; SDKNode jtnNode = null; while(nodesQueue.size() > 0)
	 * { rtmNode = nodesQueue.remove(); jtnNode = new JoinNode(rtmNode, aph2);
	 * rtmNode.addTupleSink(jtnNode); aph2.addTupleSink(jtnNode);
	 * nodesQueue.add(jtnNode); return this; }
	 * 
	 * nodesQueue.add(aph2); return this;
	 * 
	 * }
	 */

	/*
	public NetworkBuilder newAlphaNode(String arg0, Class... cls) {

		ObjectTypeNode otn1 = new ObjectQueryNode(arg0);
		AlphaNode aph2 = new AlphaNode();
		otn1.addTupleSink(aph2);

		currentRule.addRulePart(currentRule.new RulePart(aph2));

		SDKNode rtmNode = null;
		SDKNode jtnNode = null;
		while (nodesQueue.size() > 0) {
			rtmNode = (SDKNode) nodesQueue.remove();
			jtnNode = new JoinNode(rtmNode, aph2);
			rtmNode.addTupleSink(jtnNode);
			aph2.addTupleSink(jtnNode);
			nodesQueue.add(jtnNode);
			return this;
		}

		nodesQueue.add(aph2);
		return this;

	}
	*/

	/*
	 * To support adding Maps in place of classes
	 */
	@Override
	public NetworkBuilder newAlphaNode(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	public NetworkBuilder query(String queryName, Class... refClasses) {

		newAlphaNode(queryName, refClasses);

		Class[] arg0 = refClasses;

		List<Class> setCls = new LinkedList<Class>(Arrays.asList(refClasses));

		currentRule.setQueryName(queryName);

		currentRule.setQueryEntities(setCls);

		return this;

	}
	*/

	public NetworkBuilder fieldAlias(String arg0, String arg1) {

		// Class nodeClass = contextNode.getParent().getObject().getClass();

		// FieldAlias fa = new FieldAlias(nodeClass, arg0, arg1);

		// this.fieldAliases.add(fa);

		return this;
	}

	public NetworkBuilder fieldsAlias(String string, String[] strings) {

		return this;
	}

	public NetworkBuilder join(DefaultComparator comp) {

		int sz = nodesQueue.size();
		SDKNode e1 = (SDKNode) nodesQueue.remove();

		Element jnu4 = null;
		// SDKNode jx6 = null;
		while (nodesQueue.size() > 0) {

			SDKNode e2 = (SDKNode) nodesQueue.remove();
			JoinNode jx6 = new JoinNode(e1, e2, comp);

			currentRule.addRulePart(currentRule.new RulePart(jx6));

			e1.addTupleSink(jx6);
			e2.addTupleSink(jx6);

			e1 = jx6;
			nodesQueue.add(jx6);

		}

		return this;

	}

	@Override
	public NetworkBuilder joinOther(DefaultComparator comp) {
		join(comp);
		return this;		
	}

	@Override
	public NetworkBuilder joinselect(DefaultComparator comp) {
		join(comp);
		return this;		
	}

	@Override
	public NetworkBuilder joinval(DefaultComparator comp) {
		join(comp);
		return this;		
	}

	@Override
	public NetworkBuilder where(DateComparator dateComparator) {
		return this;
	}

	/*
	 * 
	 */
	public NetworkBuilder buildComposite() {

		ObjectTypeNode otn;
		AlphaNode a;
		JoinNode j;

		//first to do all where clauses
		
		int sz = nodesQueue.size();
		Object t1 = nodesQueue.remove();

		RuleQuery query = null;
		if(t1 instanceof WherePart) {

			WherePart e1 = (WherePart) t1;

			whereParts.add(e1);
			Element jnu4 = null;
			// SDKNode jx6 = null;
			while (nodesQueue.size() > 0) {
	
				WherePart e2 = (WherePart) nodesQueue.remove();				
				whereParts.add(e2);
					
			} 				
			WhereClause wh6 = new WhereClause(whereParts);
			currentRule.addRulePart(currentRule.new RulePart(wh6));
				
		} else if(t1 instanceof RuleQuery) {		
			query = (RuleQuery) t1;			
		}
		
		RTMNode rtmNode1 = new RTMNode(currentRule.getQueryName());

		currentRule.getQueryEntities();

		rtmNode1.setRuleQuery(query);
		
		Element rtm5 = doc.createElement("RTMNode");

		rtm5.setUserData("Data", rtmNode1, null);

		while (nodesQueue.size() > 0) {

			SDKNode jn56 = (SDKNode) nodesQueue.remove();

			jn56.addTupleSink(rtmNode1);
			rtmNode1.setParent(jn56);
		}

		ReactiveNode r;
		SDKNode node = rtmNode1.getParent();

		List<Class> entities = rtmNode1.getObjclasses();

		currentRule.setEntities(entities);

		currentRule.setConsequence(rtmNode1);

		return this;
	}

	public NetworkBuilder alias(String string) {

		return this;
	}

	public NetworkBuilder query(String queryName, Class ...queryClasses) {

		RuleQuery otn1 = new RuleQuery(queryName, queryClasses);
		currentRule.addRulePart(currentRule.new RulePart(otn1));		
		return this;
	}

	public NetworkBuilder fetch(String string, String string2) {

		return this;
	}

	public NetworkBuilder withCredentials(String string, String string2) {

		return this;
	}

	public NetworkBuilder withCriteria(String string, String string2) {

		return this;
	}

	public NetworkBuilder consolidate(String string, Duration duration, Duration.Unit months) {

		return this;
	}

	public NetworkBuilder group() {

		return this;
	}

	public NetworkBuilder verify(Class<FieldValueComparator> class1, String string, COMPARATOR gt, int i) {

		return this;
	}

	public NetworkBuilder or() {
		nodesQueue.add(new OrPart());
		return this;
	}

	public NetworkBuilder range(Date today, String string, String string2) {

		return this;
	}

	public NetworkBuilder allTrue() {
		return this;
	}

	public NetworkBuilder endGroup() {
		return this;
	}

	public NetworkBuilder sort() {

		return this;
	}

	public NetworkBuilder max() {
		return this;
	}

	public NetworkBuilder median() {
		return this;
	}

	public NetworkBuilder min() {

		return this;
	}

	public NetworkBuilder avg() {

		return this;
	}

	public NetworkBuilder count() {

		return this;
	}

	public Topology build() {

		Topology rete = session.getWorkingMemory().getRete();

		rete.build();

		int numrules = this.networkRules.size();

		boolean[] linksExist = new boolean[numrules];
		List[] linkedRules = new LinkedList[numrules];

		NetworkRule nwk = null;
		NetworkRule nwk2 = null;
		for (int i = 0; i < this.networkRules.size(); i++) {

			List rules = null;
			nwk = this.networkRules.get(i);
			String qname = nwk.getQueryName();

			for (int j = 0; j != i && j < this.networkRules.size(); j++) {

				nwk2 = this.networkRules.get(j);

				RTMNode mnode = (RTMNode) nwk2.getConsequence();

				String resultQueryName = mnode.getRuleQuery().getQueryName();

				if (qname.equals(resultQueryName))

					if (rules == null)
						rules = new LinkedList();

				rules.add(nwk2);

				nwk = nwk2;
			}

			if (rules.size() > 0) {
				linksExist[i] = true;
				linkedRules[i] = rules;
			}

		}

		// collect all entities referred in rules
		Set<Class> ruleEntities = new HashSet();
		for (int i = 0; i < this.networkRules.size(); i++) {
			NetworkRule nwk5 = this.networkRules.get(i);
			ruleEntities.addAll(nwk5.getEntities());
		}

		// identify which entities are referred in which rules
		// and build a dictionary

		Map<Class, List> klassEntitiesMap = new HashMap();
		for (Class ruleClass : ruleEntities) {

			for (int j = 0; j < this.networkRules.size(); j++) {

				NetworkRule nwk5 = this.networkRules.get(j);

				List<Class> nextEntities = nwk5.getEntities();

				for (Class entclass : nextEntities) {

					if (ruleClass == entclass) {

						if (klassEntitiesMap.containsKey(ruleClass)) {
							List rulenums = klassEntitiesMap.get(ruleClass);
							rulenums.add(j);
							klassEntitiesMap.put(ruleClass, rulenums);
						} else {
							List list = new ArrayList();
							list.add(j);
							klassEntitiesMap.put(ruleClass, list);
						}
					}

				}

			}

		}

		// collect all predicates referred in rule consequences
		// find out which rules refer to them in the condition part
		// and build a dict.
		List<SDKNode> rkNodes = rete.getRTMNodes();
		List rulesets = new LinkedList();

		Map<NetworkRule, List> rulesetMap = new HashMap();
		for (NetworkRule rule1 : networkRules) {

			if (!rulesetMap.containsKey(rule1)) {

				RTMNode rnode = (RTMNode) rule1.getConsequence();
				RuleQuery rq = rnode.getRuleQuery();
				int qhash1 = rq.getQueryHash();

				for (NetworkRule rule2 : networkRules) {

					if (rule1 != rule2) {

						// Entities contains same classes in
						// Query Entities + other entities present
						// in rules. To get only other entities
						// not part of the query, we need to get a diff
						String qry = rule2.getQueryName();
						List<Class> ets = rule2.getQueryEntities();

						List<Class> etsOther = new ArrayList(rule2.getEntities());

						etsOther.removeAll(ets);

						int rhash = FunctorUtils.calculateQueryHash(qry, ets);

						if (qhash1 == rhash) {

							List ruleset = new LinkedList();

							ruleset.add(rule1);
							ruleset.add(rule2);

							rulesetMap.put(rule1, ruleset);

						} else {
							List ruleset = rulesetMap.get(rule1);
							ruleset.add(rule2);
						}
						break;

					}

				}
			}

		}

		// next we collect all one-to-many and many-to-one
		// rule-predicate combinations.

		// first one-to-many. Same rule causes many different rules to fire

		return rete;

	}

	public NetworkBuilder acceptAll(boolean b) {
		return this;
	}

	public NetworkBuilder acceptAny(boolean b) {
		return this;
	}

	public NetworkBuilder acceptMax(DefaultComparator yesNoComparator) {
		return this;
	}

	public NetworkBuilder acceptMin(DefaultComparator yesNoComparator) {
		return this;
	}

	public NetworkBuilder acceptMedian(DefaultComparator yesNoComparator) {
		return this;
	}

	public NetworkBuilder acceptAvg(DefaultComparator yesNoComparator) {
		return this;
	}

	public NetworkBuilder acceptCount(DefaultComparator yesNoComparator) {
		return this;
	}

	/*
	 * An inline fact definition. A fact is a predicate meaning Only for the set of
	 * given data, the predicate is true. For all other data, it evaluates to false
	 * 
	 * A constant is OTH simply that. a definition that is constant. eg const int
	 * x=6 const double y=1.42 const String deadline = "30-07-2019" etc.
	 */

	public NetworkBuilder fact(String factName, Object... args) {
		NamedPredicate nmd = new NamedPredicate(factName, args);
		this.predicatesCollection.add(nmd);
		return this;
	}

	/*
	 * A known fact that should exist for the rule to succeed
	 */

	public NetworkBuilder fact(String factName) {
		NamedPredicate nmd = new NamedPredicate(factName);

		session.getWorkingMemory().getPredicates().add(nmd);
		return this;
	}

	public NetworkBuilder validate(String predicateName, Object... args) throws AssertionException {

		Predicate p = FunctorUtils.getPredicateByName(session, predicateName);

		if (!p.evaluate(args))
			throw new AssertionException("Eval returns false");
		return this;
	}

	public NetworkBuilder validateFalse(String predicateName) throws AssertionException {
		Predicate p = FunctorUtils.getPredicateByName(session, predicateName);

		if (p.evaluate(null))
			throw new AssertionException("Eval returns true");
		return this;

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

	/*
	 * all where statements are lazy evalued in buildComposite above
	 * 
	 * A hack to allow a where to do a date comparison.
	 * where called with no args implies the 
	 * following statement is a date or other comparison
	 */
	@Override
	public NetworkBuilder where() {
		this.whereContext = true;		
		return null;
	}

	@Override
	public NetworkBuilder date(String string, String string2, String string3) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 	 all where statements are lazy evalued in buildComposite above

	 *  Where is like a join except the fields may be literals
	 * variables maybe bean fields or literals. Or they must be 
	 * standalone variables, or predicates, elaborations etc.
	 * e.g volume > 30, contents != 0 etc
	 * 
	 * @Param expr an expression to be evaluated by an EL Processor
	 */
	@Override
	public NetworkBuilder where(String expr) {
		
		count++;
		nodesQueue.add(new WherePart(expr));
		return this;
		
	}

	@Override
	public NetworkBuilder and() {

		nodesQueue.add(new AndPart());
		return this;
	}

	@Override
	public NetworkBuilder not() {
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
