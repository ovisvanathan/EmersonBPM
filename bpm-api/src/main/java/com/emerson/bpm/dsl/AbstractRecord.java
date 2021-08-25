package com.emerson.bpm.dsl;

import java.io.Serializable;
import java.util.Vector;

import javax.swing.event.EventListenerList;


/**
 *  This abstract class provides default implementations for most of
 *  the methods in the <code>TableModel</code> interface. It takes care of
 *  the management of listeners and provides some conveniences for generating
 *  <code>TableModelEvents</code> and dispatching them to the listeners.
 *  To create a concrete <code>TableModel</code> as a subclass of
 *  <code>AbstractTableModel</code> you need only provide implementations
 *  for the following three methods:
 *
 *  <pre>
 *  public int getRowCount();
 *  public int getColumnCount();
 *  public Object getValueAt(int row, int column);
 *  </pre>
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans&trade;
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author Alan Chung
 * @author Philip Milne
 */
public abstract class AbstractRecord implements Record, Serializable
{
//
// Instance Variables
//
	 /**
     * The <code>Vector</code> of <code>Vectors</code> of
     * <code>Object</code> values.
     */
    protected Vector    dataVector;

    /** The <code>Vector</code> of column identifiers. */
    protected Vector    columnIdentifiers;
    /** List of listeners */
    protected EventListenerList listenerList = new EventListenerList();

//
// Default Implementation of the Interface
//
    public AbstractRecord() {
        this(0, 0);
    }

    private static Vector newVector(int size) {
        Vector v = new Vector(size);
        v.setSize(size);
        return v;
    }

    /**
     *  Constructs a <code>AbstractRecord</code> with
     *  <code>rowCount</code> and <code>columnCount</code> of
     *  <code>null</code> object values.
     *
     * @param rowCount           the number of rows the table holds
     * @param columnCount        the number of columns the table holds
     *
     * @see #setValueAt
     */
    public AbstractRecord(int rowCount, int columnCount) {
        this(newVector(columnCount), rowCount);
    }

    /**
     *  Constructs a <code>AbstractRecord</code> with as many columns
     *  as there are elements in <code>columnNames</code>
     *  and <code>rowCount</code> of <code>null</code>
     *  object values.  Each column's name will be taken from
     *  the <code>columnNames</code> vector.
     *
     * @param columnNames       <code>vector</code> containing the names
     *                          of the new columns; if this is
     *                          <code>null</code> then the model has no columns
     * @param rowCount           the number of rows the table holds
     * @see #setDataVector
     * @see #setValueAt
     */
    public AbstractRecord(Vector columnNames, int rowCount) {
        setDataVector(newVector(rowCount), columnNames);
    }

    /**
     *  Constructs a <code>AbstractRecord</code> with as many
     *  columns as there are elements in <code>columnNames</code>
     *  and <code>rowCount</code> of <code>null</code>
     *  object values.  Each column's name will be taken from
     *  the <code>columnNames</code> array.
     *
     * @param columnNames       <code>array</code> containing the names
     *                          of the new columns; if this is
     *                          <code>null</code> then the model has no columns
     * @param rowCount           the number of rows the table holds
     * @see #setDataVector
     * @see #setValueAt
     */
    public AbstractRecord(Object[] columnNames, int rowCount) {
        this(convertToVector(columnNames), rowCount);
    }

    /**
     *  Constructs a <code>AbstractRecord</code> and initializes the table
     *  by passing <code>data</code> and <code>columnNames</code>
     *  to the <code>setDataVector</code> method.
     *
     * @param data              the data of the table, a <code>Vector</code>
     *                          of <code>Vector</code>s of <code>Object</code>
     *                          values
     * @param columnNames       <code>vector</code> containing the names
     *                          of the new columns
     * @see #getDataVector
     * @see #setDataVector
     */
    public AbstractRecord(Vector data, Vector columnNames) {
        setDataVector(data, columnNames);
    }

    /**
     *  Constructs a <code>AbstractRecord</code> and initializes the table
     *  by passing <code>data</code> and <code>columnNames</code>
     *  to the <code>setDataVector</code>
     *  method. The first index in the <code>Object[][]</code> array is
     *  the row index and the second is the column index.
     *
     * @param data              the data of the table
     * @param columnNames       the names of the columns
     * @see #getDataVector
     * @see #setDataVector
     */
    public AbstractRecord(Object[][] data, Object[] columnNames) {
        setDataVector(data, columnNames);
    }

