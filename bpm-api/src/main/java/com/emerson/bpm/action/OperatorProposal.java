package com.emerson.bpm.action;

import java.util.Collection;

public class OperatorProposal<T> extends OperatorConsequence implements Proposal {

	String name;
		
	String prefOp;
	
	public String getPrefOp() {
		return prefOp;
	}

	public void setPrefOp(String prefOp) {
		this.prefOp = prefOp;
	}

	public double getNumericPref() {
		return numericPref;
	}

	public void setNumericPref(double numericPref) {
		this.numericPref = numericPref;
	}

	double numericPref;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<T> getWmeElements() {
		return wmeElements;
	}

	public void setWmeElements(Collection<T> wmeElements) {
		this.wmeElements = wmeElements;
	}

	Collection<T> wmeElements;
	
	
	public OperatorProposal() {
		
	}

	public OperatorProposal(String name) {
		this.name = name;
	}
	
	public OperatorProposal(String name, Collection<T> wmeElements) {
		this.name = name;
		this.wmeElements = wmeElements;
	}
	

	/*
	Set 1 {
		
		Vendor v1( Akzan Tie-Fighters & Jets)
		
		Qty: 10000
		
		Item: t1 ( Akzan Tie-Fighter T9107A-XB 21 Twin Jet Aircraft)
		
		Price per Unit:  120000 HAKSANS
		
		Delivery Date: March 2021
		
		Vendor Rating: Silver

		Weapons Capability: 
			31" Cannon
			Rack Mount Lasers
			Wingtip Laser
			Pinion Gun
		
	}
	

	Set 2 {
		
		Vendor v1( Mendelveen X-Wing Fighter Jets)
		
		Qty: 15000
		
		Item: t1 ( Mendelveen X-Wing 819K-54 Fighter Jet)
		
		Price per Unit:  165000 HAKSANS
		
		Delivery Date: May 2021
		
		Vendor Rating: Gold

		Weapons Capability: 
			14" Cannon
			Front Mount Lasers 2Nos
			Rear Machine Gun - 1 No
		
	}

	
	Set 3 {
		
		Vendor v1( Helena Aviation)
		
		Qty: 8 Nos
		
		Item: t1 ( Helena Millenium Falcon MFC1211-6A Intergalactic Fighter Jet)
		
		Price per Unit:  8,756, 497 HAKSANS
		
		Delivery Date: Jan 2024
		
		Vendor Rating: Gold

		Weapons Capability: 
			Unlimited
	}

	compare (price)

	compare (Vendor)
	
	compare (schedule)
	
	compare (Weapons Capa)
	
	compare (Item vs. Requirement)
	
	*/
	
	
}
