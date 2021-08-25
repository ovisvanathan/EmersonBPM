package com.emerson.bpm.nodes.match.sql.generic;

import com.emerson.bpm.nodes.match.sql.QbQuery;

/** 
 * Immutable class to implement a custom query.
 * @author DanFickle
 */
class QbCustomQuery implements QbQuery 
{
	private final String m_sql;
	
	QbCustomQuery(String sql)
	{
		m_sql = sql;
	}
	
	@Override
	public String getQueryString()
	{
		return m_sql;
	}

	@Override
	public int getPlaceholderIndex(String placeholderName)
	{
		throw new IllegalArgumentException("Placeholder doesn't exist");
	}
}