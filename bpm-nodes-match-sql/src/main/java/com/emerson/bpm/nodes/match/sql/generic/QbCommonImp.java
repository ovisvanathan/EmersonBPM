package com.emerson.bpm.nodes.match.sql.generic;

import java.util.List;

import com.emerson.bpm.nodes.match.sql.QbField;
import com.emerson.bpm.sql.QbSQLField;


/**
 * Static functions that are used by other implementation classes.
 * @author DanFickle
 */
class QbCommonImp
{
	static String protectTableName(String table)
	{
		return '`' + table + '`';
	}
	
	static void joinFieldNames(StringBuilder builder, QbSQLField[] fields)
	{
		for (int i = 0; i < fields.length; i++)
		{
			builder.append(fields[i].toString());
			
			if (i != fields.length - 1)
				builder.append(", ");
		}
	}
	
	static void joinFieldNames(StringBuilder builder, List<QbField> fields)
	{
		for (int i = 0; i < fields.size(); i++)
		{
			builder.append(fields.get(i).toString());
			
			if (i != fields.size() - 1)
				builder.append(", ");
		}
	}
	
	static void createPlaceholders(StringBuilder builder, int cnt)
	{
		for (int i = 0; i < cnt; i++)
		{
			builder.append('?');
			
			if (i != cnt - 1)
				builder.append(", ");
		}
	}
}