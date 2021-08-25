package com.emerson.bpm.nodes.match.sql.generic;

import com.emerson.bpm.nodes.match.sql.QbField;

/**
 * Immutable class to implement all fields for a selected table.
 * @author DanFickle
 */
class QbAllTableFieldImp implements QbField
{
	private final String m_table;
	
	QbAllTableFieldImp(String table)
	{
		m_table = table;
	}
	
	@Override
	public String toString() 
	{
		return QbCommonImp.protectTableName(m_table) + ".*";
	}
}