package com.emerson.bpm.solver.automata;

import java.lang.reflect.Array;

public class Matrix<T> {
    private T [][] m_data = null;
    private int rows = 0, cols = 0;
    
    private T [][] data = null;
    
    T [] pdata;
    
    public Matrix(Class klazz, int rows, int cols, T [] data) {
    	T [][] xdata;
    	
    	int arrdims[] = new int[2];

    	arrdims[0] = rows;
    	arrdims[1] = cols;
    	
    	xdata = (T [][]) Array.newInstance(klazz, arrdims);
    	
    	pdata = data.clone();
    	
		for(int m=0;m<rows;m++) {
			for(int k=0;k<cols;k++) {
				m_data[m][k] = pdata[k];
			}			
			rotate(1);
		}
    			
        this.rows = rows;
        this.cols = cols;
    }

    
    public Matrix(T [][] data) {
        this.data = data.clone();
        rows = this.data.length;
        cols = this.data[0].length;
    }
    
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    private void rotate(int order) {
    	
    	if (pdata == null || pdata.length==0 || order < 0) {
    		throw new IllegalArgumentException("Illegal argument!");
    	}
     
    	if(order > pdata.length){
    		order = order %pdata.length;
    	}
     
    	//length of first part
    	int a = pdata.length - order; 
     
    	reverse(pdata, 0, a-1);
    	reverse(pdata, a, pdata.length-1);
    	reverse(pdata, 0, pdata.length-1);
    	
    }
    
    private void reverse(T [] pdata, int left, int right){
    	if(pdata == null || pdata.length == 1) 
    		return;
     
    	while(left < right){
    		T temp = pdata[left];
    		pdata[left] = pdata[right];
    		pdata[right] = temp;
    		left++;
    		right--;
    	}	
    }

	public boolean isSquare() {
        return rows == cols;
    }

    public void display() {
        System.out.print("[");
        for (int row = 0; row < rows; ++row) {
            if (row != 0) {
                System.out.print(" ");
            }

            System.out.print("[");

            for (int col = 0; col < cols; ++col) {
                System.out.printf("%8.3f", data[row][col]);

                if (col != cols - 1) {
                    System.out.print(" ");
                }
            }

            System.out.print("]");

            if (row == rows - 1) {
                System.out.print("]");
            }

            System.out.println();
        }
    }

    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                result.data[col][row] = data[row][col];
            }
        }

        return result;
    }

    // Note: exclude_row and exclude_col starts from 1
    public static Matrix subMatrix(Matrix matrix, int exclude_row, int exclude_col) {
        Matrix result = new Matrix(matrix.rows - 1, matrix.cols - 1);

        for (int row = 0, p = 0; row < matrix.rows; ++row) {
            if (row != exclude_row - 1) {
                for (int col = 0, q = 0; col < matrix.cols; ++col) {
                    if (col != exclude_col - 1) {
                        result.data[p][q] = matrix.data[row][col];

                        ++q;
                    }
                }

                ++p;
            }
        }

        return result;
    }

    /*
    public T  determinant() {
        if (rows != cols) {
            return Double.NaN;
        }
        else {
            return _determinant(this);
        }
    }

    private T  _determinant(Matrix matrix) {
        if (matrix.cols == 1) {
            return matrix.data[0][0];
        }
        else if (matrix.cols == 2) {
            return (matrix.data[0][0] * matrix.data[1][1] -
                    matrix.data[0][1] * matrix.data[1][0]);
        }
        else {
            T  result = 0.0;

            for (int col = 0; col < matrix.cols; ++col) {
                Matrix sub = subMatrix(matrix, 1, col + 1);

                result += (Math.pow(-1, 1 + col + 1) *
                           matrix.data[0][col] * _determinant(sub));
            }

            return result;
        }
    }

    public Matrix inverse() {
        T  det = determinant();

        if (rows != cols || det == 0.0) {
            return null;
        }
        else {
            Matrix result = new Matrix(rows, cols);

            for (int row = 0; row < rows; ++row) {
                for (int col = 0; col < cols; ++col) {
                    Matrix sub = subMatrix(this, row + 1, col + 1);

                    result.data[col][row] = (1.0 / det *
                                             Math.pow(-1, row + col) *
                                             _determinant(sub));
                }
            }

            return result;
        }
    }
    */
    
	
}