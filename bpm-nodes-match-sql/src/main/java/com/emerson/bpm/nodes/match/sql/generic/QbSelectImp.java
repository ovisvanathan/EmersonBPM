package com.emerson.bpm.nodes.match.sql.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.emerson.bpm.nodes.match.sql.QbSelect;
import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.sql.JoinInfo;
import com.emerson.bpm.sql.QbJoinType;
import com.emerson.bpm.sql.QbSQLField;
import com.emerson.bpm.sql.QbSQLSelect;

public class QbSelectImp implements QbSelect
{
	private int m_offset;
	private int m_limit;
	private boolean m_haveLimit;
	private QbSQLField[] m_orderBy;
	private QbSQLSelect.QbOrderBy m_orderByOrder;
	private QbWhere m_havingClause;
	private QbWhere m_where;
	private QbSQLField[] m_groupBy;

	QbSelectImp() { }
	
	
	
	private List<JoinInfo> m_joinList;
	private QbSQLField[] m_selectFields;
	private boolean m_distinct;
	private String m_table;
	private Map<String, String> m_tableAliases;
	private Object m_tableObj;
	private List m_tableList;

	
	private String joinTypeToString(QbJoinType joinType)
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
	
	
	@Override
	public String getQueryString()
	{
		StringBuilder builder = new StringBuilder("SELECT ");
		
		if (m_table == null)
			throw new IllegalStateException("Must specify table");
		
		if (m_selectFields == null)
			throw new IllegalStateException("Must specify some fields");
		
		if (m_distinct)
			builder.append("DISTINCT ");
		
		QbCommonImp.joinFieldNames(builder, m_selectFields);
		builder.append(" FROM ");
		builder.append(QbCommonImp.protectTableName(m_table));
		builder.append(' ');
		
	
		if (m_joinList != null)
		{
			for (JoinInfo join : m_joinList)
			{
				builder.append(joinTypeToString(join.joinType));
				builder.append(" JOIN ");
				builder.append(QbCommonImp.protectTableName(join.table));
				builder.append(" ON ");
				builder.append(join.leftSide.toString());
				builder.append(" = ");
				builder.append(join.rightSide.toString());
			}
		}

		if (m_where != null)
			builder.append(m_where.toString());
		
		if (m_groupBy != null)
		{
			builder.append(" GROUP BY ");
			QbCommonImp.joinFieldNames(builder, m_groupBy);
		}

		if (m_havingClause != null)
			builder.append(m_havingClause.toString());
		
		if (m_orderBy != null)
		{
			builder.append(" ORDER BY ");
			QbCommonImp.joinFieldNames(builder, m_orderBy);
			builder.append(' ');
			builder.append(m_orderByOrder.toString());
		}
		
		if (m_haveLimit)
		{
			builder.append(" LIMIT ");
			builder.append(m_offset);
			builder.append(", ");
			builder.append(m_limit);
		}
			
		return builder.toString();
	}

	@Override
	public int getPlaceholderIndex(String placeholderName)
	{
		int idx = 0;
		if (m_havingClause != null)
			idx = m_havingClause.getPlaceholderIndex(placeholderName);
		
		if (idx == 0 && m_where != null)
			idx = m_where.getPlaceholderIndex(placeholderName);
		
		if (idx == 0)
			throw new IllegalArgumentException("Placeholder doesn't exist");
		
		return idx;
	}

	@Override
	public QbSelect select(QbSQLField... fields)
	{
		m_selectFields = fields;
		return this;
	}

	@Override
	public QbSelect from(String table)
	{
		m_table = table;
		return this;
	}

	public QbSelect from(String table, String alias) {
		m_table = table;
		if(m_tableAliases == null)
			m_tableAliases = new HashMap();		
		m_tableAliases.put(alias, table);
		return this;
	}

	public QbSelect  fromTableObject(String table, Object tableObj) {		
		m_table = table;

		if(this.m_tableList == null)
			this.m_tableList = new LinkedList();
		
		this.m_tableList.add(tableObj);
		return this;		
	}

	public QbSelect  fromTableObject(String table, String alias, Object tableObj) {		
		m_table = table;
		if(this.m_tableList == null)
			this.m_tableList = new LinkedList();
		if(m_tableAliases == null)
			m_tableAliases = new HashMap();		
		
		this.m_tableList.add(tableObj);
		return this;		
	}
	
	@Override
	public QbWhere where()
	{
		m_where = new QbWhereImp(false, 1);
		return m_where;
	}
	
