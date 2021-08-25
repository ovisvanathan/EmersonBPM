package com.emerson.bpm.nodes.match;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.util.IncompatibleTypesException;

public class TYPEID<E> implements Comparable<Object []> {
	
	static int VALUE_POS = 4;	
	static int INTS = 0;
	static int LONGS = 1;
	static int DOUBLES = 2;
	static int FLOATS = 3;
	static int SHORTS = 4;
	static int BOOLS = 5;
	static int DATES = 6;
	static int BYTES = 7;
	static int STRINGS = 8;
	
	static int PRIMROWS = 9;

	static Object [][] primValues = 
		{	
			new Object [] { "int", int.class, Integer.TYPE, 2, null },
			new Object [] { "long", long.class, Long.TYPE, 3, null },
			new Object [] { "double", double.class, Double.TYPE, 5, null },
			new Object [] { "float", float.class, Float.TYPE, 4, null },
			new Object [] { "short", short.class, Short.TYPE, 1, null },
			new Object [] { "boolean", boolean.class, Boolean.TYPE, 27, null },
			new Object [] { "date", Date.class, Date.class, 35, null },
			new Object [] { "byte", byte.class, Byte.TYPE, 0, null },
			new Object [] { "string", String.class, String.class, 52, null },
	
		};
	
	
	Object [] oa;
	Class typeClass;

	Integer rownum;
	
	public TYPEID(Object [] oa) {
		this.oa = oa;	
		this.typeClass = (Class) oa[1];
		this.rownum = (Integer) oa[3];
	}

