package com.emerson.bpm.api;

import java.util.List;

import com.emerson.bpm.util.Utils;

public class RuleQuery {

	String queryName;
	Class [] entities;
	
	int queryHash;
	
	public int getQueryHash() {
		return queryHash;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public Class [] getEntities() {
		return entities;
	}

	public void setEntities(Class [] entities) {
		this.entities = entities;
	}

	public RuleQuery(String queryName, Class [] entities2) {
		this.queryName = queryName;
		this.entities = entities2;
	
		computeQueryHash();
	}

	private void computeQueryHash() {

		queryHash = Utils.hash(this.queryName);

			for(Class kz : this.entities)
				queryHash = Utils.hashmerge(queryHash, kz);
	
	}
	
	
}
