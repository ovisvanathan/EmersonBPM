package com.emerson.bpm.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSException;
import org.xml.sax.InputSource;

import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.model.Address;
import com.emerson.bpm.model.Person;
import com.emerson.bpm.nodes.match.TYPEID;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.evatic.paddle.model.Bank;
import com.evatic.paddle.model.Employee;

public class XMLUtils {

	static UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	 private static <T> void jaxbObjectToXML(Object eObj) {
		 
 	  	try {
				Class eclass = eObj.getClass();
				String ename = getSimpleName(eObj.getClass());
			 
				Person person = new Person("Lokesh Gupta", 
						new Address("100 wooods ave", "", "frisco", "NS", "8779i"));

			Map annotsMap = new HashMap();
	          annotsMap.put("value", XmlAccessType.FIELD);
	          
	          RuntimeAnnotations.putAnnotation(eclass, XmlAccessorType.class, annotsMap);
	          
	          Class dclass = Address.class;
	          
	          RuntimeAnnotations.putAnnotation(dclass, XmlAccessorType.class, annotsMap);

	          JAXBContext jaxbContext = JAXBContext.newInstance(eObj.getClass());
			
	          Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		     // To format XML
		     jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
		
		     JAXBElement<T> jaxbElement = 
					        new JAXBElement<T>( new QName("", ename), 
					        			eclass, 
					                  (T) person);

          
     			jaxbMarshaller.marshal(jaxbElement, System.out);

 	  	} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }

	
	 private static <T> void jaxbObjectToXML2(Object eObj) 
	  {
	      try {
	    	  
	    	  	Class eclass = eObj.getClass();
	    	  	String ename = getSimpleName(eObj.getClass());
	    	
	    	  	Map emap = ELUtils.convertPojoToMap(eObj);
	    	  	
	    	  	
	    	  	Person person = new Person("Lokesh Gupta", 
	    	  			new Address("100 wooods ave", "", "frisco", "NS", "8779i"));
	    	  	
	    	    Map annotsMap = new HashMap();
		          annotsMap.put("value", XmlAccessType.FIELD);
		          
		          RuntimeAnnotations.putAnnotation(eclass, XmlAccessorType.class, annotsMap);

		          
		          System.out.println("eca = " + Arrays.asList(eclass.getAnnotations()));
		          

				JAXBContext jaxbContext = JAXBContext.newInstance(eObj.getClass());
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	          // To format XML
	          jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
	 
	                    //If we DO NOT have JAXB annotated class
	          JAXBElement<T> jaxbElement = 
	            new JAXBElement<T>( new QName("", ename), 
	            			eclass, 
	                      (T) person);

	          

	          /*
	        Document doc = null;
	        try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = dbf.newDocumentBuilder();
				doc = builder.newDocument();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  			          
	          jaxbMarshaller.marshal(jaxbElement, doc);
	          */
	    
	          /*
	          System.out.println("emap = " + emap);
		      
	          Set set = emap.keySet();
	          Iterator iter = set.iterator();
	          while(iter.hasNext()) {
	        	  String name = (String) iter.next();
	        	  Object val = emap.get(name);
	          
	         
	        	  JAXBElement<T> jaxbElement2 = 
	      	            new JAXBElement<T>( new QName("", name), 
	      	            			eclass, 
	      	                      (T) eObj);
	      	         
		          jaxbMarshaller.marshal(jaxbElement, doc);
	          
	        	  
	          }
	          */
	          
	          jaxbMarshaller.marshal(jaxbElement, System.out);

	          //If we have JAXB annotated class
	          //jaxbMarshaller.marshal(employeeObj, System.out);  
	           
	      } catch (JAXBException e) {
	          e.printStackTrace();
	      }
	  }

	private static String getSimpleName(Class<? extends Object> class1) {

		String className = class1.getName();
		
		int dpos = className.lastIndexOf(".");

		if(dpos != -1)
			return className.substring(dpos+1);
		
		return className;
	}
	
	public static void main(String [] args) {
		
		
		String xml = generateXMLFromPojos(new Object [] {
				new Person("soka moga"),
				new Address("55 rockshore blvd", "", "miami", "FL", "274874"),
				new Bank("MDCB"),
				new Employee("bret", "miles", "vp", "(888) 800 8000", "bretmiles@mucilcorp.com")
		});
		
	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            Document mdoc = builder.newDocument();

            Element elem = mdoc.createElementNS(
            		"http://purl.org/monarch/", "edition");
             
            
            elem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:mg", "http://www.mgnr.org/");
            elem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:msxsl", "urn:schemas-microsoft-com:xslt");
          
            elem.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:tassel", "http://www.semanticspec.org/tassel");

            elem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", 
            	    "xs:schemaLocation", "http://www.semanticspec.org/tassel/TasselSchema.xsd");
            
            mdoc.appendChild(elem);
     	    
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
       
