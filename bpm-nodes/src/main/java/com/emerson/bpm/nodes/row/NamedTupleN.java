package com.emerson.bpm.nodes.row;

import java.util.ArrayList;
import java.util.List;

import com.emerson.bpm.api.SDKNode;

public class NamedTupleN extends NamedTuple {

	SDKNode left;
	SDKNode [] right;
	NamedTupleComparator namedTupleComparator;

	List sdkNodes;

	public NamedTupleN() {
	
	}

	public NamedTupleN(SDKNode left2, SDKNode[] rightTuples) {
		super(left2, rightTuples[0]);
		this.left = left;
		this.right = rightTuples;
	
	}

	public NamedTupleN(SDKNode left2, SDKNode[] rightTuples, NamedTupleComparator namedTupleComparator2) {
		super(left2, rightTuples[0]);
		this.left = left;
		this.right = rightTuples;
	
		this.namedTupleComparator = namedTupleComparator;
	}

	public List<SDKNode> getItemsAsList() {
		return sdkNodes;
	}

	public SDKNode[] getItems() {
		if(sdkNodes == null)
			sdkNodes = new ArrayList();
		sdkNodes.add(left);
		for(SDKNode r : right)
			sdkNodes.add(r);
		
		return (SDKNode[]) sdkNodes.toArray(new Object [] {}); 
	}
}
