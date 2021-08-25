package com.emerson.bpm.xml;

import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Document;

public class DOMNode extends ElementImpl {
	
	public DOMNode(Document d, String name) {
		super((CoreDocumentImpl) d, name);
	}

	
	
	
}
