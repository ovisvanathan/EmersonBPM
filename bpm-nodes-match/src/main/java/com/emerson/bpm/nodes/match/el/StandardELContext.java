package com.emerson.bpm.nodes.match.el;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.VariableMapper;

import org.apache.commons.lang3.RandomStringUtils;

import com.emerson.bpm.nodes.match.el.api.FunctionMapperImpl;

public class StandardELContext extends ELContext {

	private ELResolver resolver;

	String uniqueId;
	
//	public String getUniqueId() {
//		return uniqueId;
//	}

	public StandardELContext() {
//		uniqueId = RandomStringUtils.randomAlphanumeric(10);	
//		putContext(ELContext.class, this);
	}

	@Override
	public FunctionMapperImpl getFunctionMapper() {
		return new ELFunctionMapper();
	}

	@Override
	public Object getContext(Class key) {
		return null;
	}

	@Override
	public boolean isPropertyResolved() {
		return true;
	}

	@Override
	public VariableMapper getVariableMapper() {
//		return new VariableMapperImpl();
		return null;
	}

	@Override
	public void putContext(Class key, Object contextObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyResolved(boolean resolved) {
			
	}

	@Override
	public void setPropertyResolved(Object base, Object property) {
			
	}

	@Override
	public void notifyBeforeEvaluation(String expr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyAfterEvaluation(String expr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyPropertyResolved(Object base, Object property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLambdaArgument(String arg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getLambdaArgument(String arg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enterLambdaScope(Map<String, Object> args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLambdaScope() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object convertToType(Object obj, Class<?> targetType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ELResolver getELResolver() {
		
		if(this.resolver == null)
			this.resolver = new ValueResolver();
		return this.resolver;
		
	}

}