    /**
     *  Returns the <code>Vector</code> of <code>Vectors</code>
     *  that contains the table's
     *  data values.  The vectors contained in the outer vector are
     *  each a single row of values.  In other words, to get to the cell
     *  at row 1, column 5: <p>
     *
     *  <code>((Vector)getDataVector().elementAt(1)).elementAt(5);</code>
     *
     * @return  the vector of vectors containing the tables data values
     *
     * @see #newDataAvailable
     * @see #newRowsAdded
     * @see #setDataVector
     */
    public Vector getDataVector() {
        return dataVector;
    }

    private static Vector nonNullVector(Vector v) {
        return (v != null) ? v : new Vector();
    }
    
    /*
     * @param   dataVector         the new data vector
     * @param   columnIdentifiers     the names of the columns
     * @see #getDataVector
     */
    public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
        this.dataVector = nonNullVector(dataVector);
        this.columnIdentifiers = nonNullVector(columnIdentifiers);
    }
     
    
    /*
     * @param dataVector                the new data vector
     * @param columnIdentifiers the names of the columns
     * @see #setDataVector(Vector, Vector)
     */
    public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
        setDataVector(convertToVector(dataVector), convertToVector(columnIdentifiers));
    }
     
    /* 
     * @param   rowCount   the new number of rows
     * @see #setRowCount
     */
    public void setNumRows(int rowCount) {
        int old = getRowCount();
        if (old == rowCount) {
            return;
        }
        dataVector.setSize(rowCount);
        if (rowCount <= old) {
        }
    }
     
    
   /*
    *  @see #setColumnCount
    * @since 1.3
    */
   public void setRowCount(int rowCount) {
       setNumRows(rowCount);
   }
   
  /*
   * @param   rowData          optional data of the row being added
   */
  public void addRow(Vector rowData) {
      insertRow(getRowCount(), rowData);
  }

  /*
   * @param   rowData          optional data of the row being added
   */
  public void addRow(Object[] rowData) {
      addRow(convertToVector(rowData));
  }

  /*
   * @param   row             the row index of the row to be inserted
   * @param   rowData         optional data of the row being added
   * @exception  ArrayIndexOutOfBoundsException  if the row was invalid
   */
  public void insertRow(int row, Vector rowData) {
      dataVector.insertElementAt(rowData, row);
  }

  /**
   *  Inserts a row at <code>row</code> in the model.  The new row
   *  will contain <code>null</code> values unless <code>rowData</code>
   *  is specified.  Notification of the row being added will be generated.
   *
   * @param   row      the row index of the row to be inserted
   * @param   rowData          optional data of the row being added
   * @exception  ArrayIndexOutOfBoundsException  if the row was invalid
   */
  public void insertRow(int row, Object[] rowData) {
      insertRow(row, convertToVector(rowData));
  }

   
    /**
     *  Returns a default name for the column using spreadsheet conventions:
     *  A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     *  returns an empty string.
     *
     * @param column  the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    /*
    public String getColumnName(int column) {
        String result = "";
        for (; column >= 0; column = column / 26 - 1) {
            result = (char)((char)(column%26)+'A') + result;
        }
        return result;
    }
    */

   /*
    *  @param   row      the row index of the row to be removed
    * @exception  ArrayIndexOutOfBoundsException  if the row was invalid
    */
   public void removeRow(int row) {
       dataVector.removeElementAt(row);
   }
    
  /*
   * @param   columnIdentifiers  vector of column identifiers.  If
   *                          <code>null</code>, set the model
   *                          to zero columns
   * @see #setNumRows
   */
  public void setColumnIdentifiers(Vector columnIdentifiers) {
      setDataVector(dataVector, columnIdentifiers);
  }

   
 /*
  * @param   newIdentifiers  array of column identifiers.
  *                          If <code>null</code>, set
  *                          the model to zero columns
  * @see #setNumRows
  */
 public void setColumnIdentifiers(Object[] newIdentifiers) {
     setColumnIdentifiers(convertToVector(newIdentifiers));
 }

 /*
  *  @see #setColumnCount
  * @since 1.3
  */
 public void setColumnCount(int columnCount) {
    columnIdentifiers.setSize(columnCount);
 }
 
 
 /*
  * @param   columnName the identifier of the column being added
  */
 public void addColumn(Object columnName) {
     addColumn(columnName, (Vector)null);
 }
 