	@Override
	public QbSelect distinct()
	{
		m_distinct = true;
		return this;
	}

	@Override
	public QbSelect join(String table, QbSQLField field1, QbSQLField field2,
			QbJoinType joinType) 
	{
		if (m_joinList == null)
			m_joinList = new ArrayList<JoinInfo>(2);
		
		m_joinList.add(new JoinInfo(field1, field2, table, joinType));
		return this;
	}

	@Override
	public QbSelect join(String table, String tableAlias, QbSQLField field1, QbSQLField field2,
			QbJoinType joinType)
	{
		if (m_joinList == null)
			m_joinList = new ArrayList<JoinInfo>(2);
		
		m_joinList.add(new JoinInfo(field1, field2, table, joinType));
		this.m_tableAliases.put(tableAlias, table);		
		return this;
	}

	public QbSelect join(String table, String tableAlias,  Object tableObj, QbSQLField field1, QbSQLField field2, QbJoinType joinType) {
		if (m_joinList == null)
			m_joinList = new ArrayList<JoinInfo>(2);
		
		m_joinList.add(new JoinInfo(field1, field2, table, joinType));
		this.m_tableAliases.put(tableAlias, table);		
		this.m_tableList.add(tableObj);
		return this;
	}

	@Override
	public QbSelect join(String table, QbSQLField field1, QbSQLField field2)
	{
		if (m_joinList == null)
			m_joinList = new ArrayList<JoinInfo>(2);
		
		m_joinList.add(new JoinInfo(field1, field2, table, QbJoinType.DEFAULT));
		return this;
	}

	@Override
	public QbSelect join(String table, String tableAlias, QbSQLField field1, QbSQLField field2)
	{
		if (m_joinList == null)
			m_joinList = new ArrayList<JoinInfo>(2);
		
		m_joinList.add(new JoinInfo(field1, field2, table, QbJoinType.DEFAULT));
		this.m_tableAliases.put(tableAlias, table);
		return this;
	}

	public QbSelect join(String table, String tableAlias,  Object tableObj, QbSQLField field1, QbSQLField field2) {
		if (m_joinList == null)
			m_joinList = new ArrayList<JoinInfo>(2);
		
		m_joinList.add(new JoinInfo(field1, field2, table, QbJoinType.DEFAULT));
		this.m_tableAliases.put(tableAlias, table);		
		this.m_tableList.add(tableObj);
		return this;
	}

	@Override
	public QbSelect groupBy(QbSQLField... fields)
	{
		m_groupBy = fields;
		return this;
	}

	@Override
	public QbWhere having()
	{
		m_havingClause = new QbWhereImp(true, m_where == null ? 1 : m_where.getPlaceholderCount() + 1);
		return m_havingClause;
	}

	@Override
	public QbSelect orderBy(QbSQLSelect.QbOrderBy order, QbSQLField... fields)
	{
		m_orderBy = fields;
		m_orderByOrder = order;
		return this;
	}

	@Override
	public QbSelect limit(int offset, int count)
	{
		m_offset = offset;
		m_limit = count;
		m_haveLimit = true;
		return this;
	}
	
	@Override	
	public String [] getFieldNames() {	
		int  k = 0;
		String [] s = new String[this.m_selectFields.length];
		for(Object o : this.m_selectFields)
			s[k++] = o.toString();
		
		return s;
	}

	@Override
	public String getSQLTable() {
		return m_table;
	}
	
	@Override
	public QbWhere getWhere() {
		return m_where;
	}

	@Override
	public List<JoinInfo> getJoinList() {
		return m_joinList;
	}

	@Override
	public QbSQLField [] getOrderByFields() {
		return m_orderBy;
		
	}

	@Override
	public String getOrderByOrder() {
		return m_orderByOrder.name();
	}

	@Override
	public String [] getJoinTables() {		
		String table1 = getSQLTable();		
		List<JoinInfo> jlist = getJoinList();		
		JoinInfo info = jlist.get(0);		
		String table2 = info.getJoinTable();
		String [] x = new String [] { table1, table2 };		
		return x;			
	}
	
	@Override
	public String []  getJoinFields() {		
		List<JoinInfo> jlist = getJoinList();		
		JoinInfo info = jlist.get(0);
		return new String [] { info.getLeftField(), info.getRightField() };		
	}
	
	@Override
	public Object [] getTableObjects() {		
		return this.m_tableList.toArray(new Object [] {});		
	}

	
}


