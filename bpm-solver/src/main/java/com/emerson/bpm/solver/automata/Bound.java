package com.emerson.bpm.solver.automata;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public abstract class Bound<V> extends Observable {

	V boundObj;

//	Cloner cloner=new Cloner();
	
	ProviderExt p;
	
	MyInvocationHandler handler;
	ProviderExt proxy;
	Manifest manifest;
	
	public Bound() {
	}

	public Bound(ProviderExt p, V dataBuf) {
		this.p = p;

		this.boundObj = dataBuf;
		
		buildManifest();
		
		handler = new MyInvocationHandler();
		
		handler.setOriginal(p);
		
		handler.setManifest(manifest);
		
		proxy = (ProviderExt) Proxy.newProxyInstance(
		                            ProviderExt.class.getClassLoader(),
		                            new Class[] { ProviderExt.class },
		                            handler);
		
	}

	private void buildManifest() {
		
		manifest = new Manifest();
		
		manifest.setClassName(this.p.getClass().getName());
		
		Method [] mds = this.p.getClass().getMethods();

		manifest.setNumMethods(mds.length);

		Map<String, Object> mmap = new HashMap<String, Object>();

		int ord = 0;
		for(Method m : mds) {
			
			Class [] ptypes = m.getParameterTypes();
			
			Class rtype = m.getReturnType();
		
			manifest.getMethods().add(m);

			String methodName = m.getName();
			
			if(mmap.containsKey(methodName))
				methodName = methodName + "_" + ++ord;

			mmap.put(methodName + "_PTYPES", ptypes);
			mmap.put(methodName + "_RTYPE", rtype);				
			mmap.put(methodName + "_NUMPARAMS", ptypes.length);
		
		}
		
		manifest.setMethodMap(mmap);
		
	}

	public V get() {
		return boundObj;
	}

	public void setBoundObj(V boundObj) {
		synchronized(this) {
			this.boundObj = boundObj;
		}
		
		 setChanged();
		 notifyObservers();
	}
	
	/*
	 * User provides implementation for update
	 */
	public abstract void update(Object o);
	/*
	{
	
		ProviderExt pxt =
				   (ProviderExt) Proxy.newProxyInstance(ProviderExt.class.getClassLoader(),
			                new Class[] { ProviderExt.class },
			                handler);

		IterData idata = new IterData();
		idata.cellNum = 2;
		idata.itemIndex = 3;
		DataBuf result = (DataBuf) pxt.getResult(idata);
		
//		this.setBoundObj(dobj);
	}
	*/
	
	static class Providers<T, V> implements ProviderExt<V> {
		DataBuf d;
		public Providers(DataBuf d) {
			this.d = d;
		}
		
		@Override
		public Object get() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public V getResult(V idata) {
			return (V) d;
		}

		@Override
		public void undo() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void redo() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public V getResult() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	
	public static void main(String [] args) {
		
		DataBuf d = new DataBuf();
		
		Providers pxs = new Providers(d);
		
		Bound b = new Bound(pxs, d) {

			@Override
			public void update(Object o) {
				
			}
		};
		
		d.setActiveIndex(10);
		b.update(10);

		/*
		ProviderExt p = new PVX(new DataBuf());
		MyInvocationHandler handler = new MyInvocationHandler();
		
		handler.setOriginal(p);
		
		ProviderExt pxt =
				   (ProviderExt) Proxy.newProxyInstance(ProviderExt.class.getClassLoader(),
			                new Class[] { ProviderExt.class },
			                handler);


		Map resultMap = pxt.getResults();
		*/
		
	}

	
	static class MyInvocationHandler implements InvocationHandler{
		
		ProviderExt pxt;
		Manifest manifest;
		
		  public void setManifest(Manifest manifest) {
			  this.manifest = manifest;
		  }
		
		  public void setOriginal(ProviderExt pxt) {
			  this.pxt = pxt;
		  }
		
		  public Object invoke(Object proxy, Method method, Object[] args)
		  throws Throwable {

			 System.out.println(" ikh method name = " + method.getName());

			 System.out.println(this.manifest.getMethods().size());
			 
			 return null;
			  
		  }
		}
	
	public void undo() {
		
		p.undo();
		
	}
	
	public void redo() {	
		p.redo();
	}

	public void clear() {
		this.boundObj = null;		
	}
	
}
