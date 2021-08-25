package com.emerson.bpm.util;


import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections4.Predicate;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.OrderedSet;
import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.functor.FunctorUtils;
import com.emerson.bpm.functor.IdentitiesPredicate;
import com.emerson.bpm.nodes.AlphaNode;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.match.MissingFieldException;
import com.emerson.bpm.nodes.match.QueryData;
import com.emerson.bpm.nodes.match.SequenceSet;
import com.emerson.bpm.nodes.match.TYPEID;
import com.emerson.bpm.nodes.match.el.util.ResolverOps;
import com.emerson.bpm.nodes.match.el.util.ResolverOps.FloatOperand;
import com.emerson.bpm.nodes.match.el.util.ResolverOps.IntegerOperand;
import com.emerson.bpm.nodes.match.el.util.ResolverOps.StackOperand;
import com.emerson.bpm.nodes.match.el.util.ResolverOps.StringOperand;
import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.nodes.react.ObjectTypeNode;
import com.emerson.bpm.sql.JoinInfo;
import com.emerson.bpm.sql.QbSQLFactory;
import com.emerson.bpm.sql.QbSQLSelect;
import com.emerson.bpm.sql.QbWhereOperator;


public class EmersonUtils implements UtilsServiceProvider {

	public static void printResult(SDKNode tuple) {

		System.out.println(tuple.getResult());
		
	}	
	
	public Class loadClass(String className) {
		
		Class cls = null;
		
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cls;
		
	}

