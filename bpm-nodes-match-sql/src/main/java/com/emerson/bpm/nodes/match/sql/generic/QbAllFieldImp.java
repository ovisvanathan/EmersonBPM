package com.emerson.bpm.nodes.match.sql.generic;

import com.emerson.bpm.nodes.match.sql.QbField;

/**
 * Immutable class to implement all fields.
 * @author DanFickle
 */
class QbAllFieldImp implements QbField
{
	QbAllFieldImp() { }
	
	@Override
	public String toString()
	{
		return "*";
	}
}
