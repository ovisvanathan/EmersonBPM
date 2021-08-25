package com.emerson.bpm.nodes.util;

import javax.inject.Inject;

import com.emerson.bpm.api.ClauseComparator;
import com.emerson.bpm.dsl.FieldProvider;

public class InjectorUtils {

	 public static ClauseComparator injectComparator(FieldProvider fp) {
		
		 /*
		if(fp instanceof FieldNameChecker) {		
			return factory.getFieldNameComparator(fp.getFieldName1(), fp.getFieldName2());			
		} else if(fp instanceof FieldValueChecker) {
			return new FieldValueComparator(fp.getFieldName1(), fp.getFieldVal(), fp.getComparator());						
		} else if(fp instanceof FieldListValChecker) {
			return new FieldListValueComparator(fp.getFieldName1(), fp.getFieldVal());									
		} else if(fp instanceof FieldJoinChecker) {
			return new FieldJoinComparator(fp.getFieldNames(), fp.getFieldValues());												
		}  else if(fp instanceof FieldCritChecker) {
			return new FieldCriteriaComparator(fp.getFieldValues());															
		} else if(fp instanceof DNE) {			
			return new DoesNotExistsComparator(fp.getFieldName1(), fp.getFieldName2());															
		}	
		*/
		
		return null;
	}



}
