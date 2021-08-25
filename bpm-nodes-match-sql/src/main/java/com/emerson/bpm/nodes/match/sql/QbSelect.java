package com.emerson.bpm.nodes.match.sql;

import java.util.List;

import com.emerson.bpm.sql.JoinInfo;
import com.emerson.bpm.sql.QbJoinType;
import com.emerson.bpm.sql.QbSQLField;
import com.emerson.bpm.sql.QbSQLSelect;

/**
 * Interface to generate a SELECT query.
 * @author DanFickle
 */
public interface QbSelect extends QbQuery, QbSQLSelect
{
	public String [] getFieldNames();

	public String getSQLTable();
	
	public QbWhere getWhere();

	public List<JoinInfo>  getJoinList();
	
	public String []  getJoinFields();
	
	public QbSQLField [] getOrderByFields();
	
	public String getOrderByOrder();
	
	public String [] getJoinTables();
	
	public Object [] getTableObjects();
	

	/**
	 * Sets the list of fields to select from. Call only once per
	 * QbSelect object.
	 * @param fields
	 * @return This query object.
	 */
	public QbSelect select(QbSQLField...fields);

	/**
	 * Which table to get records from? Call only once per
	 * QbSelect object. 
	 * @param table
	 * @return This query object.
	 */
	public QbSelect from(String table);
	
	/**
	 * Which table to get records from? Call only once per
	 * QbSelect object. For JDO support
	 * @param table
	 * @return This query object.
	 */	
	public QbSelect  fromTableObject(String table, Object tableObj);
	
	/**
	 * Which table to get records from? Call only once per
	 * QbSelect object. 
	 * @param table
	 * @return This query object.
	 */
	public QbSelect from(String table, String alias);

	/**
	 * Which table to get records from? Call only once per
	 * QbSelect object. For JDO support
	 * @param table
	 * @return This query object.
	 */	
	public QbSelect  fromTableObject(String table, String alias, Object tableObj);

	/**
	 * Creates the where clause builder. This must be called before any
	 * call to having(). Call only once per QbSelect object.
	 * @return A mutable QbWhere that is bound to this query.
	 */
	public QbWhere where();
	
	/**
	 * Adds the DISTINCT keyword.
	 * @return This query object.
	 */
	public QbSelect distinct();
	

	/**
	 * Join the table to this query. May be called multiple times.
	 * @param table - A table name without backticks.
	 * @param field1 - A field to join on.
	 * @param field2 - The second field to join on.
	 * @param joinType - Join type.
	 * @return This query object.
	 */
	public QbSelect join(String table, QbSQLField field1, QbSQLField field2, QbJoinType joinType);

	/**
	 * Join the table to this query. May be called multiple times.
	 * @param table - A table name without backticks.
	 * @param tableAlias - An alias for  table name without backticks.
	 * @param field1 - A field to join on.
	 * @param field2 - The second field to join on.
	 * @param joinType - Join type.
	 * @return This query object.
	 */
	public QbSelect join(String table, String tableAlias, QbSQLField field1, QbSQLField field2, QbJoinType joinType);

	/**
	 * Join the table to this query. May be called multiple times.
	 * @param table - A table name without backticks.
	 * @param tableAlias - An alias for  table name without backticks.
	 * @param field1 - A field to join on.
	 * @param field2 - The second field to join on.
	 * @param joinType - Join type.
	 * @return This query object.
	 */
	public QbSelect join(String table, String tableAlias,  Object tableObj, QbSQLField field1, QbSQLField field2, QbJoinType joinType);

	
	/**
	 * Similar to {@link #join(String, QbSQLField, QbSQLField, QbJoinType) join}
	 * but uses the db's default join type. May be called multiple times.
	 * @param table - A table name without backticks.
	 * @param field1 - A field to join on.
	 * @param field2 - The second field to join on.
	 * @return This query object.
	 */
	public QbSelect join(String table, QbSQLField field1, QbSQLField field2);

	/**
	 * Similar to {@link #join(String, QbSQLField, QbSQLField, QbJoinType) join}
	 * but uses the db's default join type. May be called multiple times.
	 * @param table - A table name without backticks.
	 * @param tableAlias - An alias for table name without backticks.
	 * @param field1 - A field to join on.
	 * @param field2 - The second field to join on.
	 * @return This query object.
	 */
	public QbSelect join(String table, String tableAlias, QbSQLField field1, QbSQLField field2);

	/**
	 * Similar to {@link #join(String, QbSQLField, QbSQLField, QbJoinType) join}
	 * but uses the db's default join type. May be called multiple times.
	 * @param table - A table name without backticks.
	 * @param tableAlias - An alias for table name without backticks.
	 * @param field1 - A field to join on.
	 * @param field2 - The second field to join on.
	 * @return This query object.
	 */
	public QbSelect join(String table, String tableAlias, Object tableObj, QbSQLField field1, QbSQLField field2);

	/**
	 * Takes a list of fields to group by. Call only once per
	 * QbSelect object.
	 * @param fields - The fields to group by.
	 * @return This query object.
	 */
	public QbSelect groupBy(QbSQLField... fields);
	
	/**
	 * The having clause for use with group_by. This MUST be called after any
	 * call to where(). Call only once per QbSelect object.
	 * @return A QbWhere object bound to the having clause of this query.
	 */
	public QbWhere having();
	
	/**
	 * Order ascending or descending. Use this rather than a boolean
	 * to make code more readable.
	 */
	public enum QbOrderBy
	{
		ASC,
		DESC
	}
	
	/**
	 * Used to make the ORDER BY clause. Call only once
	 * per QbSelect object.
	 * @param order - ASC or DESC.
	 * @param fields - A list of fields to order by.
	 * @return This query object.
	 */
	public QbSelect orderBy(QbSQLSelect.QbOrderBy order, QbSQLField... fields);
	
	/**
	 * Allows the provision of an offset and limit.
	 * @param offset - The record offset, starting at zero.
	 * @param count - The number of records to return.
	 * @return This query object.
	 */
	public QbSelect limit(int offset, int count);
}