/*
 * @param   columnName the identifier of the column being added
 * @param   columnData       optional data of the column being added
 */
public void addColumn(Object columnName, Vector columnData) {
    columnIdentifiers.addElement(columnName);
    if (columnData != null) {
        int columnSize = columnData.size();
        if (columnSize > getRowCount()) {
            dataVector.setSize(columnSize);
        }
        int newColumn = getColumnCount() - 1;
        for(int i = 0; i < columnSize; i++) {
              Vector row = (Vector)dataVector.elementAt(i);
              row.setElementAt(columnData.elementAt(i), newColumn);
        }
    }
  
}
 
	/*
	 * @see #addColumn(Object, Vector)
	 */
	public void addColumn(Object columnName, Object[] columnData) {
	   addColumn(columnName, convertToVector(columnData));
	} 
 

    /**
     * Returns the number of rows in this data table.
     * @return the number of rows in the model
     */
    public int getRowCount() {
        return dataVector.size();
    }
  
    public int getColumnCount() {
    	return columnIdentifiers.size();
    }
    
    
    /**
     * Returns a column given its name.
     * Implementation is naive so this should be overridden if
     * this method is to be called often. This method is not
     * in the <code>TableModel</code> interface and is not used by the
     * <code>JTable</code>.
     *
     * @param columnName string containing name of column to be located
     * @return the column with <code>columnName</code>, or -1 if not found
     */
    public int findColumn(String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnName.equals(getColumnName(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    /**
     *  Returns false.  This is the default implementation for all cells.
     *
     *  @param  rowIndex  the row being queried
     *  @param  columnIndex the column being queried
     *  @return false
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public String getColumnName(int column) {
        Object id = null;
        // This test is to cover the case when
        // getColumnCount has been subclassed by mistake ...
        if (column < columnIdentifiers.size() && (column >= 0)) {
            id = columnIdentifiers.elementAt(column);
        }
        return (id == null) ? ""
                            : id.toString();
    }

    /**
     * Returns an attribute value for the cell at <code>row</code>
     * and <code>column</code>.
     *
     * @param   row             the row whose value is to be queried
     * @param   column          the column whose value is to be queried
     * @return                  the value Object at the specified cell
     * @exception  ArrayIndexOutOfBoundsException  if an invalid row or
     *               column was given
     */
    public Object getValueAt(int row, int column) {
        Vector rowVector = (Vector)dataVector.elementAt(row);
        return rowVector.elementAt(column);
    }

    /**
     * Sets the object value for the cell at <code>column</code> and
     * <code>row</code>.  <code>aValue</code> is the new value.  This method
     * will generate a <code>tableChanged</code> notification.
     *
     * @param   aValue          the new value; this can be null
     * @param   row             the row whose value is to be changed
     * @param   column          the column whose value is to be changed
     * @exception  ArrayIndexOutOfBoundsException  if an invalid row or
     *               column was given
     */
    public void setValueAt(Object aValue, int row, int column) {
        Vector rowVector = (Vector)dataVector.elementAt(row);
        rowVector.setElementAt(aValue, column);
    }
//
//  Managing Listeners
//
    
    /**
     * Returns a vector that contains the same objects as the array.
     * @param anArray  the array to be converted
     * @return  the new vector; if <code>anArray</code> is <code>null</code>,
     *                          returns <code>null</code>
     */
    protected static Vector convertToVector(Object[] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (Object o : anArray) {
            v.addElement(o);
        }
        return v;
    }

    /**
     * Returns a vector of vectors that contains the same objects as the array.
     * @param anArray  the double array to be converted
     * @return the new vector of vectors; if <code>anArray</code> is
     *                          <code>null</code>, returns <code>null</code>
     */
    protected static Vector convertToVector(Object[][] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector<Vector> v = new Vector<Vector>(anArray.length);
        for (Object[] o : anArray) {
            v.addElement(convertToVector(o));
        }
        return v;
    }

//
//  Fire methods
//



} // End of class AbstractTableModel