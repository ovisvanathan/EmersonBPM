package com.emerson.bpm.nodes.match.sql;

import java.util.List;

import com.emerson.bpm.sql.QbSQLField;
import com.emerson.bpm.sql.QbSQLWhere;
import com.emerson.bpm.sql.QbWhereOperator;
import com.emerson.bpm.sql.WhereInfo;

/**
 * Generates a WHERE clause. For use in SELECT and UPDATE queries.
 * @author DanFickle
 */
public interface QbWhere extends QbSQLWhere
{
	
	public List<WhereInfo> getWhereInfos();
	public List<Object []> getWherePlaces();
	
	
	default public QbWhere forEach(int x, List<Object []> wlist) {
		QbWhere whr = null;
		int y=0;
		while(y<x) {
			whr = where((QbSQLField) wlist.get(y)[0], (QbWhereOperator) wlist.get(y)[1], wlist.get(y)[2]);
				y++;
		}
		return whr;	
	}

	/**
	 * Constructs a where clause. If there are already where clauses
	 * in the query, its added as a AND WHERE.
	 * @param field
	 * @param placeholder
	 * @return This where builder.
	 */
	public QbWhere where(QbSQLField field, Object placeholder);

	/**
	 * Similar to {@link #where(QbSQLField, String) where} but allows you 
	 * to specify a where operator.
	 * @param field
	 * @param op
	 * @param placeholder
	 * @return This where builder.
	 */
	public QbWhere where(QbSQLField field, QbWhereOperator op, Object placeholder); 

	/**
	 * Similar to {@link #where(QbSQLField, String) where} but allows you 
	 * to specify a where operator.
	 * @param field
	 * @param op
	 * @param placeholder1
	 * @param placeholder2
	 * @return This where builder.
	 */
	public QbWhere where(QbSQLField field, QbWhereOperator op, Object placeholder1, Object placeholder2); 

	/**
	 * Similar to {@link #where(QbSQLField, String) where} but is joined by an OR WHERE.
	 * @param field
	 * @param placeholder
	 * @return This where builder.
	 */
	public QbWhere orWhere(QbSQLField field, Object placeholder); 

	/**
	 * Similar to {@link #orWhere(QbSQLField, String) orWhere} but allows you to 
	 * specify a where operator.
	 * @param field
	 * @param op
	 * @param placeholder
	 * @return This where builder.
	 */
	public QbWhere orWhere(QbSQLField field, QbWhereOperator op, Object placeholder); 

	/**
	 * Similar to {@link #orWhere(QbSQLField, String) orWhere} but allows you to 
	 * specify a where operator.
	 * @param field
	 * @param op
	 * @param placeholder1
	 * @param placeholder2
	 * @return This where builder.
	 */	
	public QbWhere orWhere(QbSQLField field, QbWhereOperator op, Object placeholder1, Object placeholder2);

	/**
	 * Allows you to specify a custom where clause. Not recommended for
	 * use unless an appropriate clause can't be constructed with
	 * other methods.
	 * @param custom
	 * @return This where builder.
	 */
	public QbWhere where(String custom);

	/**
	 * Generates an IN clause.
	 * @param field
	 * @param placeholder - Using this placeholder will return the index of the first placeholder.
	 * @param count - The number of placeholders to place in the query.
	 * @return This where builder.
	 */
	public QbWhere whereIn(QbSQLField field, Object placeholder, int count);

	/**
	 * Similar to {@link #whereIn(QbSQLField, String, int) whereIn} but joined
	 * with an OR.
	 * @param field
	 * @param placeholder
	 * @param count
	 * @return This where builder.
	 */
	public QbWhere orWhereIn(QbSQLField field, Object placeholder, int count);

	/**
	 * Similar to {@link #whereIn(QbSQLField, String, int) whereIn} but generates
	 * a NOT IN clause.
	 * @param field
	 * @param placeholder
	 * @param count
	 * @return This where builder.
	 */
	public QbWhere whereNotIn(QbSQLField field, Object placeholder, int count);

	/**
	 * Similar to {@link #whereNotIn(QbSQLField, String, int) whereIn} but joined
	 * with an OR.
	 * @param field
	 * @param placeholder
	 * @param count
	 * @return This where builder.
	 */
	public QbWhere orWhereNotIn(QbSQLField field, Object placeholder, int count);

	/**
	 * Adds an opening bracket.
	 * @return This where builder.
	 */
	public QbWhere startBracket(); 

	/**
	 * Adds an ending bracket.
	 * @return This where builder.
	 */
	public QbWhere endBracket();
	
	/**
	 * Gets the placeholder index. Usually you don't call this
	 * directly and get the placeholder index from the QbQuery which 
	 * will check its own placeholders as well as those from any where clause.
	 * @param placeholderName
	 * @return The placeholder index.
	 */
	public int getPlaceholderIndex(String placeholderName);
	
	/**
	 * Gets the number of placeholders.
	 */
	public int getPlaceholderCount();
}