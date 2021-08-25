/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.nodes;

import java.util.List;

import com.emerson.bpm.api.RuleQuery;
import com.emerson.bpm.api.SDKNode;
import com.emerson.bpm.model.Action;
import com.emerson.bpm.nodes.react.ReactiveNode;

/**
 *
 * @author salaboy
 */
public class RuleTerminalNode extends ReactiveNode  {

    private Action action;
    RuleQuery ruleQuery;    

	public RuleQuery getRuleQuery() {
		return ruleQuery;
	}

	public void setRuleQuery(RuleQuery ruleQuery) {
		this.ruleQuery = ruleQuery;
	}

	public RuleTerminalNode(String rule) {  
        this.name = rule;
	}

	public RuleTerminalNode(RuleQuery rq) {  
        this.ruleQuery = rq;
	}

	public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


	@Override
	public void addChild(SDKNode aplNode) {
		
	}


	@Override
	public void registerObservers() {

		
	}

    /*
    public void assertLeftTuple(LeftTuple leftTuple, PropagationContext context, WorkingMemory wm) {
        //Create ActivationItem and place it into the agenda that should be contained inside the working memory
        Agenda agenda = wm.getAgenda();
        agenda.addActivation(new Activation(this.rule, action, leftTuple));
        
    }
    */

}
