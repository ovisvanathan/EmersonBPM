package com.emerson.bpm.nodes.match;

import com.emerson.bpm.util.PassthroughInvocationHandler;
import com.impetus.reflect.proxology.handlers.MethodCallInterceptor;

public class COMPARATORS {
				
    interface FieldCompIntf {
    	
    	void evaluate();
    	
		public Object doWithArgs(String s1, String s2, FieldComparator f);
    	
    }

    interface FieldComparator  {
    	
		public void fieldCompare(String x, String y);
    	
    }

    class MyGoodFieldComparator implements FieldComparator {
    	
    	public String s1, s2;
    	
    	public MyGoodFieldComparator(String s1, String s2) {
    		this.s1 = s1;
    		this.s2 = s2;    		
    	}

		@Override
		public void fieldCompare(String x, String y) {
			
		}

    }

    class MyGoodField implements FieldCompIntf {

		String s1;
		String s2;

    	MyGoodFieldComparator myGoodFieldComparator;
    	
		public MyGoodField(String f1, String f2, MyGoodFieldComparator myGoodFieldComparator) {

			this.myGoodFieldComparator = myGoodFieldComparator;
			
			s1 = this.myGoodFieldComparator.s1;
			s2 = this.myGoodFieldComparator.s2;
		
			evaluate();
		}

		@Override
		public void evaluate() {
			System.out.println("eval1");
	
			doWithArgs(s1, s2, new FieldComparator() {

				@Override
				public void fieldCompare(String x, String y) {
					System.out.println("dwa 2");

					System.out.println(x);
					System.out.println(y);
				}
				
			});			
		}

		@Override
		public Object doWithArgs(String s1, String s2, FieldComparator f) {
			System.out.println("dwa 1");
			f.fieldCompare(s1,  s2);
			return null;
		}

					
    } 		
	

    
	//	public static final MyGoodFieldComparator MyGoodFieldComparator = c1.new MyGoodFieldComparator();

	//	public void MyGoodFieldComparator(String s1, String s2 y, FieldComparator f);
			
	//		nf.filter(x);
	//	}
    	
    public COMPARATORS() {

    	String f1 = "", f2 = "";
    	String s1 = "blah";
    	String s2 = "duh";    	
    	
    	new MyGoodField(f1, f2, new MyGoodFieldComparator(s1, s2));
    	
    }
    
   
    
    public static void main(String args0[]) 
    { 
    	
    	
    //	COMPARATORS m1 = COMPARATORS.Matches;   	
    	
    	/*
    	COMPARATORS c1 = new COMPARATORS();
    	String f1, f2;
    	String s1 = "blah";
    	String s2 = "duh";    	
	*/
    	
    	try {
    		   MethodCallInterceptor interceptor = (proxy, method, args, handler) -> {
    			   Object result = handler.invoke(proxy, args);
    	      //      callDetails.add(String.format("%s: %s -> %s", method.getName(), Arrays.toString(args), result));
    	            return result;
    	        };

    	        CriteriaComparator proxy1 = PassthroughInvocationHandler.proxying(new TxnCrit(), CriteriaComparator.class);
    	        
    	      //  CriteriaComparator proxy = Proxies.interceptingProxy(new TxnCrit(), CriteriaComparator.class, null);

    	        proxy1.evaluate(new Object [] { "hi there" });

    	       // proxy.evaluate(new Object [] { "hi there" });
    	        
    	  //      assertThat(proxy.display(), equalTo("Arthur Putey (42)"));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	return (T) instance;    
    	
        	
    }

	private static void foo(Object doWithArgs) {
		
	}
        
}
