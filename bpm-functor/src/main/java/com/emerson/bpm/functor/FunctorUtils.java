package com.emerson.bpm.functor;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.Predicate;

import com.emerson.bpm.api.Session;
import com.emerson.bpm.functor.NumberComparisonPredicate.Criterion;
import com.emerson.bpm.functor.cmp.AlphaNumeric;
import com.emerson.bpm.functor.cmp.ByteNumeric;
import com.emerson.bpm.functor.cmp.CharNumeric;
import com.emerson.bpm.functor.cmp.DateNumeric;
import com.emerson.bpm.functor.cmp.DoubleNumeric;
import com.emerson.bpm.functor.cmp.FloatNumeric;
import com.emerson.bpm.functor.cmp.IntNumeric;
import com.emerson.bpm.functor.cmp.Numeric;
import com.emerson.bpm.functor.cmp.ShortNumeric;

public class FunctorUtils {
	

	public enum NumericTypes {
		SHORT,
		INTEGER,
		LONG,
		FLOAT,
		DOUBLE,
		BOOLEAN,
		CHARACTER,
		BYTE,
		STRING
	};
	public static Numeric checkPrimitive(Class input, Criterion crit){

		Object ob = null;
		try {
			ob = input.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return checkPrimitive(ob, crit);
	}


	public static Numeric checkPrimitive(Object input, Criterion crit){
	    try{
	            if (input instanceof Short)
	            	return  new ShortNumeric(input, crit);
	            else if(input instanceof Integer)
	            	return new IntNumeric(input, crit);
	            else if(input instanceof Long)
	            	return new LongNumeric(input, crit);
	            else if(input instanceof Float)
	            	return new FloatNumeric(input, crit);
	            else if(input instanceof Double)
	            	return new DoubleNumeric(input, crit);
	            else if(input instanceof Boolean)
	            	return numericTypeToComparator(NumericTypes.BOOLEAN);
	            else if(input instanceof Character)
	            	return new CharNumeric(input, crit);
	            else if(input instanceof Byte)
	            	return new ByteNumeric(input, crit);
	            else if(input instanceof Date)
	            	return new DateNumeric(input, crit);    
	            else if(String.class.isAssignableFrom(input.getClass())) 
	            	return new AlphaNumeric(input, crit);
	         }  
	    catch (NumberFormatException e){
	        System.out.println("Exception occur = "+e);
	    }
	    
	    return null;
	}


	private static Numeric numericTypeToComparator(NumericTypes s) {

		return null;
	}

	public static Predicate getPredicateByName(Session session, String predicateName) {

		List predicates = session.getWorkingMemory().getPredicates();

		return findByName(session, predicates, predicateName);
		
		
	}

	private static Predicate findByName(Session session, List predicates, String displayName2) {

		List<NamedPredicate> preds = 
				(List<NamedPredicate>) predicates.stream()
		.filter(x -> (x instanceof NamedPredicate))
			.filter(m -> ((NamedPredicate)m).names().contains(displayName2))
				.collect(Collectors.toList());
		
		assert(preds.size() == 1);	
		
		return preds.get(0);		
	}

	private Predicate findPredicate(List predicates, String displayName2) {

		List<BooleanPredicate> preds = 
					(List<BooleanPredicate>) predicates.stream()
						.filter(x -> (x instanceof BooleanPredicate))
							.filter(m -> m.toString().equals(displayName2))
								.collect(Collectors.toList());
		
				assert(preds.size() == 1);	
		
		return preds.get(0);		
	}

	 public static String makePredicate(String s){
         String[] parts = s.split(" ");
         String camelCaseString = "";
         for (String part : parts){
             if(part!=null && part.trim().length()>0)
            camelCaseString = camelCaseString + toProperCase(part);
             else
                 camelCaseString=camelCaseString+part+" ";   
         }
         return camelCaseString;
     }

	    private static String toProperCase(String s) {
            String temp=s.trim();
            String spaces="";
            if(temp.length()!=s.length())
            {
            int startCharIndex=s.charAt(temp.indexOf(0));
            spaces=s.substring(0,startCharIndex);
            }
            temp=temp.substring(0, 1).toUpperCase() +
            spaces+temp.substring(1).toLowerCase()+" ";
            return temp;
        }

	    

	    public static String getSimpleName(Class cks) {
			String className = cks.getName();			
			return getSimpleName(className);
		}			
			
		public static String getSimpleName(String cname) {
			int dpos = cname.lastIndexOf(".");			
			return cname.substring(dpos+1);
		}
		
	public static List<int[]> generateCombinations(List<Set> setOfSets, int n, int r,
			ProcessCombination comboCB, ProcessGlobal procGlobal) {

		
		int len = setOfSets.size();
		int k = 0;
		int d = 1;
		for(int i=0;i<len;i++) {
			
			try {
			
					Set first = setOfSets.get(k);
					Set next = setOfSets.get(d);
					
				//	comboCB.process(first, next, procGlobal);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
		
			List<int[]> combinations = new ArrayList<>();
		    int[] combination = new int[r];

		    // initialize with lowest lexicographic combination
		    for (int i = 0; i < r; i++) {
		        combination[i] = i;
		    }

		    while (combination[r - 1] < n) {
		        combinations.add(combination.clone());

		         // generate next combination in lexicographic order
		        int t = r - 1;
		        while (t != 0 && combination[t] == n - r + t) {
		            t--;
		        }
		        combination[t]++;
		        for (int i = t + 1; i < r; i++) {
		            combination[i] = combination[i - 1] + 1;
		        }
		    }

		    return combinations;
		}		


	static int seed = 23;
	static char [] hashstore;
	public static int hash(String queryName) {

			try {
				hashstore = new char[256];

				MessageDigest md = MessageDigest.getInstance("MD5");
				
				md.update(queryName.getBytes());
				
				byte [] bytes = md.digest();
							
				return bytes.hashCode();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}

	 public static int hashmerge(int hash, Class input) {

		    int mhash = computeClassHash(input);
		 
		 	mhash = hash * 37 + mhash;
		    return mhash;
	}	 
	 
	 private static int computeClassHash(Class input) {
	
		 Field [] fs = input.getDeclaredFields();
		 
		 int nhash = 0;
		 for(Field fa : fs) {
			 
			 String fname = fa.getName();
			 Class ftype = fa.getType();

			  nhash = nhash * 37 + new String [] { fname, ftype.getName() }.hashCode();			 
		 }
		 
		 return nhash;
	}


	 private static boolean compareHash(int hash1, int hash2) {
		 
		 if(hash1 == hash2)
			 return true;
		 
		 return false;		 
	 }


	public static int calculateQueryHash(String qry, List<Class> ets) {

		int queryHash = hash(qry);

		for(Class kz : ets)
			queryHash = FunctorUtils.hashmerge(queryHash, kz);

		return queryHash;

	}



}