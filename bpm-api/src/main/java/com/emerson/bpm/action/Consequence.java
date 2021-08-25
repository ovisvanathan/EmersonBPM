package com.emerson.bpm.action;

public interface Consequence {

	void setAction(RulesAction action);

	void execute();

}
