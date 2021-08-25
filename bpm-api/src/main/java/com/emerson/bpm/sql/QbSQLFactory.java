package com.emerson.bpm.sql;

public interface QbSQLFactory {

	QbSQLField newStdField(String string);

	QbSQLSelect newSelectQuery();

	QbSQLField newQualifiedField(String string, String string2);

}
