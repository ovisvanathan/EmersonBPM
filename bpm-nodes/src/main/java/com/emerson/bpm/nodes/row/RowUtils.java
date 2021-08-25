package com.emerson.bpm.nodes.row;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.nodes.JoinNode;
import com.emerson.bpm.nodes.cmp.AlphaNodeComparator;
import com.emerson.bpm.nodes.cmp.JoinNodeComparator;

public class RowUtils {
	
	public static RowTuple createRowTuple(SDKNode left, SDKNode sdkNode) {

		if(sdkNode.getTuplicity() == 1) 
			return new NamedTuple(left);
		
		return null;
	}

	public static RowTuple createRowTuple(SDKNode left, SDKNode rightTuple, SDKNode sdkNode) {

			return new NamedTuple(left, rightTuple,
						new NamedTupleComparator(sdkNode.getClauseComparator(), 
								 (String) sdkNode.getClauseComparator().getFieldNames()[0],
								 (String) sdkNode.getClauseComparator().getFieldNames()[1]));
					
	}
	
	public static RowTuple createRowTuple(SDKNode left, SDKNode[] rightTuples, SDKNode sdkNode) {

		if(sdkNode.getTuplicity() == 2) {

			return new NamedTuple(left, rightTuples[0],
						new NamedTupleComparator(sdkNode.getClauseComparator(), 
								 (String) sdkNode.getClauseComparator().getFieldNames()[0],
								 (String) sdkNode.getClauseComparator().getFieldNames()[1]));
			
		}
		
		return null;
	}

	public static RowTuple createRowTupleOrderN(SDKNode left, SDKNode [] rightTuples, SDKNode joinNode) {

		if(joinNode.getTuplicity() > 2) {

			return new NamedTupleN(left, rightTuples,
						new NamedTupleComparator(joinNode.getClauseComparator(), 
								 (String) joinNode.getClauseComparator().getFieldNames()[0],
								 (String) joinNode.getClauseComparator().getFieldNames()[1]));
			
		}
		
		return null;
	}
	
	
	/* 
	 * Climb object graph and collect parant nodes
	 * bottom up scenario. can be many parents.
	 * 
	 * @Param a1 ALphaNodeComparator or JoinNodeComparator
	 */
	public static List<Class> doAggregateParents(SDKNode a1) {
		
		List<Class> listOfClasses = new ArrayList<>();
		
		if(a1 instanceof JoinNodeComparator) {
			JoinNodeComparator ja1 = (JoinNodeComparator) a1;
			
			RowTuple tuple = ja1.getRealNode().getParents();
			
			int len = tuple.getLength();
			Object [] items = tuple.getItems();
			
			for(int k=0;k<len;k++) {
				SDKNode rk = (SDKNode) items[k];				
				listOfClasses.addAll(doAggregateParents(rk));
			}
			
		} else if(a1 instanceof AlphaNodeComparator) {
			Class [] objClass = a1.getRealNode().getObjClasses();		
			listOfClasses.addAll(Arrays.asList(objClass));			
		} 
							
		return listOfClasses;
	}

	public static Class doFieldNameSearch(List<Class> classList) {
		return null;
	}

	
	
	
}
