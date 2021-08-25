package com.emerson.bpm.nodes.row;

import java.util.Arrays;
import java.util.List;

import com.emerson.bpm.api.RowTuple;
import com.emerson.bpm.api.SDKNode;

public class NamedTuple implements RowTuple {

	SDKNode left, right;
	NamedTupleComparator namedTupleComparator;
	
	SDKNode [] nodes;

	public NamedTuple() {
		
	}
		
	public NamedTuple(SDKNode left, SDKNode right) {
		this.left = left;
		this.right = right;
	}

	public NamedTuple(SDKNode left, SDKNode right, NamedTupleComparator namedTupleComparator) {
		this.left = left;
		this.right = right;
		this.namedTupleComparator = namedTupleComparator;
	}

	public NamedTuple(SDKNode parent) {
		this.left = parent;
	}
	
	@Override
	public List<SDKNode> getItemsAsList() {	
		return (List<SDKNode>) Arrays.asList(getItems());
	}

	@Override
	public SDKNode[] getItems() {
		if(nodes == null)
			nodes = (SDKNode[]) new SDKNode [] { left, right }; 

		return (SDKNode[]) nodes;
	}

	@Override
	public int getOrder() {
		return 2;
	}
}
