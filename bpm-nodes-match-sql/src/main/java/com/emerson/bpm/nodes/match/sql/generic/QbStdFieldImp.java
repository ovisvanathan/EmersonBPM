package com.emerson.bpm.nodes.match.sql.generic;

import com.emerson.bpm.nodes.match.sql.QbField;

/**
 * Immutable class to implement a standard un-qualified field.
 * @author DanFickle
 */
class QbStdFieldImp implements QbField
{
	private final String m_fieldName;

	QbStdFieldImp(String field)
	{
		m_fieldName = field;
	}
	
	@Override
	public String toString() 
	{
		return '`' + m_fieldName + '`';
	}
}