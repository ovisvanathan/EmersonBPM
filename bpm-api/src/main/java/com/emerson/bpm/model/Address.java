package com.emerson.bpm.model;

import javax.xml.bind.annotation.XmlElement;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author salaboy
 */
public class Address {
    private String addressLine1;

    @XmlElement
    String street1;

    @XmlElement
    String street2;

    
    @XmlElement
    String city;

    @XmlElement
    String state;

    @XmlElement
    String zip;
    
	public Address() {

	}

    public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public Address(String city) {
    	this.city = city;
    }

    
    public Address(String str1, String str2, String city, String state, String zip) {
    	this.street1 = str1;
    	this.street2 = str2;
    	
    	this.city = city;
    	this.state = state;
    	this.zip = zip;	

    	this.addressLine1 = this.street1;
    }


	public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @Override
    public String toString() {
        return "Address{" + "addressLine1=" + addressLine1 + '}';
    }
    
    
    
}