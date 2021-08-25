package com.emerson.bpm.nodes.match.el;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.emerson.bpm.nodes.match.el.ELExprResolver.Customer;
import com.emerson.bpm.nodes.match.el.api.FunctionMapperImpl;
import com.emerson.bpm.nodes.match.el.util.ELUtils;

public class ELFunctionMapper extends FunctionMapperImpl {

		private Map<String, ELFunction> functions;

		public ELFunctionMapper() {
			
		}

		public void	addFunction(String prefix, String localName, String target) {
			   
			if (this.functions == null) {
		            this.functions = new HashMap<String, ELFunction>();
		        }
		    
			Method m =  ELUtils.findGetMethod(Customer.class, "custId");

				ELFunction f = new ELFunction(prefix, localName, m, target);
		        synchronized (this) {
		            this.functions.put(prefix+":"+localName, f);
		        }
		}

		@Override
		public Method resolveFunction(String prefix, String localName) {

		     if (this.functions != null) {
		            ELFunction f = this.functions.get(prefix + ":" + localName);
		            return f.getMethod();
		        }
		        return null;
		}
	
}
