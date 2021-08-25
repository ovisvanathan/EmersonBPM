package com.emerson.bpm.sql;


public class QbUtils {

	public static String joinTypeToString(QbJoinType joinType)
	{
		switch (joinType)
		{
		case DEFAULT:
			return "";
		case LEFT_OUTER:
			return "LEFT OUTER";
		case RIGHT_OUTER:
			return "RIGHT OUTER";
		case INNER:
		case OUTER:
		case LEFT:
		case RIGHT:
		default:
			return joinType.toString();
		}
	}
	

}
