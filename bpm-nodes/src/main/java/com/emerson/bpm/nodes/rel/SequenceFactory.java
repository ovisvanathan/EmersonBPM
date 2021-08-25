package com.emerson.bpm.nodes.rel;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.emerson.bpm.api.OrderedSet;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.dsl.REL;
import com.emerson.bpm.nodes.match.SequenceSet;

public class SequenceFactory {

    
	public static OrderedSet constructSequenceSet(Map data, SDKNode node) {		
		
		OrderedSet seqSet = new SequenceSet(node);

		Set keys = data.keySet();
		
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			Class dval = (Class) data.get(key);			
			seqSet.add(dval);
		}
		
		return seqSet;		
	}

	public static OrderedSet constructSequenceSet(SDKNode node, REL relation) {				
		return constructSequenceSet(node, relation, false);
	}
	
	public static OrderedSet constructSequenceSet(SDKNode node, REL relation, boolean isAssoc) {				
		
		OrderedSet seqSet = null;
		if(relation == null)		
			seqSet = new ComparatorSequenceSet(node, isAssoc);
		else			
			seqSet = new RelationSequenceSet(node, relation);
		
		return seqSet;		
	}

}
