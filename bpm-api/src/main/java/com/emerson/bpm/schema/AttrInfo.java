package com.emerson.bpm.schema;

/*
 * Created on Jul 27, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author omprakash.v
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AttrInfo {

	String attrName;
	String consVal;
	int consType;
	String attrNS;
	
	/**
	 * @return Returns the attrName.
	 */
	public String getAttrName() {
		return attrName;
	}
	/**
	 * @param attrName The attrName to set.
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	/**
	 * @return Returns the attrNS.
	 */
	public String getAttrNS() {
		return attrNS;
	}
	/**
	 * @param attrNS The attrNS to set.
	 */
	public void setAttrNS(String attrNS) {
		this.attrNS = attrNS;
	}
	/**
	 * @return Returns the consType.
	 */
	public int getConsType() {
		return consType;
	}
	/**
	 * @param consType The consType to set.
	 */
	public void setConsType(int consType) {
		this.consType = consType;
	}
	/**
	 * @return Returns the consVal.
	 */
	public String getConsVal() {
		return consVal;
	}
	/**
	 * @param consVal The consVal to set.
	 */
	public void setConsVal(String consVal) {
		this.consVal = consVal;
	}
}