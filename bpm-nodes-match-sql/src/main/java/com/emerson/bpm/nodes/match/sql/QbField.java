package com.emerson.bpm.nodes.match.sql;

import com.emerson.bpm.sql.QbSQLField;

/**
 * A QbField can be used in select and where clauses and represents
 * a database field or an aggregate function on a database field.
 * Use a DB specific Factory to create QbField objects.  
 * @author DanFickle
 */
public interface QbField extends QbSQLField
{
}
