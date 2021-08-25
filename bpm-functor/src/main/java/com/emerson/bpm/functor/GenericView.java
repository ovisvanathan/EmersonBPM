package com.emerson.bpm.functor;

import java.util.List;

public class GenericView<P, Q> {

	public static class Student {
		
	}
	
	public static class Course {
		
	}
	
	public static class Department {
		
	}
	
	public static class Project {
		
	}
	
	public class Shape {
		
	}
	
	public class Dimension {
		
	}
	
	public class Angle {
		
	}
	
	public class Distance {
	
	}
	
	public class Speed {
		
	}
	
	public class Time {
		
	}
	
	P p;
	Q q;
	
		public GenericView(P p, Q q) {

			this.p = p;
			
			this.q = q;
		
		}
	
		P getP() {
			return p;
		}
		
		Q getQ() {
			return q;		
		}
		
		public P viewer(P p) {
			return null;

		}

		public List viewer(P p, Q q) {
			return null;
		}

		Object k = null;
	
		P findByPos(Class<P> p2, int pos) {

			System.out.println("Enter FBP =  ");

			if(pos == 1)
				k = this.p;
			else
				k = this.q;

			System.out.println("p2 =  " + p2);

			System.out.println("kclass =  " + k.getClass());
			System.out.println("kclass 2=  " + (p2 == k.getClass()));

			if(p2 == k.getClass())
					return (P) k;

			System.out.println("Exit FBP =  ");

			return null;
		}



}
