package com.emerson.bpm.sql;

/**
 * The where operator.
 */
public enum QbWhereOperator
{
	EQUALS("="),
	NOT_EQUALS("<>"),
	LESS_THAN("<"),
	GREATER_THAN(">"),
	LESS_THAN_EQUALS("<="),
	GREATER_THAN_EQUALS(">="),
	LIKE("LIKE"),
	NOT_LIKE("NOT LIKE"),
	BETWEEN("BETWEEN");
	
	private final String value;
	
	QbWhereOperator(String val)
	{
		value = val;
	}
	
	@Override
	public String toString()
	{
		return value;
	}
}
