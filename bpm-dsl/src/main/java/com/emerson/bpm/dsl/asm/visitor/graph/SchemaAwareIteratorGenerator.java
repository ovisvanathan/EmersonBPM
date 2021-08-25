package com.emerson.bpm.dsl.asm.visitor.graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.horus.velograph.model.Graph;
import com.horus.velograph.model.Vertex;

public class SchemaAwareIteratorGenerator<V> implements Graph.Visitor<V>, IteratorGenerator {

	@Override
	public void visit(V vertex) {
		
	}
	
	List<Iterable> iterables = new LinkedList();
	

	@Override
	public void visit(V vertex, V parent) {

		Vertex vx = (Vertex) vertex;

		Vertex px = (Vertex) parent;
		
		String fieldLabel = vx.getVertLabel();

		String vertdata = vx.getVertexData();
		
		JSONObject jobj = new JSONObject(vertdata);
				
		String isList = jobj.getString("isList");

		String gentype = jobj.getString("GenericType");

		String iamAList = jobj.getString("isList");

		String parentType = px.getVertexData();	
		
		if(Boolean.parseBoolean(iamAList) == true)
				addIterable(
					ASMUtils.iterableFromSource(parentType, fieldLabel, gentype, true)
					);
			else
				addIterable(
						ASMUtils.iterableFromSource(parentType, fieldLabel, gentype, false)
						);
	
		
	}

	@Override
	public List getVisited() {

		
		return null;
	}

	@Override
	public void addIterable(Iterable iterableFromSource) {
		iterables.add(iterableFromSource);		
	}

	@Override
	public Iterator getIterator() {		
		
		return iterables.iterator();
	}

}
