package com.emerson.bpm.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.LSParser;

import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.schema.ElemState.ELEM_STATES;
import com.emerson.bpm.schema.util.SchemaUtils;
import com.emerson.bpm.util.ServiceFactory;


public class SchemaTreeWalk implements DOMErrorHandler { // , ContentHelper {

	/** Default namespaces support (true). */
	protected static final boolean DEFAULT_NAMESPACES = true;

	/** Default validation support (false). */
	protected static final boolean DEFAULT_VALIDATION = false;

	/** Default Schema validation support (false). */
	protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

	static LSParser builder;
	XSModel model = null;
	XSNamedMap elemMap = null;

//	static String xsdFile = "RulesDSLSchema.xsd";
	static String xsdFile = "D:\\java2\\MonarchJBPM\\target\\classes\\monarch-schema2.xsd";

	// ArrayList allowedElems = new ArrayList();
	// ArrayList allowedAttrs = new ArrayList();
	XSNamedMap attrMap = null;
	ArrayList attrList = new ArrayList();
	ArrayList elemDetails = new ArrayList();
	String schemaFile = null;
	static HashMap elemHash = new HashMap();
	// String elementName = null;

	Map<String, String> elementNamesMap = new HashMap<String, String>();

	Map<String, List> elemListMap = new HashMap<String, List>();

	ElementStack<ElemState> elemStack = new ElementStack<>();
	
	ELEM_STATES ElemStates;

	protected UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();


