package com.emerson.bpm.dsl.record;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.emerson.bpm.dsl.AbstractRecord;

public class AbstractDSLRecord extends AbstractRecord {

	public static DefaultTableModel buildRecord(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	public static DefaultTableModel buildRecord(Object [] columnNames, Object [][] datavec)
	        throws Exception {
	
	    return new DefaultTableModel(datavec, columnNames);	
	
	}

	public AbstractRecord buildRecord(String [] columnNames, Object dataobj)
	        throws Exception {
		
		List x = buildRecord0(columnNames, new Object [] { dataobj });
	
		if(x != null)
			return (AbstractRecord) x.get(0);
	
		return null;
	}

	
	public List<AbstractRecord> buildRecord0(String [] columnNames, Object [] dataobj)
	        throws Exception {
	
		List reclist = new ArrayList();
  		Vector records = new Vector();

		  try {

			  	Method [] mks = null;
			  		for(Object j : dataobj) {
			  			
			  			Vector rowdata = new Vector();
			  			if(mks == null) {
			  				mks = j.getClass().getMethods();
			  			}
			  				
			  			for(int k=0;k<mks.length;k++) {
			  				Method m = mks[k];
			  				String mName = m.getName();
			  				for(int p=0;p<columnNames.length;p++) {
			  					
			  					if(mName.startsWith("get")) {
			  						
			  						if(mName.substring(3).equalsIgnoreCase(columnNames[p])) {
			  					
			  							Object val = m.invoke(dataobj, null);
			  							rowdata.add(val);
			  						}
			  					}
			  				}

			  				records.add(rowdata);
				  							  				
			  			}

			  			AccountRecord rec = new AccountRecord(records, columnNames);	
			  			reclist.add(rec);		  

			  		}
			  		
			  			return reclist;	
			  								
		  	  } catch (Exception e) {
			    // and this, too
			  }
			  			
		
		  return null;	


	}

	public Vector<Map> buildRecordsMap(String [] columnNames, Object [] dataobj)
	        throws Exception {
	
  		Vector records = new Vector();

		  try {

			  	Method [] mks = null;
			  		for(Object j : dataobj) {
			  			
			  			Map<String, Object> rowdata = new HashMap();
			  			if(mks == null) {
			  				mks = j.getClass().getMethods();
			  			}
			  				
			  			for(int k=0;k<mks.length;k++) {
			  				Method m = mks[k];
			  				String mName = m.getName();
			  				for(int p=0;p<columnNames.length;p++) {
			  					
			  					if(mName.startsWith("get")) {
			  						String colName = columnNames[p];
			  						if(mName.substring(3).equalsIgnoreCase(columnNames[p])) {
			  					
			  							Object val = m.invoke(dataobj, null);
			  							rowdata.put(colName, val);
			  						}
			  					}
			  				}

			  								  				
			  			}
	
			  				records.add(rowdata);
			  		}
			  		

			  } catch (Exception e) {
				    // and this, too
			  }

			return records;	

		}

}
