package com.emerson.bpm.sql;

public interface QbSQLQueryData {

	Object[] getTables();

	String[] getFieldNames();

	QbSQLWhere getWhere();

}
