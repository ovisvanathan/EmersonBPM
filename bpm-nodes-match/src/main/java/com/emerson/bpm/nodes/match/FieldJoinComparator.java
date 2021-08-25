package com.emerson.bpm.nodes.match;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.emerson.bpm.api.COMPARATOR;
import com.emerson.bpm.nodes.match.el.util.ELUtils;
import com.emerson.bpm.nodes.match.sql.QbField;
import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.sql.QbWhereOperator;
import com.emerson.bpm.sql.WhereInfo;
import com.picasso.paddle.annotation.Component;

/*
 * A Comparator that does a join as well as provides for 
 * where clause checking
 */
@Component
public class FieldJoinComparator<T>  extends FieldNameComparator implements ValueComparison {

		
	public FieldJoinComparator(String [] fields, Object ... whereArgs) {
		super(COMPARATOR.EQUALS);
		
		this.fieldNames = fields;			
		this.whereArgs = whereArgs;
		this.cmp = COMPARATOR.EQUALS;		
	}

	
	
	@Override
	public boolean evaluate(Object o) throws Exception {
	
		boolean eval1 = super.evaluate(o);

		boolean eval2 = evaluateWithMagic(new ClassFieldNameMatcher() {
			
			@Override
			public boolean evaluate(Object fieldObj,  String fieldName, Object valueToCheck) throws Exception {
				
				Method getter = ELUtils.findGetMethod(fieldObj, fieldName);
				
				//dont use fieldVal which is valueToCheck
				Object fieldVal1 = null;
				try {
					fieldVal1 = getter.invoke(fieldObj, null);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
				return TYPEID.evaluate(fieldVal1, valueToCheck, cmp);
			}
			
		});
		
		return eval1 && eval2;		
	}
	
	private boolean evaluateWithMagic(NameMatcher m) throws Exception {
	
			QbWhere whr = (QbWhere) this.queryData.getWhere();

			List<WhereInfo> winfos = whr.getWhereInfos();

			List<Object []> slots = whr.getWherePlaces();
			
			int x = 0;
			boolean eval4 = false;
			for(WhereInfo wi : winfos) {
				
				QbField qbfield = (QbField) wi.getField();
				QbWhereOperator oper = wi.getWhereOperator();		
				Object value = slots.get(x++)[2];

				eval4 &= m.evaluate(this.queryTables[0],  qbfield.toString(), value);					
			}
			
			return eval4;
	}

	
}
