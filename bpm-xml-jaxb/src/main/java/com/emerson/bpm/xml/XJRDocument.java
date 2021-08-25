package com.emerson.bpm.xml;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import com.emerson.bpm.nodes.match.DefaultComparator;

public class XJRDocument extends DocumentImpl {

    public Element createElement(String tagName) throws DOMException {
    	return null;
    }

	public Element createModelElement(String simpleName) {

		return createElement(simpleName);
	}

	public Element createJoinElement(DefaultComparator comp) {
		// TODO Auto-generated method stub
		return null;
	}

	public Element createRTMElement(String ruleName) {
		return null;
	}
	
	
}
