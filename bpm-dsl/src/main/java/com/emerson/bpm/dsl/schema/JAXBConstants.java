package com.emerson.bpm.dsl.schema;

import java.util.ArrayList;
import java.util.List;

public class JAXBConstants {
	
	public static List getList() {
		return new ArrayList<Class>() {{
		    add(RootNodeType.class);
		    add(ObjectNodeType.class);
		    add(AlphaNodeType.class);
		    add(FieldComparisonType.class);
		    add(ValuesComparatorType.class);
		    add(RTMNodeType.class);
		    add(JoinNodeType.class);
		    add(RulesIDContextType.class);
		    
		}};
	}
}
