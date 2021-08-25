package com.emerson.bpm.schema.util;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.emerson.bpm.schema.ElemState;
import com.emerson.bpm.schema.SchemaTreeWalk;

public class SchemaUtils {
	
	
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

	public static Map getSchemaMap(String rulesDslSchema) {

		SchemaTreeWalk walker = new SchemaTreeWalk(rulesDslSchema);
		
		XSObject root = getSchemaRootElement(walker);

		XSElementDeclaration elem0 = null;
		if(root instanceof XSElementDeclaration)
			elem0 = (XSElementDeclaration) root;
		
		
		Stack stackOfElems = walker.findElement(root.getNamespaceItem().getSchemaNamespace(),
								elem0.getName(), elem0.getName());

		System.out.println("**************stackOfElems**********************");

		while(!stackOfElems.isEmpty()) {			
			ElemState esn = (ElemState) stackOfElems.pop();			
			
		//	System.out.println(" esnext2222222222 ::" + esn.elemName);

			/*
			elemTreeMap = new HashMap();
			
			if(prevMap != null) {
				System.out.println(" esprev ::" + prev.elemName);
				elemTreeMap.put(esn, prevMap);
			} else
				elemTreeMap.put(esn, null);
			*/
	//		System.out.println("celems  ::");

	//		if(esn.elems != null) 
	//			for(String s : esn.elems)
	//				System.out.print("::" + s + "::, ");

	//		System.out.println(" -- ");
			
		//	prev = esn;
		//	prevMap = elemTreeMap;			
		}			

		
	//	Set set = elemsInfo.keySet();
	//	Iterator iter = set.iterator();

		//ifdebug 
		/*
		while(iter.hasNext()) {
			String key = (String) iter.next();
			System.out.println("key =" + key);
			List childList = (List) elemsInfo.get(key);

			if(childList != null) {
				System.out.println("childElems =" + childList);
	//			System.out.println("childAttrs =" + childList.get(1));
			}
		}
		*/				
		
		return null;
	}
	
	private static XSObject getSchemaRootElement(SchemaTreeWalk walker) {
		XSModel model = walker.getModel();					
		XSNamedMap elemMap = model.getComponents(XSConstants.ELEMENT_DECLARATION);
		if (elemMap.getLength() != 0) {
			for (int i = 0; i < elemMap.getLength(); i++) {
				XSObject item = elemMap.item(i);
	//			System.out.println("{" + item.getNamespace() + "}" + item.getName());
				return item;					
			}
		}
		return null;
	}

	public static void debugTreeMap(Map<ElemState, Map> elemTreeMap) {
		Set keyset = (Set) elemTreeMap.keySet();		
		Iterator ix = keyset.iterator();
		while(ix.hasNext()) {

			ElemState em = (ElemState) ix.next();		
 			System.out.println("Pkey =" + em.elemName);					
 			
 			for(String s : em.elems) {
 	 			System.out.print("=====");		
 	 			System.out.println(s);					
 	 		}
 			
 			Map vals = elemTreeMap.get(em);
 			
 			if(vals == null)
 				return;
 			
 			System.out.println();					
 	 	 		
 			debugTreeMap(vals);
 						
		}		
	}

	
}