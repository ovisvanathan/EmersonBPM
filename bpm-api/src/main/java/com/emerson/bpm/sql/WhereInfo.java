package com.emerson.bpm.sql;

import java.util.List;

import com.emerson.bpm.sql.QbWhereOperator;

public class WhereInfo
{
	public QbSQLField m_field;
	public QbWhereOperator m_op;
	public boolean m_orClause; /* True if this clause should be preceded by OR. */
	public String m_custom;

	public boolean m_startBracket;
	public boolean m_endBracket;
	
	public int m_inCount;   /* The number of placeholders we have to create for an IN clause. */
	public boolean m_notIn; /* True if it is a NOT IN clause. */

	public WhereInfo() { }
	
	public List placeHolders;
	
	public WhereInfo(QbSQLField field, QbWhereOperator op)
	{
		m_field = field;
		m_op = op;
	}
	
	public QbSQLField getField() {
		return m_field;
	}
	
	public QbWhereOperator getWhereOperator() {
		return m_op;
	}
	
	
}