            mergeXml(mdoc, doc);
        
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        	
	}


	/*
	 * Bult registers classes to load via JAXB
	 */
	public static void registerLoadClasses(Class[] classes) {

		List<Class> entClasses = new ArrayList();
		for(Class c2 : classes) {
			
			Field [] fields = c2.getDeclaredFields();
			
			for(Field f : fields) {
				if(!TYPEID.isPrimitiveType(f.getType())) 
						entClasses.add(f.getType());
			}
								
		}
		
		entClasses.addAll(Arrays.asList(classes));
		
		for(Class cx : entClasses)
			XMLUtils.addRuntimeAnnotation(cx);

		
	//	Object [] dataObjects = XMLUtils.populateDefaultValues(entClasses);
		
	//	for(Object kobj : dataObjects)
	//		XMLUtils.generateJAXB(kobj);
		
		
	}


	/*
	private static Object[] populateDefaultValues(List<Class> entClasses) {

		EasyRandom easyRandom = new EasyRandom();

		int k=0;
		Object [] results = new Object[entClasses.size()];
		for(Class ck : entClasses) { 
			Object o2 = easyRandom.nextObject(ck);
			results[k++] = 02;
		}
		
		return results;
	}
	*/


	private static <T> void generateJAXB(Object eObj) {

	  	Class eclass = eObj.getClass();
	  	String ename = getSimpleName(eObj.getClass());

        try {
			JAXBContext jaxbContext = JAXBContext.newInstance(eObj.getClass());
			
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			 // To format XML
			 jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 

			 JAXBElement<T> jaxbElement = 
					        new JAXBElement<T>( new QName("", ename), 
					        			eclass, 
					                  (T) eObj);

   
				jaxbMarshaller.marshal(jaxbElement, System.out);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private static void addRuntimeAnnotation(Class cx) {
		
		  Map annotsMap = new HashMap();
          annotsMap.put("value", XmlAccessType.FIELD);
          
          RuntimeAnnotations.putAnnotation(cx, XmlAccessorType.class, annotsMap);	
	}

	
	public static String generateXMLFromPojos(Object [] pojos) {

	       VelocityEngine ve = new VelocityEngine();
	       Properties p = new Properties();
	       p.setProperty("resource.loader", "class");
	       p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

	 //      ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	       ve.init(p);

		   VelocityContext context = new VelocityContext();
	       
		   Map pojoMap = EmersonUtils.convertPojosToMap(pojos);

		   context.put("userDataMap", pojoMap);

	        /*
	         *   get the Template  
	         */

	        Template t = ve.getTemplate( "/templates/sgmodel.vm" );

	        /*
	         *  now render the template into a Writer, here 
	         *  a StringWriter 
	         */

	        StringWriter writer = new StringWriter();

	        t.merge( context, writer );

	        /*
	         *  use the output in the body of your emails
	         */

	    //    System.out.println( writer.toString() );
	        
	        return  writer.toString();
		
	}
	
	public static Document convertStringToXMLNode(String xmlString) {
		  //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
	}

	/*
	public org.w3c.dom.Document concatXmlDocuments(InputStream... xmlInputStreams) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    org.w3c.dom.Document result = builder.newDocument();
	    org.w3c.dom.Element rootElement = result.createElement("root");
	    result.appendChild(rootElement);
	    for(InputStream is : xmlInputStreams) {
	        org.w3c.dom.Document document = builder.parse(is);
	        org.w3c.dom.Element root = document.getDocumentElement();
	        NodeList childNodes = root.getChildNodes();
	        for(int i = 0; i < childNodes.getLength(); i++) {
	            Node importNode = result.importNode(childNodes.item(i), true);
	            rootElement.appendChild(importNode);
	        }
	    }
	    return result;
	}
	*/
	
	public static Document mergeXml(Document doc, Document docToMerge) {
	

		Element docRoot = doc.getDocumentElement();
	
//		System.out.println(xml);
		 
	//	Document modelDoc = convertStringToXMLNode(xml);

		Element modelRoot = docToMerge.getDocumentElement();

		 // Copy Child Nodes
	    NodeList childNodes2 = modelRoot.getChildNodes();
	    for (int x = 0; x < childNodes2.getLength(); x++) {
	      Node importedNode = doc.importNode(childNodes2.item(x), true);
	      docRoot.appendChild(importedNode);
	    }
		
	    printDOM(doc);
	    
	    return doc;
	}


	private static void printDOM(Document doc) {
		  // Get a factory (DOMImplementationLS) for creating a Load and Save object.
        try {
			org.w3c.dom.ls.DOMImplementationLS impl = 
			    (org.w3c.dom.ls.DOMImplementationLS) 
			    org.w3c.dom.bootstrap.DOMImplementationRegistry.newInstance().getDOMImplementation("LS");

			// Use the factory to create an object (LSSerializer) used to 
			// write out or save the document.
			org.w3c.dom.ls.LSSerializer writer = impl.createLSSerializer();
			org.w3c.dom.DOMConfiguration config = writer.getDomConfig();
			config.setParameter("format-pretty-print", Boolean.TRUE);
			
			// Use the LSSerializer to write out or serialize the document to a String.
			String serializedXML = writer.writeToString(doc);
		
			System.out.println(serializedXML);
			
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
        
	}
	
	
	
	
}
