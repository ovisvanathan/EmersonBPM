package com.emerson.bpm.nodes.match;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.api.OrderedSet;
import com.emerson.bpm.api.UtilsServiceProvider;
import com.emerson.bpm.nodes.match.sql.QbField;
import com.emerson.bpm.sql.QbSQLField;
import com.emerson.bpm.sql.QbSQLQueryData;
import com.emerson.bpm.sql.QbWhereOperator;
import com.emerson.bpm.util.ServiceFactory;

/*
 * The base class for all match comparators
 * provides default implementations for the comparators specified
 * in @link com.monarch.v2.react.nodes.COMPARATOR.
 * 
 * These include EQUALS, GT, LT, GTE, LTE etc.
 */
public class DefaultComparator implements ClauseComparator {
	
	COMPARATOR cmp;
	Object o1, o2;
	
	boolean isSet = true;

	Object bindData;
	
	public Object getBindData() {
		return bindData;
	}

	public void setBindData(Object bindData) {
		this.bindData = bindData;
	}

	@Inject
	protected Map<Class, Method[]> beanGettersMap;

	
	@Override
	public Object [] getFieldNames() {
		return fieldNames;
	}

	Object [] fieldNames;

	Object [] whereArgs;

	@Override
	public Object[] getWhereArgs() {
		return whereArgs;
	}

	protected UtilsServiceProvider EmersonUtils = (UtilsServiceProvider) 
			ServiceFactory.getUtilsProvider();


	//local caching
	protected Object [] queryTables;

	//local caching	
	protected String [] queryFields;

	
	/* A bean class holding query parameters */
	protected QbSQLQueryData queryData;
		
	public QbSQLQueryData getQueryData() {
		return queryData;
	}

	static ClauseComparator defaultComparator;
	
	@Inject
	OrderedSet sequenceSet;
	
	boolean isStringEquals;

	private QueryParams queryParams;

	public OrderedSet getDeck() {
		return sequenceSet;
	}

	public void setSequenceSet(SequenceSet sequenceSet) {
		this.sequenceSet = (OrderedSet) sequenceSet;
	}

	public DefaultComparator(COMPARATOR cmp) {
		this.cmp = cmp;
	}

	public DefaultComparator(String symbol, String name, int val) {
		this.cmp = new COMPARATOR(symbol, name, val);
	}

	public DefaultComparator(String field1, Object field2) {
		this.fieldNames = new Object [] { field1, field2 };		
		this.cmp = COMPARATOR.EQUALS;
	}
	
	
	public boolean isStringMode() {
		return isStringEquals;
	}

	public void setStringMode(boolean isStringEquals) {
		this.isStringEquals = isStringEquals;
	}

	/*
	class DefaultEqualsComparator extends DefaultComparator {
	
		public DefaultEqualsComparator(COMPARATOR cmp) {
			this.isStringEquals = false;
		}

		public DefaultEqualsComparator(COMPARATOR cmp, boolean isStringEquals) {
			this.isStringEquals = isStringEquals;
		}
		
		@Override
		public boolean evaluate(Object o) throws Exception {

			System.out.println("comp eval defeq o =" + o.getClass().getName());

			if(!isSet)
				throw new ValueNotSetException("values not set to compare");
			
			int result = 0;
			if(this.isStringEquals)
				result = ((String) o1).compareTo( ((String) o2) );
			else {
					BigDecimal b1 = new BigDecimal( ((Double) o1).doubleValue());
				    BigDecimal b2 = new BigDecimal( ((Double) o2).doubleValue());
				    result =  b1.compareTo(b2);
			}
			
			if(result ==0)
				return true;
			
			return false;
		}
		
	}

	
	class DefaultGTELTEComparator extends DefaultComparator {
		
		boolean isGreater, isEquals;
		COMPARATOR cmp;
		boolean isStringEquals;
		
		public DefaultGTELTEComparator(COMPARATOR cmp, boolean isGreater, boolean isEquals) {
			this.isGreater = isGreater;
			this.isEquals = isEquals;
			this.cmp = cmp;
		}

		public DefaultGTELTEComparator(COMPARATOR cmp, boolean isGreater, boolean isEquals, boolean isStringEquals) {
			this.isGreater = isGreater;
			this.isEquals = isEquals;
			this.cmp = cmp;
			this.isStringEquals = isStringEquals;
		}

		@Override
		public boolean evaluate(Object o) {

			System.out.println("comp eval def o = " + o.getClass().getName());
			
			int result = 0;
			if(this.isStringEquals) {

				result = ((String) o1).compareTo( ((String) o2) );
				
				switch(cmp) {
				
				case GT:
					if(result > 0)
						return true;
					return false;
					
				case GTE:
					if(result >= 0)
						return true;
					return false;
					
				case LT:
					if(result < 0)
						return true;
					return false;

				case LTE:
					if(result <= 0)
						return true;
					
					return false;
				}
				
			} else {
					BigDecimal b1 = new BigDecimal( ((Double) o1).doubleValue());
				    BigDecimal b2 = new BigDecimal( ((Double) o2).doubleValue());
				    result =  b1.compareTo(b2);

					switch(cmp) {
					
					case GT:						
						if(result > 0)
							return true;
						return false;
						
					case GTE:						
						if(result >= 0)
							return true;
						return false;
						
					case LT:
						if(result < 0)
							return true;
						return false;

					case LTE:
						if(result <= 0)
							return true;
						
						return false;
					}
					
			}
			
			return false;
				
		}
		
	}


	public static ClauseComparator getDefaultComparatorForComp(COMPARATOR cmp2) {
		
		DefaultComparator defcomp = new DefaultComparator();
		switch(cmp2) {
		
		case EQUALS:
			   defaultComparator = defcomp.new DefaultEqualsComparator(cmp2, true);
		       break;
		case GT:
		//	   this.defaultComparator = new DefaultGTELTEComparator(boolean isGreater, boolean isEquals);
			   defaultComparator = defcomp.new DefaultGTELTEComparator(cmp2, true, false);
			   break;
		case LT:
			   defaultComparator = defcomp.new DefaultGTELTEComparator(cmp2, false, false);
			   break;
		case GTE:
			   defaultComparator = defcomp.new DefaultGTELTEComparator(cmp2, true, true);
			   break;
		case LTE:
			   defaultComparator = defcomp.new DefaultGTELTEComparator(cmp2, false, true);
			   break;
		default:
			System.out.println("unknown comparison type");
		
		}
		return defaultComparator;
	}
	*/

	@Override
	public boolean evaluate(Collection fieldVal, Object valueToCheck) {

		return false;
	}

	
	@Override
	public OrderedSet getSequenceSet() {
		return this.sequenceSet;
	}
	
	public void setQueryData(QueryData queryData) {
		this.queryData = queryData;
	}

	@Override
	public COMPARATOR get() {

		return this.cmp;
	}

	@Override
	public QbSQLField qbfield() {
		return (QbField) this.whereArgs[0];
	}

	@Override
	public QbWhereOperator qbwhere() {
		return (QbWhereOperator) this.whereArgs[1];
	}

	@Override
	public Object qbvalue() {
		return this.whereArgs[2];
	}

//	@Override
//	public Operator getOperator() {
		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public boolean isTemporal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getValue() {
		return this.cmp.getValue();
	}

	@Override
	public void setQueryData(QbSQLQueryData queryData) {
		this.queryData = queryData;
	}

	
}
