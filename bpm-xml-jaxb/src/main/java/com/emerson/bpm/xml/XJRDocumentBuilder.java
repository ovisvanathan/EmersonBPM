package com.emerson.bpm.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XJRDocumentBuilder extends DocumentBuilder {

	@Override
	public Document parse(InputSource is) throws SAXException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNamespaceAware() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidating() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEntityResolver(EntityResolver er) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setErrorHandler(ErrorHandler eh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Document newDocument() {
		return new XJRDocument();
	}

	@Override
	public DOMImplementation getDOMImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

}
