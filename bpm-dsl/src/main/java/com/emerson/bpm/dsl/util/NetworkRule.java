package com.emerson.bpm.dsl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.RuleQuery;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.WhereClause;
import com.emerson.bpm.nodes.match.DateComparator;
import com.emerson.bpm.nodes.match.DoesNotExistsComparator;
import com.emerson.bpm.nodes.match.FieldJoinComparator;
import com.emerson.bpm.nodes.match.FieldListValueComparator;
import com.emerson.bpm.nodes.match.FieldNameComparator;
import com.emerson.bpm.nodes.match.FieldValueComparator;
import com.emerson.bpm.nodes.match.ValueComparator;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.util.ServiceFactory;

public class NetworkRule {

	boolean doPreCheck;
	String id;

	List<Class> queryEntities;

	List<RulePart> ruleParts;

	private String whenStr;

	UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	public enum RulePartType {
		ALPHA, JOIN, TEXT, WHERE, QUERY
	};


	SDKNode consequence;
	String queryName;

	public String getQueryName() {
		return queryName;
	}

	public List<RulePart> getRuleParts() {
		return ruleParts;
	}

	public void setRuleParts(List<RulePart> ruleParts) {
		this.ruleParts = ruleParts;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public SDKNode getConsequence() {
		return consequence;
	}

	public void setConsequence(SDKNode consequence) {
		this.consequence = consequence;
	}

	public List<Class> getQueryEntities() {
		return queryEntities;
	}

	public void setQueryEntities(List<Class> queryEntities) {
		this.queryEntities = queryEntities;
	}

	List<Class> entities;

	public List<Class> getEntities() {
		return entities;
	}

	public void setEntities(List<Class> entities) {
		this.entities = entities;
	}

	public NetworkRule(String id) {
		this(id, false);
	}

	public NetworkRule(String id, boolean doPreCheck) {
		this.id = id;
		this.doPreCheck = doPreCheck;
	}

	public void addRulePart(RulePart rulePart) {
		this.ruleParts.add(rulePart);
	}

	
	public class RulePart {

		String ruleText;

		Object node;
		boolean isAlpha;

		NetworkRule parent;
		
		RulePartType rulePartType;

		List imports = new ArrayList();;
		StringBuffer queryBuf = new StringBuffer();

		Map<String, String> variablesMap = new HashMap();

		public RulePart(AlphaNode anode) {
			this.node = anode;

			this.rulePartType = RulePartType.ALPHA;
		
		//	new Thread(() -> {
				doAlpha();
		//	}).start();

		}

		// convert alphanode to bean instance, and text
		private void doAlpha() {

			ObjectTypeNode otn1 = (ObjectTypeNode) ((SDKNode) node).getParent();

			Object item = otn1.getObject();

			Map map = (Map) item;
			if (item.getClass().isAssignableFrom(Map.class)) {

				String klazzName = (String) map.get("OTN_CLASSNAME");

				String kzName = klazzName.substring(klazzName.lastIndexOf(".") + 1);

				imports.add(klazzName);

				variablesMap.put(klazzName, EmersonUtils.nextVar());

				if (whenStr == null) {
					whenStr = "when ";
					queryBuf.append(whenStr);
				}

				queryBuf.append(" " + kzName + "() ");

			} else {

				String predicateName = (String) map.get("PRED_NAME");

				if (whenStr == null) {
					whenStr = "when ";
					queryBuf.append(whenStr);
				}

				queryBuf.append(predicateName);
			}

		}

		public RulePart(JoinNode jnode) {
			this.node = jnode;
			this.rulePartType = RulePartType.JOIN;

		//	new Thread(() -> {
				doJoin();
		//	}).start();


		}

		public RulePart(RuleQuery qnode) {
			this.node = qnode;
			this.rulePartType = RulePartType.QUERY;		
		}
		
		public RulePart(String text) {
			this.node = text;
			this.rulePartType = RulePartType.TEXT;

			new Thread(() -> {
				doText();
			}).start();

		}

		public RulePart(WhereClause wh6) {
			this.node = wh6;
			this.rulePartType = RulePartType.TEXT;	
			new Thread(() -> {
				doWhere();
			}).start();

		}

		private void doWhere() {

			
			
		}

		private void doText() {

		}

		private void doJoin() {

			SDKNode otnNode = ((SDKNode)this.node).getParent().getParent();
			
			if(otnNode instanceof ObjectTypeNode) {
				
				Map<String, Object> otnMap = (Map<String, Object>) otnNode.getObject();

				String klazzName = (String) otnMap.get("OTN_CLASSNAME");
			
				String varName = variablesMap.get(klazzName);
			
				JoinNode jnd = ((JoinNode)this.node);
				ClauseComparator defcom = jnd.getComparator();

				if(defcom instanceof FieldNameComparator) {

					String [] varNames = new String[2];

					String [] varFieldNames = new String[2];
					int k=0;

					Object [] fnames = defcom.getFieldNames();
					
					for(Object x : fnames) {
						
						String fname = (String )x;
						
						int dpos = fname.indexOf(".");
									
						String objName = null;
						String objFieldName = null;
						if(dpos > 0) {
						
							objName = fname.substring(0, dpos);
							
							objFieldName = fname.substring(dpos+1);
						
							Class objClass = ExpressionResolver.getObjectForPrefix(objName);
							
							String klazzName2 = objClass.getName();
		
							varNames[k] = (String) variablesMap.get(klazzName2);

							varFieldNames[k++] = objFieldName;
						}						
					}
					
				} else if(defcom instanceof FieldValueComparator) {
				
					//e.g "p.name, val, =" should become "p1.name = val" 
					// where p1 is the variable name in the rules file
					// p is the prefix for the expression evaluator
					Object [] fnames = defcom.getFieldNames();
					
					String fname = (String) fnames[0];
					
					int dpos = fname.indexOf(".");
					
					String objName = null;
					String objFieldName = null;
					if(dpos > 0) {
					
						objName = fname.substring(0, dpos);
						
						objFieldName = fname.substring(dpos+1);
					
						Class objClass = ExpressionResolver.getObjectForPrefix(objName);
						
						String klazzName2 = objClass.getName();

						objName = (String) variablesMap.get(klazzName2);

						objFieldName = objFieldName;
					}
					
					
					
					
				} else if(defcom instanceof FieldListValueComparator) {
					
					
					Object [] fnames = defcom.getFieldNames();
					
					String fname = (String) fnames[0];
					
					int dpos = fname.indexOf(".");
					
					String objName = null;
					String objFieldName = null;
					if(dpos > 0) {
					
						objName = fname.substring(0, dpos);
						
						objFieldName = fname.substring(dpos+1);
					
						Class objClass = ExpressionResolver.getObjectForPrefix(objName);
						
						String klazzName2 = objClass.getName();

						objName = (String) variablesMap.get(klazzName2);

						objFieldName = objFieldName;
					}
					
					
					
					
				} else if(defcom instanceof FieldJoinComparator) {

					Object [] fnames = defcom.getFieldNames();
					int len = fnames.length;
					String [] varNames = new String[len];
					String [] varFieldNames = new String[len];
					int k=0;
					for(Object x : fnames) {
						
						String fname = (String )x;
						
						int dpos = fname.indexOf(".");
									
						String objName = null;
						String objFieldName = null;
						if(dpos > 0) {
						
							objName = fname.substring(0, dpos);
							
							objFieldName = fname.substring(dpos+1);
						
							Class objClass = ExpressionResolver.getObjectForPrefix(objName);
							
							String klazzName2 = objClass.getName();
		
							varNames[k] = (String) variablesMap.get(klazzName2);

							varFieldNames[k++] = objFieldName;
						}
						
					}

				
				
				} else if(defcom instanceof DoesNotExistsComparator) {

					
				} else if(defcom instanceof DateComparator) {
						
				} else if(defcom instanceof ValueComparator) {
						
				}
				
				queryBuf.append("$" + varName );
		
			}
		}

		public String getRuleText() {
			return ruleText;
		}

		public void setRuleText(String ruleText) {
			this.ruleText = ruleText;
		}

		public Object getNode() {
			return node;
		}

		public void setNode(SDKNode node) {
			this.node = node;
		}

		public boolean isAlpha() {
			return isAlpha;
		}

		public void setAlpha(boolean isAlpha) {
			this.isAlpha = isAlpha;
		}

	}


	static class ExpressionResolver {

		public static Class getObjectForPrefix(String objName) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
