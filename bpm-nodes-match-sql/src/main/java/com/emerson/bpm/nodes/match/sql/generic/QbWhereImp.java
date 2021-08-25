package com.emerson.bpm.nodes.match.sql.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.sql.QbSQLField;
import com.emerson.bpm.sql.QbWhereOperator;
import com.emerson.bpm.sql.WhereInfo;

public class QbWhereImp implements QbWhere
{
	private boolean m_having;
	QbWhereImp(boolean having, int placeholderOffset) 
	{
		m_having = having;
		m_placeholderCnt = placeholderOffset;
	}


	private List<WhereInfo> m_whereInfo = new ArrayList<WhereInfo>(4);
	private Map<Object [], Integer> m_placeholders = new HashMap<Object [], Integer>();
	private int m_placeholderCnt = 1;

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(m_having ? " HAVING " : " WHERE ");
		
		int fieldCnt = 0;
		for (WhereInfo whereInfo : m_whereInfo)
		{
			if (whereInfo.m_startBracket)
			{
				builder.append('(');
				continue;
			}
			
			if (whereInfo.m_endBracket)
			{
				builder.append(')');
				continue;
			}

			if (whereInfo.m_custom != null)
			{
				builder.append(whereInfo.m_custom);
				fieldCnt++;
				continue;
			}
			
			if (fieldCnt != 0)
			{
				builder.append(whereInfo.m_orClause ? " OR " : " AND ");
			}
			fieldCnt++;
			
			builder.append(whereInfo.m_field.toString());
			
			if (whereInfo.m_inCount != 0)
			{
				builder.append(whereInfo.m_notIn ? " NOT IN (" : " IN (");
				QbCommonImp.createPlaceholders(builder, whereInfo.m_inCount);
				builder.append(')');
				continue;
			} 			
			builder.append(' ');
						
			if(whereInfo.m_op.toString().equals(QbWhereOperator.BETWEEN.toString())) {
				builder.append(whereInfo.m_op.toString());
				builder.append(" ?");
				builder.append(" AND ?");				
			} else {
				builder.append(whereInfo.m_op.toString());
				builder.append(" ?");
			}
		}
		
		return builder.toString();
	}
	
	
	
	@Override
	public QbWhere where(QbSQLField field, Object placeholder)
	{
		WhereInfo whereInfo = new WhereInfo(field, QbWhereOperator.EQUALS);
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder}, m_placeholderCnt);
		m_placeholderCnt++;
		return this;
	}

	@Override
	public QbWhere where(QbSQLField field, QbWhereOperator op, Object placeholder)
	{
		WhereInfo whereInfo = new WhereInfo(field, op);
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder}, m_placeholderCnt);
		m_placeholderCnt++;
		return this;
	}

	@Override
	public QbWhere orWhere(QbSQLField field, Object placeholder)
	{
		WhereInfo whereInfo = new WhereInfo(field, QbWhereOperator.EQUALS);
		whereInfo.m_orClause = true;
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder}, m_placeholderCnt);
		m_placeholderCnt++;
		return this;
	}

	@Override
	public QbWhere orWhere(QbSQLField field, QbWhereOperator op, Object placeholder)
	{
		WhereInfo whereInfo = new WhereInfo(field, op);
		whereInfo.m_orClause = true;
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder }, m_placeholderCnt);
		m_placeholderCnt++;
		return this;
	}

	@Override
	public QbWhere orWhere(QbSQLField field, QbWhereOperator op, Object placeholder1, Object placeholder2)
	{
		WhereInfo whereInfo = new WhereInfo(field, op);
		whereInfo.m_orClause = true;
		m_whereInfo.add(whereInfo);
		m_placeholderCnt++;
		m_placeholderCnt++;		
		m_placeholders.put(new Object [] { placeholder1, placeholder2}, m_placeholderCnt);
		return this;
	}
	@Override
	public QbWhere where(String custom)
	{
		WhereInfo whereInfo = new WhereInfo();
		whereInfo.m_custom = custom;
		m_whereInfo.add(whereInfo);
		return this;
	}

	@Override
	public QbWhere whereIn(QbSQLField field, Object placeholder, int count)
	{
		WhereInfo whereInfo = new WhereInfo();
		whereInfo.m_field = field;
		whereInfo.m_inCount = count;
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder }, m_placeholderCnt);
		m_placeholderCnt += count;
		return this;
	}

	@Override
	public QbWhere orWhereIn(QbSQLField field, Object placeholder, int count)
	{
		WhereInfo whereInfo = new WhereInfo();
		whereInfo.m_field = field;
		whereInfo.m_inCount = count;
		whereInfo.m_orClause = true;
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder }, m_placeholderCnt);
		m_placeholderCnt += count;
		return this;
	}

	@Override
	public QbWhere whereNotIn(QbSQLField field, Object placeholder, int count)
	{
		WhereInfo whereInfo = new WhereInfo();
		whereInfo.m_field = field;
		whereInfo.m_inCount = count;
		whereInfo.m_notIn = true;
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder }, m_placeholderCnt);
		m_placeholderCnt += count;
		return this;
	}

	@Override
	public QbWhere orWhereNotIn(QbSQLField field, Object placeholder, int count)
	{
		WhereInfo whereInfo = new WhereInfo();
		whereInfo.m_field = field;
		whereInfo.m_inCount = count;
		whereInfo.m_notIn = true;
		whereInfo.m_orClause = true;
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder }, m_placeholderCnt);
		m_placeholderCnt += count;
		return this;
	}

	@Override
	public QbWhere startBracket()
	{
		WhereInfo whereInfo = new WhereInfo();
		whereInfo.m_startBracket = true;
		m_whereInfo.add(whereInfo);
		return this;
	}

	@Override
	public QbWhere endBracket() 
	{
		WhereInfo whereInfo = new WhereInfo();
		whereInfo.m_endBracket = true;
		m_whereInfo.add(whereInfo);
		return this;
	}

	@Override
	public int getPlaceholderIndex(String placeholderName)
	{
		Integer idx = m_placeholders.get(placeholderName);
		
		if (idx == null)
			return 0;
		else
			return idx;
	}
	
	@Override
	public int getPlaceholderCount()
	{
		return m_placeholders.size();
	}


	public QbWhere forEach(int x, QbSQLField field, QbWhereOperator oper, Object value) {
		return where(field, oper, value);
	}

	@Override
	public QbWhere where(QbSQLField field, QbWhereOperator op, Object placeholder1, Object placeholder2) {
		WhereInfo whereInfo = new WhereInfo(field, op);
		whereInfo.m_orClause = false;
		m_whereInfo.add(whereInfo);
		m_placeholders.put(new Object [] { placeholder1 }, m_placeholderCnt);
		m_placeholderCnt++;
		m_placeholders.put(new Object [] { placeholder2 }, m_placeholderCnt);
		m_placeholderCnt++;
		return this;
	 }
	
	
	
	public List<WhereInfo> getWhereInfos() {
		return this.m_whereInfo;
	}

	public List<Object []> getWherePlaces() {
		Set<Object []> set = m_placeholders.keySet();
		return new ArrayList(set);
	}

	
	
}