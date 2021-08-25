package com.emerson.bpm.sql;

import java.util.List;

public interface QbSQLWhere {

//	QbSQLWhere forEach(int x, List<Object[]> wlist);

//	void where(QbSQLField QbSQLField, QbSQLWhereOperator QbSQLWhere, Object qbvalue);

	public QbSQLWhere where(QbSQLField field, Object placeholder);

	public QbSQLWhere where(QbSQLField field, QbWhereOperator op, Object placeholder);

	public QbSQLWhere orWhere(QbSQLField field, Object placeholder);
	
	public QbSQLWhere orWhere(QbSQLField field, QbWhereOperator op, Object placeholder);

	public QbSQLWhere orWhere(QbSQLField field, QbWhereOperator op, Object placeholder1, Object placeholder2);

	public QbSQLWhere where(String custom);

	public QbSQLWhere whereIn(QbSQLField field, Object placeholder, int count);

	public QbSQLWhere orWhereIn(QbSQLField field, Object placeholder, int count);

	public QbSQLWhere whereNotIn(QbSQLField field, Object placeholder, int count);

	public QbSQLWhere orWhereNotIn(QbSQLField field, Object placeholder, int count);

	public QbSQLWhere startBracket();

	public QbSQLWhere endBracket();

	public int getPlaceholderIndex(String placeholderName);
	
	public int getPlaceholderCount();

	public QbSQLWhere forEach(int x, QbSQLField field, QbWhereOperator oper, Object value);
	

	public QbSQLWhere where(QbSQLField field, QbWhereOperator op, Object placeholder1, Object placeholder2);
	
	public List<WhereInfo> getWhereInfos();

	public List<Object []> getWherePlaces();

}
