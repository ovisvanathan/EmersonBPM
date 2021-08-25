/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.emerson.bpm.api;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salaboy
 */
public class COMPARATOR {

	public static final int MATCHES_VAL = 0;
	public static final int EQUALS_VAL = 1;
	public static final int EQUAL_TO_VAL = 2;
	public static final int NOT_EQUAL_VAL = 3;
	public static final int NOT_EQUAL_DIFF_VAL = 4;
	public static final int GT_VAL = 5;
	public static final int LT_VAL = 6;
	public static final int GTE_VAL = 7;
	public static final int LTE_VAL = 8;
	public static final int LOGICAL_NOT_VAL = 9;
	public static final int LOGICAL_AND_VAL = 10;
	public static final int LOGICAL_OR_VAL = 11;
	public static final int NOP_VAL = 12;
	public static final int EXISTS_VAL = 13;
	public static final int NOT_EXISTS_VAL = 14;
	public static final int MANY_VAL = 15;
	public static final int CONTAINS_VAL = 15;

	
	public static final COMPARATOR MATCHES = new COMPARATOR("~=", "MATCHES", MATCHES_VAL);
	public static final COMPARATOR EQUALS = new COMPARATOR("==", "EQUALS", EQUALS_VAL);
	
	public static final COMPARATOR EQUAL_TO = new COMPARATOR("===", "EQUAL_TO", EQUAL_TO_VAL);
	public static final COMPARATOR NOT_EQUAL = new COMPARATOR("!=", "NOT_EQUAL", NOT_EQUAL_VAL);
	public static final COMPARATOR NOT_EQUAL_DIFF = new COMPARATOR("<>", "NOT_EQUAL_DIFF", NOT_EQUAL_DIFF_VAL);
	public static final COMPARATOR GT = new COMPARATOR(">", "GT", GT_VAL);
	public static final COMPARATOR LT = new COMPARATOR("<", "LT", LT_VAL);
	public static final COMPARATOR GTE = new COMPARATOR(">=", "GTE", GTE_VAL);
	public static final COMPARATOR LTE = new COMPARATOR("<=", "LTE", LTE_VAL);
	public static final COMPARATOR LOGICAL_NOT = new COMPARATOR("!", "LOGICAL_NOT", LOGICAL_NOT_VAL);

	public static final COMPARATOR LOGICAL_AND = new COMPARATOR("&&", "LOGICAL_AND", LOGICAL_AND_VAL);
	public static final COMPARATOR LOGICAL_OR = new COMPARATOR("||", "LOGICAL_OR", LOGICAL_OR_VAL);
	public static final COMPARATOR NOP = new COMPARATOR("", "NADA", NOP_VAL);
	public static final COMPARATOR EXISTS = new COMPARATOR("!!", "EXISTS", EXISTS_VAL);
	public static final COMPARATOR NOT_EXISTS = new COMPARATOR("!~", "NOT_EXISTS", NOT_EXISTS_VAL);
	public static final COMPARATOR MANY = new COMPARATOR("**", "MANY", MANY_VAL);

	public static final COMPARATOR CONTAINS = new COMPARATOR("=!", "CONTAINS", CONTAINS_VAL);

	
 //   private final Object[] values;

    String symbol, name;
    int value;
    
    public COMPARATOR(String symbol, String name, int value) {
    	this.symbol = symbol;
    	this.name = name;
    	this.value = value;
    }
    
//	 COMPARATOR(Object... vals) {
 //        values = vals;
  //   }

     public String str() {
 //        return (String) values[1];
    	 return name;
     }

     public String symbol() {
 //        return (String) values[0];
    	 return symbol;
     }

	public int getValue() {
		return this.value;
	}

}
