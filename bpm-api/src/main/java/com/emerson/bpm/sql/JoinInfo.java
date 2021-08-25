package com.emerson.bpm.sql;


public class JoinInfo
{
	public final QbSQLField leftSide;
	public final QbSQLField rightSide;
	public final QbJoinType joinType;
	public final String table;

	public JoinInfo(QbSQLField left, QbSQLField right, String tabl, QbJoinType type)
	{
		leftSide = left;
		rightSide = right;
		joinType = type;
		table = tabl;
	}

	public String getLeftField() {
		return this.leftSide.toString();
	}

	public String getRightField() {
		return this.rightSide.toString();
	}
	
	public String getJoinType() {
		return QbUtils.joinTypeToString(joinType);
	}
	
	public String getJoinTable() {
		return this.table;
	}
	
		

}

