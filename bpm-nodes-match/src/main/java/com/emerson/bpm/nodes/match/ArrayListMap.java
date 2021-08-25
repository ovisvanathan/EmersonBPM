package com.emerson.bpm.nodes.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ArrayListMap<S, F, U, O> {

	
	@FunctionalInterface
	interface TriFunction<A,B,C,R> {

	    R apply(A a, B b, C c);

	    default <V> TriFunction<A, B, C, V> andThen(
	                                Function<? super R, ? extends V> after) {
	        Objects.requireNonNull(after);
	        return (A a, B b, C c) -> after.apply(apply(a, b, c));
	    }
	}
	
	static class QBField {
		
		public String toString() {
			return "qbfield";
		}
	}
	
	static class QBOperator {

		public String toString() {
			return "qboper";
		}

	}
	
	
	
	static TriFunction<QBField, QBOperator, Object, QBField> tri1 = (x, y, z) -> { return x; };

	static TriFunction<QBField, QBOperator, Object, QBOperator> tri2 = (x, y, z) -> { return y; };

	static TriFunction<Object, QBOperator, Object, Object> tri3 = (x, y, z) -> { return z; };
	
	
	static QBField foo(TriFunction<QBField, QBOperator, Object, QBField> f) {
		
		QBField a = new QBField();
		QBOperator b = new QBOperator();		
		Object c1 = new Object();
	
		Object [] x = new Object[3];
		
		x[0] = a;
		
		x[1] = b;
			
		x[2] = c1;
		
		
		return f.apply(a, b, c1);
		
		
	}
	
	
	
	public static void main(String[] args) {

		
		try {
				
			List<TriFunction> rowcols = new ArrayList();
			
			rowcols.add(tri1);
			rowcols.add(tri2);
			rowcols.add(tri3);

				
			
				/*
				List row1 = new ArrayList();
				
				row1.add(new  AbstractMap.SimpleEntry<String, File>("s1", new File("")));

				List row2 = new ArrayList();
				
				row2.add(new  AbstractMap.SimpleEntry<String, URL>("s2", new File("").toURL()));
				
				List row3 = new ArrayList();
				
				row3.add(new  AbstractMap.SimpleEntry<Object, Object>("s3", new Object()));				
				
				*/

			TriFunction t1 = rowcols.get(0);
			
			System.out.println(
						foo(tri1)
					);

			/*
			TriFunction t2 = rowcols.get(1);
			
			System.out.println(t2.apply("s2", 2.87, 17));

			TriFunction t3 = rowcols.get(2);
			
			System.out.println(t3.apply("s5", false, 6.88));
			*/
			
			
			/*
			Method [] ms = TriFunction.class.getDeclaredMethods();

			for(Method m : ms) {
				
				java.lang.reflect.Type[] types = m.getGenericParameterTypes();
				
				for(java.lang.reflect.Type t : types)
					System.out.println(t.getTypeName());
				
			}
			*/
			
			
		//	System.out.println(x);

			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
