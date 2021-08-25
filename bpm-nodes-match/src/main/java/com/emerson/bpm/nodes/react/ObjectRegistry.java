package com.emerson.bpm.nodes.react;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.emerson.bpm.api.FactHandle;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.subscribe.ObserverEx;


public class ObjectRegistry {

	static Map<Object, FactHandle> factsMap = new HashMap();

	private static ObserverEx observerEx;
	
	static Map<Class, ObserverEx> observersMap = new HashMap();
	
	public static FactHandle get(Object fact) {
	
		FactHandle otnNode = null;
		boolean isClass = false;
		try {
				if(fact instanceof java.lang.Class) {
					isClass = true;
					otnNode = new ObjectTypeNode(fact);
					factsMap.put(fact, otnNode);
				} else {
					isClass = false;					
					Class factClass = fact.getClass();
					if(factsMap.containsKey(factClass)) {				
						otnNode = factsMap.get(factClass);						
						ObserverEx classObserver = otnNode.getMonarchObserver();	
						ObjectTypeNode xobj = new ObjectTypeNode(fact);
				//		factsMap.put(fact, xobj);
					//	otnObj.setObserver(classObserver);
					} else {
						throw new DoesNotExistException("FactHandle does not exist for class. Did you forget to create facthandle for class");
					}
				}
			
		} catch (DoesNotExistException e) {
			e.printStackTrace();
			otnNode = new ObjectTypeNode(fact);
			factsMap.put(fact, otnNode);
			otnNode.setException(e);
		}
				
		return otnNode;	
	}

	public static Collection getAllFactNodes() {
		return factsMap.values();		
	}

}