	public Object loadClassObject(String className) {
		
		
		try {
		
				Class cls = Class.forName(className);

				return getClassInstance(cls);
			 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

	public Object getClassInstance(Class criteriaClass) {

		try {
			return criteriaClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	public Method beanGettersMapGet(Map map, Object fieldObj, String fieldName) {

		Method [] getters = (Method[]) map.get(fieldObj.getClass());		
		for(Method getter : getters) {			
			if(getter.getName().indexOf(fieldName) > 0) {
				return getter;
			}
		}
		
		return null;
	}
	*/

	public synchronized String stripBean(String fieldName1) {
		
		int n = fieldName1.indexOf(".");
		if(n > 0) {		
			return fieldName1.substring(n+1);			
		}
		return fieldName1;
	}

	
    
	/*
	 * This method climbs the Rete Tree to search for Alpha Node Classes
	 * that might contain the fieldNames specified in the Rules. It follows
	 * separation of concerns principle and only does the search for Classes at this time.
	 * The scanning of the classes to check if they contain the field and 
	 * retrievinbg its value etc. is done by another task.
	 * 
	 * Initially when called the SDKNode is single. However, if it is a join node,
	 * it splits into 2 other ALpha/Join Nodes. This method param accepts an array 
	 * in anticipation of this scenario.
	 * 
	 * @Param sdkNodes Array of Single SDKNode
	 * @Return List of Objects found
	 */	
	@SuppressWarnings("unchecked")
	public List<Class> doSearchForRootNode(SDKNode [] sdkNodes) {
	
		List<Class> sdkClasses = new ArrayList<>();
		for(SDKNode node : sdkNodes) {			
			if(node instanceof AlphaNode) {				
				ObjectTypeNode obnode = (ObjectTypeNode) node.getParent();
				sdkClasses.add(obnode.getObjectType());				
			} else if(node instanceof JoinNode) {				
				Object [] nodes = node.getParents().getItems();
				
				SDKNode [] sdnNodes = Arrays.copyOf(nodes,
						nodes.length, 
						SDKNode [].class);
				
				sdkClasses.addAll(doSearchForRootNode(sdnNodes));
			} 
		}
		
		return sdkClasses;	
	}

	/*
	 * Searches a list of classes for the specified fields
	 */
	public Map<String, Class> doFieldSearch(Object [] nodeObjects, Object [] fields) throws MissingFieldException {

		Map<String, Class> mapFields = new HashMap<String, Class>();
		for(Object s1 : fields) {
			String ss1 = (String) s1;
			Class fieldClass = doClassSearch(nodeObjects, ss1);

			if(fieldClass == null)
				throw new MissingFieldException("field name not found " + s1);
			mapFields.put(ss1, fieldClass);			
		}
		
		return mapFields;
	}
	
	private static Class doClassSearch(Object [] nodeObjects, String fldName) {
	
		for(Object cob : nodeObjects) {
			Class c = cob.getClass();
			Field [] fs = c.getFields();
			for(Field f : fs) {
				if(f.getName().equals(fldName)) {
					return c;
				}						
			}
		}
		return null;
	}


	public static List<Set> getEntityNodes(SDKNode anode) {
		
		List objlist = new LinkedList();
		Set<Class> objclasses = new LinkedHashSet();
		Set<String> objnames = new LinkedHashSet();

		RowTuple rtup = anode.getParents();
		
		SDKNode [] nodes = rtup.getItems();
		
		for(SDKNode inode : nodes) {
	
			if(inode instanceof JoinNode) {
				List xlist = getEntityNodes(inode);
			
				Set set0 = (Set) xlist.get(0);
				Set set1 = (Set) xlist.get(1);
				
				objclasses.addAll(set0);
				objnames.addAll(set1);
			}
				
			Class objclass = (Class) inode.getObjectType();
			
			String pname = null;
			if(objclass == null) {
				
				Object item = inode.getObject();
				
				if(item instanceof FactHandle) {
					FactHandle handle = (FactHandle) item;
					pname = (String) handle.getObject().getClass().getName();					
				} else if(item instanceof Class) {
					pname = (String) ((Class)item).getName();					
				} else	
					pname = (String) item.getClass().getName();					
				
				objnames.add(pname);					
			}
				
			objclasses.add(objclass);
		}

		objlist.add(objclasses);
		objlist.add(objnames);

		return objlist;
	}

    public FactHandle getObjectNodeForClassNode(SDKNode node) {
    	
    	if(node instanceof AlphaNode) {

        	Map<Class, FactHandle> nodesMap = node.getNodeObjects();
        	
        	Class nodeClass = node.getKlazz();
    	
        	return nodesMap.get(nodeClass);
        	
    	} else if(node instanceof ObjectTypeNode) {

        	Map<Class, FactHandle> nodesMap = node.getNodeObjects();

    		return nodesMap.get(node.getObjectType());
    		
    	}
    	
    	return null;
    	
    }



	
	public static URL loadResource(String rulesDslSchema) {
		
		try {
			return EmersonUtils.class.getClassLoader().getResource(rulesDslSchema);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Document loadDOM(InputStream openStream) {

		  try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder        builder = factory.newDocumentBuilder();
				DOMImplementation      impl    = builder.getDOMImplementation();
				
		        // Create the document
		        return impl.createDocument(null, null, null);
		        
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		  return null;
	}



    @SuppressWarnings("rawtypes")
	public
	static void jaxbObjectToXML(Class cks) 
    {
        try {
        	

        	Object cksObject = null;

        	String sname = FunctorUtils.getSimpleName(cks);
        		try {
						cksObject = cks.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	
            JAXBContext jaxbContext = JAXBContext.newInstance(cks);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
   
            // To format XML
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
   
            //If we DO NOT have JAXB annotated class
            JAXBElement jaxbElement = 
              new JAXBElement( new QName("", sname), 
                        cks, 
                        cksObject);
               
            jaxbMarshaller.marshal(jaxbElement, System.out);
             
            //If we have JAXB annotated class
            //jaxbMarshaller.marshal(employeeObj, System.out);  
             
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    
	public static <T> T[] listToArray(Class<T> klazz, ArrayList listenerList) {

		final T[] a = (T[]) Array.newInstance(klazz, listenerList.size());

		return (T [] ) listenerList.toArray( a );
	}


	static List<String> strNames;
	static Optional<String> namestr = null;

	public String nextVar() {

		
		Map varNames = new HashMap();
		
		if(namestr == null) {
		
			Field [] fields = EmersonUtils.class.getDeclaredFields();
	
			Method [] methods = EmersonUtils.class.getDeclaredMethods();
	
			strNames = EmersonUtils.concatenateArrays(fields, methods);

			namestr = strNames.stream()
								.reduce( (p,q) -> p + q);
		}
		
		int n = namestr.get().length();
		
		String s = namestr.get();
		Random rand = new Random();
		
		String s1 = null;
		do {
			int guess1 = (int) rand.nextInt(n+1);
			
			int guess2 = (int) rand.nextInt(n+1);
					
			char c1 = s.charAt(guess1);
			char c2 = s.charAt(guess2);
			
			
			s1 = String.valueOf(c1);
			
			s1 = s1.concat(String.valueOf(c2));
		
			s1 += guess1;
			
			s1 += guess2;

		} while(varNames.containsKey(s1));
		
		varNames.put(s1, true);
		
		return s1;
	}


	private static List concatenateArrays(Field[] a, Method[] b) {

		int aLen = a.length;
		int bLen = b.length;

		    @SuppressWarnings("unchecked")
		    String [] c = new String[aLen + bLen];
			System.arraycopy(a, 0, c, 0, aLen);
		    System.arraycopy(b, 0, c, aLen, bLen);
	
		    return new ArrayList(Arrays.asList(c));	
	}

	public static Map<String, Object> convertPojos(Class [] class1) {

		Map<String, Object> modelMap = new LinkedHashMap();		
		Map<String, Object> pojosMap = new LinkedHashMap();		
		for(Class ck : class1) {					
			pojosMap.putAll(convertPojo2Map(ck));					
		}
		
		
		System.out.println(" sz = " + pojosMap.size());
		
		modelMap.put("model", pojosMap);					
		return modelMap;
	}

	public static Map<String, Object> convertPojo2Map(Class class1) {
		
		Map<String, Object> map = new HashMap();
		
		String cname = class1.getName();
				
		Map fieldsMap = new HashMap();
		map.put(cname, fieldsMap);
		
		Field [] fs = class1.getDeclaredFields();
			
		for(Field f : fs) {			
			
			String fname = f.getName();
			Class fclass = f.getType();

		//	System.out.println("fname =" + fname);
		//	System.out.println("fclass = " + fclass.getName() );

			
			if(isNameInExclusionList(fname))
				continue;
			
			if(TYPEID.isPrimitiveType(fclass)) {				
				String ckName = fclass.getName(); 				
			//	int dpos = ckName.lastIndexOf(".");
				
			//	if(dpos != -1)
			//		ckName = ckName.substring(dpos+1);
				
				fieldsMap.put(fname, ckName);
			} else {				
			
				fieldsMap.put(fname, fclass.getName());				
				
				String fclassName = FunctorUtils.getSimpleName(fclass);
				fieldsMap.put(fclassName, convertPojo2Map(fclass));				
			
			}	
		}		
		
		return map;				
	}

	@SuppressWarnings("unchecked")
	private static boolean isNameInExclusionList(String fname) {

		
		String [] exclusions =  {
			"serialPersistentFields",
			"CASE_INSENSITIVE_ORDER",
			"serialVersionUID",
			"value",
			"hash"			
		};
		
		for(String item : exclusions)
			if(item.equals(fname))
				return true;
		
		return false;
	}


	public Map convertPojosToMap(Object [] pojos) {

		Map pojoMap = new HashMap();
		for(Object o : pojos) {		
			List fields = getFieldsList(o);			
			String sname = FunctorUtils.getSimpleName(o.getClass().getName());
			pojoMap.put(sname, fields);			
		}
		
		return pojoMap;
	}


	private static List getFieldsList(Object o) {

		List fieldsList = new ArrayList();

		Class c = o.getClass();
		
		Field [] fs = c.getDeclaredFields();
		
		for(Field f : fs) {			

			List<Object> fieldList = new ArrayList();

			String fname = f.getName();
			Class fclass = f.getType();
			
			if(isNameInExclusionList(fname))
				continue;
			
			fieldList.add(fname);
			fieldList.add(fclass.getName());
			
			Object value = null;
			try {
				
					Method [] mds = c.getMethods();
					
					for(Method m : mds) {
						
						String mname = m.getName();
						if(mname.startsWith("get")) {

							if(mname.substring(3).equalsIgnoreCase(fname)) {
								value =  m.invoke(o);
								fieldList.add(value);							
								break;
							}
							
						}
						
					}
					
					
				if(fieldList.size() == 2)
					fieldList.add("null");

			}catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fieldList.add("");				
			} 

			fieldsList.add(fieldList);

		}		
					
		return fieldsList;
	}


	private static String replaceNull(String valueOf) {
		return valueOf == null? "" : valueOf;
	}


	  /**
     * Converts an array of Class names to Class types
     * @param s
     * @return The array of Classes
     * @throws ClassNotFoundException
     */
    public Class[] toTypeArray(String[] s) throws ClassNotFoundException {
        if (s == null)
            return null;
        Class[] c = new Class[s.length];
        for (int i = 0; i < s.length; i++) {
            c[i] = Class.forName(s[i]);
        }
        return c;
    }

    /**
     * Converts an array of Class types to Class names
     * @param c
     * @return The array of Classes
     */
    public String[] toTypeNameArray(Class[] c) {
        if (c == null)
            return null;
        String[] s = new String[c.length];
        for (int i = 0; i < c.length; i++) {
            s[i] = c[i].getName();
        }
        return s;
    }

    static int Atoi(char[] str)
    {
        int sign = 1, base = 0, i = 0;
 
        // if whitespaces then ignore.
        while (str[i] == ' ')
        {
            i++;
        }
 
        // sign of number
        if (str[i] == '-' || str[i] == '+')
        {
            sign = 1 - 2 * (str[i++] == '-' ? 1 : 0);
        }
 
        // checking for valid input
        while (i < str.length
               && str[i] >= '0'
               && str[i] <= '9') {
 
            // handling overflow test case
            if (base > Integer.MAX_VALUE / 10
                || (base == Integer.MAX_VALUE / 10
                    && str[i] - '0' > 7))
            {
                if (sign == 1)
                    return Integer.MAX_VALUE;
                else
                    return Integer.MIN_VALUE;
            }
            base = 10 * base + (str[i++] - '0');
        }
        return base * sign;
    }
 
    
     public StackOperand strToType(String s) {
    	 	Object d = null;

    	 	if(s.startsWith("\"") || s.startsWith("\'")){ 
					return new StringOperand(s);	
    	 	}
    	 	
    	 	if(s.indexOf("\\.") != -1) {
    	 		
    	 		try {				
    	 					d = Double.parseDouble(s);
    	 					return new FloatOperand(d);	
    	 		} catch (NumberFormatException e) {
					e.printStackTrace();
					return new StringOperand(s);	
    	 		}
    	 		
    	 	} else {
    	 	
    	 			try {
						d = Integer.parseInt(s);
						return new IntegerOperand((int) d);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return new StringOperand(s);
					}    	 		
    	 	}
   
    }
     
    static Map<String, Integer> elemHash = new HashMap(); 

     public String replaceStrings(String s) {
    	 
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


	@Override
	public Locale getCurrentLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.emerson.bpm.api.UtilsServiceProvider#getPredicateName(com.emerson.bpm.api.SDKNode)
	 * 
	 * @Param SDKNode usually a RTMNode aka RuleTerminalNode
	 * 
	 * This method attempts to guessa valid predicate name depending on the supplied node
	 * 
	 */
	@Override
	public Predicate getPredicate(String prefix, String objectName, SDKNode rtmNode, String suffix) throws Exception {

		RowTuple parents = rtmNode.getParents();
		
		SDKNode [] items = parents.getItems();

		StringBuffer sbuf = new StringBuffer();

		StringBuffer sbufObjNames = new StringBuffer();
		
		sbuf.append(replaceNull(prefix));
		sbuf.append(replaceNull(objectName));
		sbuf.append(replaceNull(suffix));
		
		List valueObjects = new ArrayList();
		for(SDKNode node : items) {
			
			if(node instanceof JoinNode) {
				
				SDKNode left = node.getLeft();
				
				SDKNode right = node.getRight();

				FactHandle fct = (FactHandle) left.getObject();
			
				Object item = fct.getObject();
			
				valueObjects.add(item);
			}
			
		}
		
		return new IdentitiesPredicate(sbuf.toString(), 
					(Object []) valueObjects.toArray(new Object [] {}));

	}


	
	
	
}
