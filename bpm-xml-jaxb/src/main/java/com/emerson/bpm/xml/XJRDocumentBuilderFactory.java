package com.emerson.bpm.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XJRDocumentBuilderFactory extends DocumentBuilderFactory {

	@Override
	public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		return new XJRDocumentBuilder();
	}

	@Override
	public void setAttribute(String name, Object value) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getAttribute(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFeature(String name, boolean value) throws ParserConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFeature(String name) throws ParserConfigurationException {
		// TODO Auto-generated method stub
		return false;
	}

}
