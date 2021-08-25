package com.emerson.bpm.nodes.match.el;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;

import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.nodes.match.el.api.FunctionMapperImpl.Function;
import com.emerson.bpm.util.ServiceFactory;

public class ELFunction {

	protected transient Method m;
    protected String owner;
    protected String name;
    protected String[] types;
    protected String prefix;
    protected String localName;

	protected UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();

	
	public ELFunction(String prefix, String localName, Method meth, String owner) {
		this(prefix, localName, meth);
		this.owner = owner;
	}
		    
	        /**
	         * 
	         */
	        public ELFunction(String prefix, String localName, Method m) {
	            if (localName == null) {
	                throw new NullPointerException("LocalName cannot be null");
	            }
	            if (m == null) {
	                throw new NullPointerException("Method cannot be null");
	            }
	            this.prefix = prefix;
	            this.localName = localName;
	            this.m = m;
	        }
	        
	        public ELFunction() {
	            // for serialization
	        }
	    
	        /*
	         * (non-Javadoc)
	         * 
	         * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	         */
	        public void writeExternal(ObjectOutput out) throws IOException {
	            
	            out.writeUTF((this.prefix != null) ? this.prefix : "");
	            out.writeUTF(this.localName);
	            
	            if (this.owner != null) {
	                out.writeUTF(this.owner);
	            } else {
	                out.writeUTF(this.m.getDeclaringClass().getName());
	            }
	            if (this.name != null) {
	                out.writeUTF(this.name);
	            } else {
	                out.writeUTF(this.m.getName());
	            }
	            if (this.types != null) {
	                out.writeObject(this.types);
	            } else {
	                out.writeObject(EmersonUtils.toTypeNameArray(this.m.getParameterTypes()));
	            }
	        }
	    
	        /*
	         * (non-Javadoc)
	         * 
	         * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	         */
	        public void readExternal(ObjectInput in) throws IOException,
	                ClassNotFoundException {
	            
	            this.prefix = in.readUTF();
	            if ("".equals(this.prefix)) this.prefix = null;
	            this.localName = in.readUTF();
	            this.owner = in.readUTF();
	            this.name = in.readUTF();
	            this.types = (String[]) in.readObject();
	        }
	    
	        public Method getMethod() {
	            if (this.m == null) {
	                try {
	                    Class<?> t = Class.forName(this.owner, false,
	                                Thread.currentThread().getContextClassLoader());
	                    Class[] p = EmersonUtils.toTypeArray(this.types);
	                    this.m = t.getMethod(this.name, p);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	            return this.m;
	        }
	        
	        public boolean matches(String prefix, String localName) {
	            if (this.prefix != null) {
	                if (prefix == null) return false;
	                if (!this.prefix.equals(prefix)) return false;
	            }
	            return this.localName.equals(localName);
	        }
	    
	        /* (non-Javadoc)
	         * @see java.lang.Object#equals(java.lang.Object)
	         */
	        public boolean equals(Object obj) {
	            if (obj instanceof Function) {
	                return this.hashCode() == obj.hashCode();
	            }
	            return false;
	        }
	        
	        /* (non-Javadoc)
	         * @see java.lang.Object#hashCode()
	         */
	        public int hashCode() {
	            return (this.prefix + this.localName).hashCode();
	        }
	    
}