	@Override
	public int compareTo(Object [] o) {
		
		try {
			if(this.typeClass.isAssignableFrom(Number.class)) {			
				Class objClass = (Class) o[1];			
				if(objClass.isAssignableFrom(Number.class)) {			
					Integer rownum2 = (Integer) o[3];		
					return this.rownum.compareTo(rownum2);		
				} else
						throw new IncompatibleTypesException("cannot compare types Number and string");
			} else if(this.typeClass.isAssignableFrom(String.class)) {			
				Class objClass = (Class) o[1];			
				if(objClass.isAssignableFrom(String.class)) {			
					Integer rownum2 = (Integer) o[3];		
					return this.rownum.compareTo(rownum2);		
				} else 
					throw new IncompatibleTypesException("cannot compare types Number and string");
			} else if(this.typeClass.isAssignableFrom(Date.class)) {			
				Class objClass = (Class) o[1];			
				if(objClass.isAssignableFrom(Date.class)) {			
					Integer rownum2 = (Integer) o[3];		
					return this.rownum.compareTo(rownum2);		
				} else 
					throw new IncompatibleTypesException("cannot compare types Number and string");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return 0;
	}

	public static TYPEID getPrimitiveType(Object fval) {

		if(Number.class.isAssignableFrom(fval.getClass())) {
			int i = (fval == null)? 0 : (int) fval;			
			primValues[INTS][VALUE_POS] = i; 
			return new TYPEID(primValues[INTS]);
		} else if(String.class.isAssignableFrom(fval.getClass())) {
			int i = (fval == null)? 0 : (int) fval;			
			primValues[INTS][VALUE_POS] = i; 
			return new TYPEID(primValues[INTS]);
		} else if(boolean.class.isAssignableFrom(fval.getClass())) {
			int i = (fval == null)? 0 : (int) fval;			
			primValues[INTS][VALUE_POS] = i; 
			return new TYPEID(primValues[INTS]);
		}  else if(Date.class.isAssignableFrom(fval.getClass())) {
			int i = (fval == null)? 0 : (int) fval;			
			primValues[INTS][VALUE_POS] = i; 
			return new TYPEID(primValues[INTS]);
		}

		return null;
	}

	public static boolean isPrimitiveType(Class fval) {

		if(int.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(long.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(double.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(float.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(short.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(boolean.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(Date.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(byte.class.isAssignableFrom(fval.getClass())) {
			return true;
		} else if(String.class.isAssignableFrom(fval.getClass())) {
			return true;
		}
		
		return false;

	}

	
	
	public static TYPEID getPrimitiveTypeForObject(Object fval) {

		
		if(int.class.isAssignableFrom(fval.getClass())) {
			int i = (fval == null)? 0 : (int) fval;			
			primValues[INTS][VALUE_POS] = i; 
			return new TYPEID(primValues[INTS]);
		} else if(long.class.isAssignableFrom(fval.getClass())) {
			long i = (fval == null)? 0 : (long) fval;			
			primValues[LONGS][VALUE_POS] = i; 
			return new TYPEID(primValues[LONGS]);
		} else if(double.class.isAssignableFrom(fval.getClass())) {
			double i = (fval == null)? 0 : (double) fval;			
			primValues[DOUBLES][VALUE_POS] = i; 
			return new TYPEID(primValues[DOUBLES]);
		} else if(float.class.isAssignableFrom(fval.getClass())) {
			float i = (fval == null)? 0 : (float) fval;			
			primValues[FLOATS][VALUE_POS] = i; 
			return new TYPEID(primValues[FLOATS]);
		} else if(short.class.isAssignableFrom(fval.getClass())) {
			short i = (fval == null)? 0 : (short) fval;			
			primValues[SHORTS][VALUE_POS] = i; 
			return new TYPEID(primValues[SHORTS]);
		} else if(boolean.class.isAssignableFrom(fval.getClass())) {
			boolean i = (fval == null)? false : (boolean) fval;			
			primValues[BOOLS][VALUE_POS] = i; 
			return new TYPEID(primValues[BOOLS]);
		} else if(Date.class.isAssignableFrom(fval.getClass())) {
			Date i = (fval == null)? null : (Date) fval;			
			primValues[DATES][VALUE_POS] = i; 
			return new TYPEID(primValues[DATES]);
		} else if(byte.class.isAssignableFrom(fval.getClass())) {
			byte i = (fval == null)? 0 : (byte) fval;			
			primValues[BYTES][VALUE_POS] = i; 
			return new TYPEID(primValues[BYTES]);
		}
		
		return null;
	}
	
	public static TYPEID reconcileTypes(TYPEID typeid1, TYPEID typeid2) {
		
		List<TYPEID> typesList = new ArrayList<>();
		
		typesList.add(typeid1);
		typesList.add(typeid2);
				
		return typesList.get(0);
		
	}
	
	public static boolean evaluate(Object a, Object b, COMPARATOR cmp) throws Exception {

		try {
			TYPEID ta1 = getPrimitiveType(a);
			TYPEID ta2 = getPrimitiveType(b);
			
			Class reconClass = getFinalType(ta1, ta2);

			if(reconClass.isAssignableFrom(String.class))
				return new TYPEIDEVALUATOR1(cmp, true).evaluate( (String) a, (String)  b);
			else {
				return new TYPEIDEVALUATOR2(cmp).evaluate( (Number) a, (Number) b);				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}	
	}
	

	private static Class getFinalType(TYPEID typeid1, TYPEID typeid2) {

		TYPEID recon = reconcileTypes(typeid1, typeid2);
		
		Class typeClass = recon.getType();
		
		return typeClass;
	}
	
	

	private Class getType() {
		return (Class) this.oa[1];
	}

	static class TYPEIDEVALUATOR<E>  implements Evaluator2<E> {
		protected COMPARATOR cmp;

		public TYPEIDEVALUATOR(String o) {
		}

		public TYPEIDEVALUATOR(COMPARATOR cmp) {
			this.cmp = cmp;
		}
		
		@Override
		public boolean evaluate(E e, E b) throws Exception {
	
			return false;
		}		
	}

	static class TYPEIDEVALUATOR1<E extends String> extends TYPEIDEVALUATOR<E>  {
		
		boolean ignoreCase;
		
		public TYPEIDEVALUATOR1(COMPARATOR cmp, boolean ignoreCase) {
			super(cmp);
			this.ignoreCase = ignoreCase;
		}
		
		@Override
		public boolean evaluate(String a, String b) throws Exception {

			int r = (a).compareTo(b);
			
			switch(cmp.getValue()) {
			
			case COMPARATOR.EQUALS_VAL:
						if(!ignoreCase)
							return ((String) a).equals((String) b);
						else
							return ((String) a).equalsIgnoreCase((String) b);									
			case COMPARATOR.GT_VAL:
					return r > 0;
			case COMPARATOR.LT_VAL:
					return r < 0;
			case COMPARATOR.GTE_VAL:
					return r >= 0;
			case COMPARATOR.LTE_VAL: 		
				return r <= 0;
			case COMPARATOR.NOT_EQUAL_VAL:
				return r != 0;
			case COMPARATOR.MATCHES_VAL:
				return ((String) a).matches((String) b);
			
			}
			
			return false;
		}
		
		
	}

	static class TYPEIDEVALUATOR2<E extends Number>  extends TYPEIDEVALUATOR<E>  {

		public TYPEIDEVALUATOR2(COMPARATOR cmp) {
			super(cmp);
		}
		
		@Override
		public boolean evaluate(E a, E b) throws Exception {

			E obj1 = (E) a;
			E obj2 = (E) b;

			Integer ar1 = (Integer) a;
			
			Integer ar2 = (Integer) b;

			int r = ar1.compareTo(ar2);
			
			
			switch(cmp.getValue()) {
			
			case COMPARATOR.EQUALS_VAL:
					return r == 0;
			case COMPARATOR.GT_VAL:
					return r > 0;
			case COMPARATOR.LT_VAL:
					return r < 0;
			case COMPARATOR.GTE_VAL:
					return r >= 0;
			case COMPARATOR.LTE_VAL: 		
				return r <= 0;
			case COMPARATOR.NOT_EQUAL_VAL:
				return r != 0;			
			}
			
			return false;
		}
		
	}

}
