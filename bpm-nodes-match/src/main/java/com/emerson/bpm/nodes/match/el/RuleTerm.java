package com.emerson.bpm.nodes.match.el;

import java.util.Arrays;
import java.util.Map;

import com.sun.el.parser.Node;

import nz.org.take.ComplexTerm;
import nz.org.take.Constant;
import nz.org.take.Function;
import nz.org.take.KnowledgeBase;
import nz.org.take.KnowledgeBaseVisitor;
import nz.org.take.Term;
import nz.org.take.Variable;

public class RuleTerm implements Term {

	Term term;

	//complex term	
	private Function function = null;
	private Term[] terms = null;

	// constant	
	private Object object = null;
	// the type can be overridden, by default the type of the object is used
	private Class vartype = null;
	// this is a string that is used to proxy the object
	private String ref = null;

	
	// variable
	private String name = null;
	private Class type = null;

	public RuleTerm(Term term) {
		this.term = term;

		System.out.println("term type in RT= "+ term.getClass().getName());

		if(ComplexTerm.class.isAssignableFrom(this.term.getClass())) {

			this.function = ((ComplexTerm)term).getFunction();	
		
			this.terms = ((ComplexTerm)term).getTerms();	
		} else if(Constant.class.isAssignableFrom(this.term.getClass())) {
		
			Constant ct = (Constant) term;
			
			this.object = ct.getObject();
			this.vartype = ct.getType();
			this.ref = ct.getRef();
		    
		}
		else if(Variable.class.isAssignableFrom(this.term.getClass())) {
			
			this.name = ((Variable)term).getName();
			this.type = term.getType();
		
		}
	
	}

