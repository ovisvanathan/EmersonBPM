package com.emerson.bpm.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class Person {
    
	public String name;
    
    public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public long getFatherId() {
		return fatherId;
	}

	public void setFatherId(long fatherId) {
		this.fatherId = fatherId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	Address address;
    
    long fatherId;
    
    long id;
    
    public Person() {
    	
    }
    
    public Person(String name) {
        this.name = name;
    }

    public Person(long id, String name, long fatherId) {
        this.id = id;
    	this.name = name;
    	this.fatherId = fatherId;  
    }

    
    public Person(String string, Address address1) {
    	this.name = string;
    	this.address = address1;
    }

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" + "name=" + name + '}';
    }
    
    
    
}