package com.emerson.bpm.nodes.match.el.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.inject.Inject;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.OrderedSet;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.nodes.match.QueryData;
import com.emerson.bpm.nodes.match.SequenceSet;
import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.nodes.react.PrototypeMap;
import com.emerson.bpm.sql.JoinInfo;
import com.emerson.bpm.sql.QbSQLFactory;
import com.emerson.bpm.sql.QbSQLSelect;
import com.emerson.bpm.sql.QbWhereOperator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.picasso.paddle.inject.BeanGetters;

public class ELUtils {

    static Map<String, Integer> elemHash = new HashMap(); 

	@Inject
	static BeanGetters beanGetters;
	
	public static Method findGetMethod(Object item, String propertyName) {
		
		if(item instanceof Class) {
			
			Class klazz = (Class) item;
			
			return beanGetters.findGetMethod(klazz, propertyName);
		} else {
				
			Class klazz = item.getClass();
			
			return beanGetters.findGetMethod(klazz, propertyName);
						
		}		
	}
	
	public static Map<Class, Method []> getBeanGettersMap() {
		return beanGetters.getBeanGettersMap();			
	}

	public static String toLowerCaseFChar(String sname) {

		
		return null;
	}

    public static String replaceStrings(String s) {
   	 
   	 StringTokenizer Tok = new StringTokenizer(s, " ");
        int n=0;

        StringBuffer sbuf = new StringBuffer();
        while (Tok.hasMoreElements()) {
                String elem = (String) Tok.nextElement();
        
        		try {
        					Integer.parseInt(elem);

        					sbuf.append(elem);
        	                sbuf.append(" ");

        		} catch (NumberFormatException e) {
        			
        			int idx = ResolverOps.getOperator(elem);
        			
        			if(idx != -1) {
     					sbuf.append(elem);
    	                sbuf.append(" ");      				
        			} else {
        			
        					if(elem.equals("(") || elem.equals(")")) {
             					sbuf.append(elem);
            	                sbuf.append(" ");      				
        					}
        					else {
        								
										int hash = 0;						
										elem = elem.replaceAll(elem, "\"");
										elem = elem.replaceAll(elem, "\'");
					
				         				if(!elemHash.containsKey(elem)) {
											hash = stringToHash(elem);		
				         					elemHash.put(elem, hash);
				         				} else
											hash = elemHash.get(elem);		

			         					sbuf.append("" + hash);
			         					sbuf.append(" ");				                		

									}

        					}
        		}
        }
        		return sbuf.toString();
    }

    public static int stringToHash(String s) {
	     int hash = 7;
	     for (int i = 0; i < s.length(); i++) {
	         hash = hash*31 + s.charAt(i);
	     }
    
	     return hash;
    }

	public static String toELName(Class nodeClass) {
		return String.valueOf(
				Character.toLowerCase(nodeClass.getSimpleName().toCharArray()[0]));
	}

	
	@SuppressWarnings({ "unchecked", "unused" })
	public static Map<String, Object> convertPojoToMap(Object klazzObj) {
		
	//	Map<String, Object> item = cache.get("OTN_" + klazz.getName());
		Map<String, Object> item = null;
		if(item == null) {
		
			ObjectMapper objectMapper = new ObjectMapper();
			
			PrototypeMap map = objectMapper.convertValue(klazzObj, PrototypeMap.class);
			
			map.put("OTN_CLASSNAME", klazzObj.getClass().getName());			
			
			return map;
		} else {
			return item;

		}
		

	}


	public static QueryData convertSelectToQueryData(QbSQLSelect sel, boolean fetchTableObjects, boolean isJoinQuery) {
		
		QueryData qdata = new QueryData();
		if(fetchTableObjects) {
			Object [] tableObjs = sel.getTableObjects();			
			qdata.setTables(tableObjs);
		}
		
		System.out.println("fields");
	
		String [] fields = sel.getFieldNames();

		qdata.setFieldNames(fields);
		
		if(isJoinQuery) {		
			String [] flds = sel.getJoinFields();
			qdata.setJoinFields(flds);
						
			System.out.println("joins");
			List<JoinInfo> infos = sel.getJoinList();
			qdata.setMultiJoin(true);
			qdata.setJoinList(infos);
	
			String [] tableNames = sel.getJoinTables();
			qdata.setTableNames(tableNames);

		}
		
		QbWhere whr = (QbWhere) sel.getWhere();
		qdata.setWhere(whr);
		
		return qdata;
	}

	public static String createAlias(String objName, int nextVal) {

		int dpos = objName.lastIndexOf(".");
		
		if(dpos != -1) {
			
			objName = objName.substring(dpos+1);
			
			if(objName.length() > 15)
				objName = objName.substring(0, 7);
			
			objName += nextVal;
		}
				
		return objName;
	}

	/*
	 * User may provide values thus
	 * { name, CMP.equals, 'mAG' }
	 * OR in case theres many
	 * { { name, CMP.equals, 'mAG' }, { 'age' cmp.LT, 30 }, ... }
	 * 
	 */
	public static List<Object[]> convertObjectArrayToList(QbSQLFactory f, Object[] wargs) {

		List list = new ArrayList();
		
		int x=0;
		Object [] args = new Object[3];

		for(int i=0;i<wargs.length;i+=3)  {
			
			if(args == null)
				args = new Object[3];

			args[x++] = f.newStdField((String) wargs[i]);
			args[x++] = operatorAdapterFor((ClauseComparator) wargs[i+1]);
			args[x++] = wargs[i+1];
			
			if(x == 3) {
				x = 0;
				list.add(args);
				args = new Object[3];	
			}
		}
				
		return list;
	}

	/*
	 * Convert from ClauseComparator's COMPARATOR object to
	 * SQL QbWhereOperator
	 */
	public static QbWhereOperator operatorAdapterFor(ClauseComparator comparator) {

		switch(comparator.getValue()) {
		
		case COMPARATOR.EQUALS_VAL:
			return QbWhereOperator.EQUALS;			
		case COMPARATOR.GT_VAL:
			return QbWhereOperator.GREATER_THAN;
		case COMPARATOR.LT_VAL:
			return QbWhereOperator.LESS_THAN;
		case COMPARATOR.GTE_VAL:
			return QbWhereOperator.GREATER_THAN_EQUALS;
		case COMPARATOR.LTE_VAL:
			return QbWhereOperator.LESS_THAN_EQUALS;
		case COMPARATOR.NOT_EQUAL_VAL:
			return QbWhereOperator.NOT_EQUALS;
		
		}
		
		return null;
	}
	
	
	
	
	


}
