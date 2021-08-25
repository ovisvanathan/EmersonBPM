package com.emerson.bpm.sql;

import java.util.List;

public interface QbSQLSelect {

	
	public String [] getFieldNames();

	public String getSQLTable();
	
	public QbSQLWhere getWhere();

	public List<JoinInfo>  getJoinList();
	
	public String []  getJoinFields();
	
	public QbSQLField [] getOrderByFields();
	
	public String getOrderByOrder();
	
	public String [] getJoinTables();
	
	public Object [] getTableObjects();
	
	public QbSQLSelect select(QbSQLField...fields);

	public QbSQLSelect from(String table);
	
	public QbSQLSelect  fromTableObject(String table, Object tableObj);
	
	public QbSQLSelect from(String table, String alias);

	public QbSQLSelect  fromTableObject(String table, String alias, Object tableObj);

	public QbSQLWhere where();
	
	public QbSQLSelect distinct();
	
	public QbSQLSelect join(String table, QbSQLField field1, QbSQLField field2, QbJoinType joinType);

	public QbSQLSelect join(String table, String tableAlias, QbSQLField field1, QbSQLField field2, QbJoinType joinType);

	public QbSQLSelect join(String table, String tableAlias,  Object tableObj, QbSQLField field1, QbSQLField field2, QbJoinType joinType);

	
	public QbSQLSelect join(String table, QbSQLField field1, QbSQLField field2);

	public QbSQLSelect join(String table, String tableAlias, QbSQLField field1, QbSQLField field2);

	public QbSQLSelect join(String table, String tableAlias, Object tableObj, QbSQLField field1, QbSQLField field2);

	public QbSQLSelect groupBy(QbSQLField... fields);
	
	public QbSQLWhere having();
	
	public enum QbOrderBy
	{
		ASC,
		DESC
	}
	
	public QbSQLSelect orderBy(QbOrderBy order, QbSQLField... fields);
	
	public QbSQLSelect limit(int offset, int count);

	
}