	public RuleTerm() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addAnnotation(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAnnotations(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAnnotation(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getAnnotations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String removeAnnotation(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	//complex term
		
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
	}
	public Term[] getTerms() {
		return terms;
	}
	public void setTerms(Term[] terms) {
		this.terms = terms;
	}
	
	public Class getType() {	
		if(ComplexTerm.class.isAssignableFrom(this.term.getClass()))
			return function==null? null : function.getReturnType();
		else if(Constant.class.isAssignableFrom(this.term.getClass())) {
		
			if  (type==null) {
				if (object==null)
					return null;
				else
					return object.getClass();
			} else
				return type;
		}
		else if(Variable.class.isAssignableFrom(this.term.getClass())) {
			return type;		
		}
		
		return null;
	}
	
	
	public String toString() {

		if(ComplexTerm.class.isAssignableFrom(this.term.getClass())) {

			StringBuffer b = new StringBuffer();
			boolean f = true;
			b.append(function);
			b.append('(');
			for (Term t:terms) {
				if (f)
					f = false;
				else 
					b.append(',');
				b.append(t);
			}
			b.append(')');
			return b.toString();

		}
		else if(Constant.class.isAssignableFrom(this.term.getClass())) {
	
				StringBuffer b = new StringBuffer();
			//	if (isProxy())
			//		b.append('[');
				
			//	System.out.println(" const tostr isProxy = "+ isProxy());
			//	System.out.println(" const tostr isLiteral = "+ isLiteral());
			//	System.out.println(" const tostr getLiteral = "+ getLiteral());
			//	System.out.println(" const tostr getType = "+ getType().getName());
			//	System.out.println(" const tostr getObject = "+ getObject());
				
				b.append(isProxy()?ref: "\"" + object + "\"");
			//	if (isProxy())
			//		b.append(']');
				return b.toString();
		}
		else if(Variable.class.isAssignableFrom(this.term.getClass())) {
		
				StringBuffer b = new StringBuffer();
			//OV commented 250511 
			//	b.append('<');
				b.append(name);
			//	b.append('>');
				return b.toString();

		}
		
		
		return "";
		
	}
	public void accept(KnowledgeBaseVisitor visitor) {

		if(ComplexTerm.class.isAssignableFrom(this.term.getClass())) {
			
			ComplexTerm ct = (ComplexTerm) this.term;
			if (visitor.visit( ct)) {
				for (Term t:terms)
					t.accept(visitor);
			}		
			visitor.endVisit( ct);
			
		}
		else if(Constant.class.isAssignableFrom(this.term.getClass())) {			

			visitor.visit( (Constant) this.term);
			visitor.endVisit( (Constant) this.term);
		}
		else if(Variable.class.isAssignableFrom(this.term.getClass())) {
			visitor.visit( (Variable) this.term);
			visitor.endVisit(  (Variable) this.term);		
		}
		

		
		
	}
	@Override
	public int hashCode() {
		
		if(ComplexTerm.class.isAssignableFrom(this.term.getClass())) {
			
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((function == null) ? 0 : function.hashCode());
			result = prime * result + Arrays.hashCode(terms);
			return result;

		}
		else if(Constant.class.isAssignableFrom(this.term.getClass())) {			
		
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((object == null) ? 0 : object.hashCode());
			result = PRIME * result + ((ref == null) ? 0 : ref.hashCode());
			result = PRIME * result + ((type == null) ? 0 : type.hashCode());
			return result;			

		} else if(Variable.class.isAssignableFrom(this.term.getClass())) {				
		
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
			
		}
		
		return 0;
	}
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if(ComplexTerm.class.isAssignableFrom(this.term.getClass())) {

			if (getClass() != obj.getClass())
				return false;
			final ComplexTerm other = (ComplexTerm) obj;
			if (function == null) {
				if (other.getFunction() != null)
					return false;
			} else if (!function.equals(other.getFunction()))
				return false;
			if (!Arrays.equals(terms, other.getTerms()))
				return false;
			return true;

		}
		else if(Constant.class.isAssignableFrom(this.term.getClass())) {				
		
			if (getClass() != obj.getClass())
				return false;
			final Constant other = (Constant) obj;
			if (object == null) {
				if (other.getObject() != null)
					return false;
			} else if (!object.equals(other.getObject()))
				return false;
			if (ref == null) {
				if (other.getRef() != null)
					return false;
			} else if (!ref.equals(other.getRef()))
				return false;
			if (type == null) {
				if (other.getType() != null)
					return false;
			} else if (!type.equals(other.getType()))
				return false;
			return true;

		} else if(Variable.class.isAssignableFrom(this.term.getClass())) {				
		
			if (this.term.getClass() != obj.getClass())
				return false;
			final Variable other = (Variable) obj;
			if (name == null) {
				if (other.getName() != null)
					return false;
			} else if (!name.equals(other.getName()))
				return false;
			if (type == null) {
				if (other.getType() != null)
					return false;
			} else if (!type.equals(other.getType()))
				return false;
			return true;

		}
		
		return false;
	}

	public void setType(Class t) {

		if(Constant.class.isAssignableFrom(this.term.getClass())) {				
			
			checkTypeConsistency(object,t);
			this.vartype = t;
			
		} else if(Variable.class.isAssignableFrom(this.term.getClass())) {				
			this.type = type;
		}
		
		
	}
	
	
	// constant
	public Object getObject() {
		return this.object;		
	}
	
	public void setObject(Object o) {
		checkTypeConsistency(o,type);
		this.object = o;
	}
	
	private void checkTypeConsistency(Object o,Class t) {
		if (o!=null && t!=null && !t.isAssignableFrom(o.getClass()))
			throw new IllegalArgumentException("Object "+ o + " and type " + t + " are inconsistent, the type must be a supertype of the object type");
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	/**
	 * Whether this is just a placeholder to access the object or a real object. 
	 * @return
	 */
	public boolean isProxy() {
		return this.object==null && this.ref!=null;
	}
	/**
	 * Whether this is a literal, in particular a string.
	 * @return
	 */
	public boolean isLiteral() {
		Class type = getType();
		if (this.object==null) return false; // proxy
		else return (type==String.class)  || type.isPrimitive() || java.lang.Number.class.isAssignableFrom(type) || type == Boolean.class;
	}

	// return a Java literal prepresenting this object, or null if this isn't possible
	public String getLiteral() {
		if (object!=null) {
			if (object instanceof String)
				return "\"" + object + "\"";
			else if (object instanceof Number || object instanceof Boolean || object.getClass().isPrimitive())
				return String.valueOf(object);
		}
		return null;
	}

	
	// variable

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

	

}
