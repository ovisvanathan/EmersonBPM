package com.emerson.bpm.action;

import com.emerson.bpm.api.SDKNode;

public class ElaborationAction<T> extends RulesAction<T> {
	
	SDKNode sourceNode;
		
	ELABSTRATEGY strategy;
	
	RulesAction rulesAction;
	
	int numSelections;
	
	public ElaborationAction(SDKNode elabNode1, String fieldName, ELABSTRATEGY stgy) {
		this.sourceNode = elabNode1;
		this.strategy = stgy;
	}


	public ElaborationAction(SDKNode elabNode1, String fieldName, ELABSTRATEGY stgy, RulesAction rulesAction) {
		this.sourceNode = elabNode1;
		this.strategy = stgy;
		
		this.rulesAction = rulesAction;
	}


	public ElaborationAction(SDKNode elabNode2, Class<T> klazz, ELABSTRATEGY stgy, int numSelections,
				RulesAction ract1) {
		this.sourceNode = elabNode2;
		this.klazz = klazz;
		
		this.strategy = stgy;

		this.rulesAction = ract1;		
		this.numSelections = numSelections;
	}
	
	

}
