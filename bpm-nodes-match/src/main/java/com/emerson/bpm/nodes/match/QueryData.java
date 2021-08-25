package com.emerson.bpm.nodes.match;

import java.util.List;

import com.emerson.bpm.nodes.match.sql.QbWhere;
import com.emerson.bpm.sql.JoinInfo;
import com.emerson.bpm.sql.QbSQLQueryData;

/* A JavaBean for storing rule query data */
public class QueryData implements QbSQLQueryData {

	Object [] tables;
	
	String [] tableNames;
	
	String [] fieldNames;
	
	String [] joinFields;
	
	
	boolean multiJoin;
	
	List<JoinInfo> joinList;
	
	public boolean isMultiJoin() {
		return multiJoin;
	}

	public void setMultiJoin(boolean multiJoin) {
		this.multiJoin = multiJoin;
	}

	public List<JoinInfo> getJoinList() {
		return joinList;
	}

	public void setJoinList(List<JoinInfo> joinList) {
		this.joinList = joinList;
	}

	QbWhere where;

	public Object[] getTables() {
		return tables;
	}

	public void setTables(Object[] tables) {
		this.tables = tables;
	}

	public String[] getTableNames() {
		return tableNames;
	}

	public void setTableNames(String[] tableNames) {
		this.tableNames = tableNames;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public String[] getJoinFields() {
		return joinFields;
	}

	public void setJoinFields(String[] joinFields) {
		this.joinFields = joinFields;
	}

	public QbWhere getWhere() {
		return where;
	}

	public void setWhere(QbWhere where) {
		this.where = where;
	}
	
	
}
