package com.emerson.bpm.action;

import java.util.List;

public class OperatorSelection extends OperatorConsequence {

	List<Proposal> proposals;
	
	Proposal selected;
	
	int selectedIndex;

	public List<Proposal> getProposals() {
		return proposals;
	}

	public void setProposals(List<Proposal> proposals) {
		this.proposals = proposals;
	}

	public Proposal getSelected() {
		return selected;
	}

	public void setSelected(Proposal selected) {
		this.selected = selected;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	
	
	
}
