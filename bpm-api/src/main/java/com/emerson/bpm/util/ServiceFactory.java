package com.emerson.bpm.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import com.emerson.bpm.api.MessageServiceProvider;
import com.emerson.bpm.api.ServiceProvider;
import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.dsl.FieldProviderFactory;
import com.picasso.paddle.Injector;

public abstract class ServiceFactory {

	static Map<String, ServiceProvider> servicesMap = new HashMap();

	private static String MESSAGES;
	private static String UTILS;
	
	static Session session;
	
	static Injector paddleInjector;
	
	static FieldProviderFactory fieldProviderFactory;
	
	public static Session getSession() {
		return session;
	}

	public static void setSession(Session session) {
		ServiceFactory.session = session;
	}

	static {
		try {
			loadProviders();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadProviders() {
			
	      // load our plugin
	      ServiceLoader<ServiceProvider> serviceLoader =
	    		  ServiceLoader.load(ServiceProvider.class);

	      //checking if load was successful
	      for (ServiceProvider provider : serviceLoader) {
	      
	         if(provider instanceof MessageServiceProvider)
	        	 servicesMap.put(ServiceFactory.MESSAGES, provider);
	         else if(provider instanceof UtilsServiceProvider)
	        	 servicesMap.put(ServiceFactory.UTILS, provider);
		        
	      }
	   }

	   public static ServiceProvider getMessageProvider() {
		   return servicesMap.get(MESSAGES);
	   }

	   public static ServiceProvider getUtilsProvider() {
		   return servicesMap.get(UTILS);
	   }

	public static void setInjector(Injector paddleInjector) {
		ServiceFactory.paddleInjector = paddleInjector;
	}

	public static void setFieldProviderFactory(FieldProviderFactory factory) {
		ServiceFactory.fieldProviderFactory = factory;
	}

	public static FieldProviderFactory getFieldProviderFactory() {
		return fieldProviderFactory;
	}

}
