package com.emerson.bpm.functor;

public class CompoundPredicate {
	
	Expression exp;
	
//	Jep jep = new Jep();
		
	public CompoundPredicate(Expression e) {
	
		this.exp = e;
	
		 try {
		//	 	jep.addVariable("x", 10);
		//	 	jep.parse("x+1");
		     
		//	 	Object result = jep.evaluate();
		 			
			// 	System.out.println("x + 1 = " + result);
	//	 } catch (JepException e) {
		 } catch (Exception ex) {
			 	System.out.println("An error occurred: " + ex.getMessage());
		}
	
	}
}