	public SchemaTreeWalk(String schemaFile) {

		System.out.println(" in qxs ctor " + schemaFile);

		this.schemaFile = schemaFile;

		try {
			// get DOM Implementation using DOM Registry
			System.setProperty(DOMImplementationRegistry.PROPERTY,
					"com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl");
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

			XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");

			XSLoader schemaLoader = impl.createXSLoader(null);

			DOMConfiguration config = schemaLoader.getConfig();

			// create Error Handler
			DOMErrorHandler errorHandler = (DOMErrorHandler) this;

			// set error handler
			config.setParameter("error-handler", errorHandler);

			// set validation feature
			config.setParameter("validate", Boolean.TRUE);

			// parse document
			System.out.println("Parsing " + schemaFile + "...");
			model = schemaLoader.loadURI(schemaFile);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println(" qxs model " + model);

		// element declarations
		elemMap = model.getComponents(XSConstants.ELEMENT_DECLARATION);

		if (elemMap.getLength() != 0) {
			System.out.println("*************************************************");
			System.out.println("* Global element declarations: {namespace} name ");
			System.out.println("*************************************************");
			for (int i = 0; i < elemMap.getLength(); i++) {
				XSObject item = elemMap.item(i);
				System.out.println("{" + item.getNamespace() + "}" + item.getName());
			}
		}

		// attribute declarations
		attrMap = model.getComponents(XSConstants.ATTRIBUTE_DECLARATION);
		System.out.println(" attrmap len " + attrMap.getLength());

		if (attrMap.getLength() != 0) {
			System.out.println("*************************************************");
			System.out.println("* Global attribute declarations: {namespace} name");
			System.out.println("*************************************************");
			for (int i = 0; i < attrMap.getLength(); i++) {
				XSObject item = attrMap.item(i);
				System.out.println("{" + item.getNamespace() + "}" + item.getName());
			}
		}

	}

	public XSModel getModel() {
		return model;
	}

	public static void main(String[] argv) {

		/*
		 * if (argv.length == 0) { printUsage(); System.exit(1); }
		 */

		try {
			// get DOM Implementation using DOM Registry
			System.setProperty(DOMImplementationRegistry.PROPERTY,
					"com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl");
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

			XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");

			XSLoader schemaLoader = impl.createXSLoader(null);

			DOMConfiguration config = schemaLoader.getConfig();

			// create Error Handler
			DOMErrorHandler errorHandler = new SchemaTreeWalk(xsdFile);

			// set error handler
			config.setParameter("error-handler", errorHandler);

			// set validation feature
			config.setParameter("validate", Boolean.TRUE);

			// parse document
			// System.out.println("Parsing " + argv[0] + "...");

			XSModel model = schemaLoader.loadURI(xsdFile);
			if (model != null) {
				// element declarations
				XSNamedMap map = model.getComponents(XSConstants.ELEMENT_DECLARATION);
				if (map.getLength() != 0) {
					System.out.println("*************************************************");
					System.out.println("* Global element declarations: {namespace} name ");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}
				// attribute declarations
				map = model.getComponents(XSConstants.ATTRIBUTE_DECLARATION);
				if (map.getLength() != 0) {
					System.out.println("*************************************************");
					System.out.println("* Global attribute declarations: {namespace} name");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}
				// notation declarations
				map = model.getComponents(XSConstants.TYPE_DEFINITION);
				if (map.getLength() != 0) {
					System.out.println("*************************************************");
					System.out.println("* Global type declarations: {namespace} name");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}

				// notation declarations
				map = model.getComponents(XSConstants.NOTATION_DECLARATION);
				if (map.getLength() != 0) {
					System.out.println("*************************************************");
					System.out.println("* Global notation declarations: {namespace} name");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Stack findElement(String nsuri, String elemName, String qName) {

		System.out.println("  elem name = " + elemName);

		ArrayList allowedElems = new ArrayList();
		ArrayList attrList = new ArrayList();
		elemDetails = new ArrayList();

		XSObject item = elemMap.itemByName(nsuri, elemName);

		// System.out.println(" elem obj = " + item);

		if (item != null && item instanceof XSElementDeclaration) {

			XSElementDeclaration xdecl = (XSElementDeclaration) item;

			XSTypeDefinition xdef = xdecl.getTypeDefinition();

			if (xdef instanceof XSComplexTypeDefinition) {

				// System.out.println(" elem is cplx ");
				XSComplexTypeDefinition xcpdef = (XSComplexTypeDefinition) xdef;

				if (xcpdef != null) {

					attrList = processComplexAttributes(xdecl, xcpdef, elemName);

					System.out.println("  root attr = " + attrList);

					XSObjectList xobjs = xcpdef.getAttributeUses();

					// System.out.println(" xobj attr = " + xobjs);

					if (xobjs != null) {

						for (int s = 0; s < xobjs.getLength(); s++) {

							XSObject xobj = xobjs.item(s);
							if (xobj != null) {
								// System.out.println(" xobj type = " + xobj.getClass().getName());

								XSAttributeUse attrImpl = (XSAttributeUse) xobj;
								XSAttributeDeclaration attrDecl = attrImpl.getAttrDeclaration();

								String attrName = attrDecl.getName();
								short type = attrDecl.getType();
								String ns = attrDecl.getNamespace();
								String attrVal = attrDecl.getConstraintValue();
								String consType = new Integer(attrDecl.getConstraintType()).toString();

								// System.out.println(" attr name = " + attrName);
								// System.out.println(" attr type = " + type);
								// System.out.println(" attr ns = " + ns);
								// System.out.println(" attr val = " + attrVal);
								// System.out.println(" attr constype = " + consType);

								AttrInfo attrInfo = new AttrInfo();

								attrInfo.setAttrName(attrName);
								attrInfo.setAttrNS(ns);
								attrInfo.setConsType(type);
								attrInfo.setConsVal(attrVal);

								attrList.add(attrInfo);

							} // end if

						} // end for

					} // end xobjs

					XSParticle pcle = xcpdef.getParticle();

					XSTerm xterm = pcle.getTerm();

					// System.out.println(" term type " + xterm.getType());

					verifyType(xterm, elemName);
				}

			}

		}

	//	elemDetails.add(attrList);
	//	elemDetails.add(allowedElems);
		
		
		//prepare elemStack for return
		Stack elementStack = elemStack.getStack();
		
		Stack elemStack2 = (Stack) elementStack.clone();
		
		System.out.println("stack ::" + elementStack.size());

		ElemState prev = null;

		Map<ElemState, Map> prevMap = null;
		Map<ElemState, Map> elemTreeMap = null;
		while(!elementStack.isEmpty()) {			
			ElemState esn = (ElemState) elementStack.pop();			
			
		//	System.out.println(" esnext ::" + esn.elemName);

			elemTreeMap = new HashMap();
			
			if(prevMap != null) {
		//		System.out.println(" esprev ::" + prev.elemName);
				elemTreeMap.put(esn, prevMap);
			} else
				elemTreeMap.put(esn, null);

		//	System.out.println("celems  ::");

		//	if(esn.elems != null) 
		//		for(String s : esn.elems)
		//			System.out.print("::" + s + "::, ");

			
			prev = esn;
			prevMap = elemTreeMap;			
		}			

		
		System.out.println("ETM ::" + elemTreeMap);
		
		
		SchemaUtils.debugTreeMap(elemTreeMap);
		
		
		// return elemDetails;
		return elemStack2;

	}

	public ArrayList findChildren(XSElementDeclaration xdecl, String nsuri, String elemName, String qName) {

		System.out.println(" Entering findChildren " + elemName);

		ArrayList allowedElems = null;
		ArrayList attrList = null;
		elemDetails = new ArrayList();

		// elementName = elemName;

		XSTypeDefinition xdef = xdecl.getTypeDefinition();

		if (xdef instanceof XSComplexTypeDefinition) {

			// System.out.println(" elem is cplx ");
			XSComplexTypeDefinition xcpdef = (XSComplexTypeDefinition) xdef;

			if (xcpdef != null) {

				// attrList = processComplexAttributes(xdecl, xcpdef, qName);

				short ctype = xcpdef.getContentType();

				if (ctype == XSComplexTypeDefinition.CONTENTTYPE_ELEMENT
						|| ctype == XSComplexTypeDefinition.CONTENTTYPE_MIXED) {

					XSParticle pcle = xcpdef.getParticle();

					XSTerm xterm = pcle.getTerm();

					// System.out.println(" term type " + xterm.getType());

					// allowedElems =
					verifyType(xterm, elemName);

				} // end ctype check

			} // if xcpdef != null

		}

		// elemDetails.add(attrList);
		// elemDetails.add(allowedElems);

		// System.out.println(" FC ename = " + elemName);
		// System.out.println(" FC name = " + xdecl.getName());
		// System.out.println(" FC nsp = " + xdecl.getNamespace());
		// System.out.println(" FC qname = " + qName);
		// System.out.println(" FC allowedElems = " + allowedElems);
		// System.out.println(" FC attrList = " + attrList);

		/*
		 * if(allowedElems != null && allowedElems.size() > 0) elemHash.put(elemName +
		 * "_ELEM", allowedElems);
		 * 
		 * if(attrList != null && attrList.size() > 0) elemHash.put(elemName + "_ATTR",
		 * attrList);
		 */
		// System.out.println(" FC elemHash = " + elemHash);

		return elemDetails;

	}

	public ArrayList processComplexAttributes(XSElementDeclaration xdecl, XSComplexTypeDefinition xdef, String qName) {

		ArrayList attrList = new ArrayList();

		XSObjectList xobjs = xdef.getAttributeUses();

		// System.out.println(" xobj attr = " + xobjs);

		if (xobjs != null) {
			for (int s = 0; s < xobjs.getLength(); s++) {
				XSObject xobj = xobjs.item(s);
				if (xobj != null) {
					// System.out.println(" xobj type = " + xobj.getClass().getName());

					XSAttributeUse attrImpl = (XSAttributeUse) xobj;
					XSAttributeDeclaration attrDecl = attrImpl.getAttrDeclaration();

					String attrName = attrDecl.getName();
					short type = attrDecl.getType();
					String ns = attrDecl.getNamespace();
					String attrVal = attrDecl.getConstraintValue();
					String consType = new Integer(attrDecl.getConstraintType()).toString();

					// System.out.println(" attr name = " + attrName);
					// System.out.println(" attr type = " + type);
					// System.out.println(" attr ns = " + ns);
					// System.out.println(" attr val = " + attrVal);
					// System.out.println(" attr constype = " + consType);

					AttrInfo attrInfo = new AttrInfo();

					attrInfo.setAttrName(attrName);
					attrInfo.setAttrNS(ns);
					attrInfo.setConsType(type);
					attrInfo.setConsVal(attrVal);

					attrList.add(attrInfo);

				} // end if
			} // end for
				// if(attrList != null && attrList.size() > 0)
				// elemHash.put(qName + "ATTR", attrList);

		} // end xobjs

		return attrList;

	}

	/*
	 * private void checkType(XSObject xterm) {
	 * 
	 * switch(xterm.getType()) {
	 * 
	 * case XSConstants.ELEMENT_DECLARATION: System.out.println("  is elme name " +
	 * xterm.getName());
	 * 
	 * allowedElems.add(xterm.getName());
	 * 
	 * break;
	 * 
	 * case XSConstants.MODEL_GROUP: System.out.println("  is mg "); XSModelGroup
	 * xmg = (XSModelGroup) xterm;
	 * 
	 * XSObjectList xobjList = xmg.getParticles();
	 * System.out.println(" ================= mg list size ================== " +
	 * xobjList.getLength());
	 * 
	 * for(int r=0;r<xobjList.getLength();r++) { XSObject xobj = xobjList.item(r);
	 * checkType(xobj); }
	 * 
	 * break;
	 * 
	 * case XSConstants.WILDCARD: System.out.println("  is wcard "); break;
	 * 
	 * case XSConstants.PARTICLE: System.out.println("  is ptcl "); XSParticle pcle
	 * = (XSParticle) xterm;
	 * 
	 * XSTerm pterm = pcle.getTerm(); System.out.println("  pterm type " +
	 * pterm.getType()); checkType(pterm); break;
	 * 
	 * 
	 * case XSConstants.ATTRIBUTE_DECLARATION:
	 * System.out.println("  $$$$$$$$$$$$$$$$is attr $$$$$$$$$$$$$$$$$"); break;
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 */

	private ArrayList verifyType(XSObject xterm, String elemName) {
		/*
		 * 
		 * System.out.println("  in VT name = " + xterm.getName());
		 * System.out.println("  in VT type = " + xterm.getType());
		 * 
		 * System.out.println("  in VT elem  = " + XSConstants.ELEMENT_DECLARATION);
		 * System.out.println("  in VT mg = " + XSConstants.MODEL_GROUP);
		 * System.out.println("  in VT WC = " + XSConstants.WILDCARD);
		 * System.out.println("  in VT attr = " + XSConstants.ATTRIBUTE_DECLARATION);
		 * System.out.println("  in VT not = " + XSConstants.NOTATION_DECLARATION );
		 * System.out.println("  in VT ptcl = " + XSConstants.PARTICLE );
		 * System.out.println("  in VT qname = " + XSConstants.QNAME_DT );
		 * 
		 */

		System.out.println("  Entering verifyType = " + xterm.getName() + ", " + elemName);

		ArrayList allowedElems = new ArrayList();
		ArrayList allowedElemsAttrs = new ArrayList();

		// System.out.println(" elem name vt " + elemName);

		switch (xterm.getType()) {

		case XSConstants.ELEMENT_DECLARATION:
			System.out.println("  is elmeDecl ");

			XSElementDeclaration elemDecl = (XSElementDeclaration) xterm;
			if (elemDecl.getTypeDefinition() instanceof XSComplexTypeDefinition) {

				String qName = elemDecl.getNamespace() + ":" + elemDecl.getName();

				XSComplexTypeDefinition xcpdef = (XSComplexTypeDefinition) elemDecl.getTypeDefinition();

				attrList = processComplexAttributes(elemDecl, xcpdef, qName);

				ElemState top = elemStack.peek();
				if(attrList != null) {
					
					if(top.attrs == null) {
						 System.out.println(" top attrs matches " + qName);
						top.attrs = attrList;
					} else {
						top.attrs = attrList;
					}				
				}	
				
		//		elemListMap.put(xterm.getName() + "ATTR", attrList);

				String key1 = xcpdef.getName();
				String key2 = elemName;

				if (elementNamesMap.containsKey(key1) || elementNamesMap.containsKey(key2)) {
					System.out.println("  Name present already.  ");
					return null;
				}

				System.out.println(" adding key   " + key1);
				elementNamesMap.put(key1, elemName);

				String nsuri = elemDecl.getNamespace();
				String elementName = elemDecl.getName();
				qName = elemDecl.getNamespace() + ":" + elemDecl.getName();

				findChildren((XSElementDeclaration) xterm, nsuri, elementName, qName);

			} else {
				XSElementDeclaration elementDecl = (XSElementDeclaration) xterm;
				String elementName = elementDecl.getName();
				allowedElems.add(elementName);
			}

			break;

		case XSConstants.MODEL_GROUP:
			System.out.println("  is mg ");
			XSModelGroup xmg = (XSModelGroup) xterm;

			XSObjectList xobjList = xmg.getParticles();
			ArrayList childElems = new ArrayList();

			ArrayList childList = null;

			System.out.println(" ================ mg size =============== " + xobjList.getLength());

			
			ElemState estate = new ElemState();
			estate.elemName = elemName;		
			elemStack.push(estate);

		//	for (int r = 0; r < xobjList.getLength(); r++) {
		//		allowedElems.add(xobjList.get(r).toString());
		//	}

		//	elemListMap.put(elemName, allowedElems);

			for (int r = 0; r < xobjList.getLength(); r++) {
				XSObject xobj = xobjList.item(r);

				try {
					childList = Optional.ofNullable(verifyType(xobj, elemName))
											.orElseThrow(IllegalArgumentException::new);
				} catch (IllegalArgumentException e) {

				//	ElemState top = elemStack.peek();
				//	System.out.println(" caught illegal arg for  " + xobj.getName());					
				//	if(top.elemName.equals(elemName) && top.elems != null)
				//		top.elems.add(xobj.getName());
					
				//	e.printStackTrace();
				}
											
			}
				
				elemStack.pop();
				
				// childElems.addAll(childList);
				// elemHash.put(elemName + "_ELEM", childElems);
				// elemHash.put(elemName + "_ATTR", attrList);

				// if (allowedElems.size() == 0)
				// allowedElems.add(elemName);
				// else if (!((String) allowedElems.get(0)).equals(elemName))
				// allowedElems.add(elemName);

				/*
				 * ArrayList childAttrList = (ArrayList) childList.get(0); ArrayList
				 * childAllowedElems = (ArrayList) childList.get(1);
				 * 
				 * System.out.println("  VT allowedElems = " + childAllowedElems);
				 * System.out.println("  VT  attrList = " + attrList);
				 * 
				 * if(childAllowedElems != null && childAllowedElems.size() > 0) {
				 * if(elemHash.containsKey(elemName + "_ELEM")) { ArrayList elemList =
				 * (ArrayList) elemHash.get(elemName + "_ELEM");
				 * elemList.addAll(childAllowedElems); elemHash.put(elemName + "_ELEM",
				 * elemList); } else { elemHash.put(elemName + "_ELEM", childAllowedElems); } }
				 * if(attrList != null && attrList.size() > 0) elemHash.put(elemName + "_ATTR",
				 * childAttrList);
				 */
				// System.out.println(" VT elemHash = " + elemHash);
			
			
			break;

		case XSConstants.WILDCARD:
			System.out.println("  is wcard ");
			break;

		case XSConstants.PARTICLE:
			System.out.println("  is ptcl ");
			XSParticle pcle = (XSParticle) xterm;

			XSTerm pterm = pcle.getTerm();
			
			System.out.println(" pterm name " + pterm.getName());

			ElemState top = elemStack.peek();

			System.out.println(" top name = " + top.elemName);

			if(top.elems == null) {
				 System.out.println(" top matches " + pterm.getName());
				top.elems = new ArrayList();
				top.elems.add(pterm.getName());
			
			} else {
				top.elems.add(pterm.getName());
			}
			
			verifyType(pterm, elemName);

			/*
			 * System.out.println("  VT allowedElems = " + childAllowedElems);
			 * System.out.println("  VT  attrList = " + attrList);
			 * 
			 * if(childAllowedElems != null && childAllowedElems.size() > 0)
			 * elemHash.put(elemName + "_ELEM", childAllowedElems);
			 * 
			 * if(attrList != null && attrList.size() > 0) elemHash.put(elemName + "_ATTR",
			 * attrList);
			 * 
			 * System.out.println(" VT  elemHash = " + elemHash);
			 */

			break;

		case XSConstants.ATTRIBUTE_DECLARATION:
			// System.out.println(" $$$$$$$$$$$$$$$$is attr $$$$$$$$$$$$$$$$$");
			break;

		}

		return allowedElemsAttrs;

	}

	private static void printUsage() {

		System.err.println("usage: java dom.QueryXS uri ...");
		System.err.println();

	} // printUsage()

	public boolean handleError(DOMError error) {
		short severity = error.getSeverity();
		if (severity == DOMError.SEVERITY_ERROR) {
			System.out.println("[xs-error]: " + error.getMessage());
		}

		if (severity == DOMError.SEVERITY_WARNING) {
			System.out.println("[xs-warning]: " + error.getMessage());
		}
		return true;

	}

	// public void buildMetaData(String qNamerootElem, String rootElemNS, String
	// rootElemName) {
	public void buildMetaData() {

		try {
			// get DOM Implementation using DOM Registry
			System.setProperty(DOMImplementationRegistry.PROPERTY,
					"com.sun.org.apache.xerces.internal.dom.DOMXSImplementationSourceImpl");
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

			XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");

			XSLoader schemaLoader = impl.createXSLoader(null);

			DOMConfiguration config = schemaLoader.getConfig();

			// create Error Handler
			DOMErrorHandler errorHandler = (DOMErrorHandler) this;

			// set error handler
			config.setParameter("error-handler", errorHandler);

			// set validation feature
			config.setParameter("validate", Boolean.TRUE);

			// parse document
			System.out.println("Parsing " + schemaFile + "...");
			model = schemaLoader.loadURI(schemaFile);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println(" qxs model " + model);

		// XSElementDeclaration rootDecl = model.getElementDeclaration(qNamerootElem,
		// rootElemNS);

		// element declarations
		elemMap = model.getComponents(XSConstants.ELEMENT_DECLARATION);

		if (elemMap.getLength() != 0) {
			System.out.println("*************************************************");
			System.out.println("* Global element declarations: {namespace} name ");
			System.out.println("*************************************************");
			for (int i = 0; i < elemMap.getLength(); i++) {
				XSObject item = elemMap.item(i);
				System.out.println("{" + item.getNamespace() + "}" + item.getName());

				if (item != null && item instanceof XSElementDeclaration) {

					XSElementDeclaration xdecl = (XSElementDeclaration) item;
					String nsuri = xdecl.getNamespace();
					String elemName = xdecl.getName();
					String qName = xdecl.getNamespace() + ":" + xdecl.getName();

					// elemDetails = findChildren(xdecl, nsuri, "", qName);

					findChildren(xdecl, nsuri, elemName, qName);

					// addChildrenToMap(xdecl, nsuri, qName);

				}
			}
		}

		System.out.println(" elemHash = " + elemHash);
	}

	/*
	 * private void addChildrenToMap(XSElementDeclaration xdecl, String nsuri,
	 * String qName) {
	 * 
	 * elemDetails = findChildren(xdecl, nsuri, "", qName);
	 * 
	 * System.out.println("  elems    " + elemDetails);
	 * 
	 * if(elemDetails != null && elemDetails.size() > 0) {
	 * 
	 * ArrayList allowedAttrs = (ArrayList) elemDetails.get(0); ArrayList
	 * allowedElems = (ArrayList) elemDetails.get(1);
	 * 
	 * for(int j=0;j<allowedElems.size();j++)
	 * System.out.println("  elems found =   " + allowedElems.get(j));
	 * 
	 * if(allowedElems != null && allowedElems.size() > 0) elemHash.put(qName +
	 * "ELEM", allowedElems);
	 * 
	 * if(allowedAttrs != null && allowedAttrs.size() > 0) { for(int
	 * k=0;k<allowedAttrs.size();k++) { AttrInfo ainfo = (AttrInfo)
	 * allowedAttrs.get(k); System.out.println("  attrs found name =   " +
	 * ainfo.getAttrName() + " ns = " + ainfo.getAttrNS() + " ctype = " +
	 * ainfo.getConsType() + " cval = " + ainfo.getConsVal()); } }
	 * 
	 * } }
	 */

	// @Override
	public ArrayList get(String qName, String mode) {
		if (mode.equals("Element"))
			return (ArrayList) elemHash.get(qName + "_ELEM");
		else if (mode.equals("ATTR"))
			return (ArrayList) elemHash.get(qName + "_ATTR");
		else
			return null;
	}

}
