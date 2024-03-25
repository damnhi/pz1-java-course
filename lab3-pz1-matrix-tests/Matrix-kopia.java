package lab2;

import java.util.Random;

public class Matrix {

    private final double[]data;
    private int rows;
    private int cols;
    //...
    public Matrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        data = new double[rows*cols];
    }

    public Matrix(double[][] d){
        int lengthOfRow = 0;
        for (double[] row : d){
            if(row.length > lengthOfRow){
                lengthOfRow = row.length;
            }
        }

        this.rows = d.length;
        this.cols = lengthOfRow;

        data = new double[d.length * lengthOfRow];
        for (int i = 0; i<d.length; i++){
            for (int j = 0; j<d[i].length; j++){
                data[i*cols + j] = d[i][j];
            }
        }
    }
    public String toString(){
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        for(int i=0;i<rows;i++){
            buf.append("[");
            for(int j=0;j<cols;j++){
                if(j!=0){
                    buf.append(", ");
                }
                buf.append(data[i*cols +j]);
            }
            buf.append("]");
        }
        buf.append("]");
        return buf.toString();
    }

    public double[][] asArray(){
        double[][] twoDimMatrix = new double[rows][cols];
        for (int i = 0; i<rows; i++) {
            for (int j = 0; j < cols; j++) {
                twoDimMatrix[i][j] = data[i*cols + j];
            }
        }
        return twoDimMatrix;
    }

    public double get(int r,int c){
        if(r>rows || c > cols) {
            throw new IllegalArgumentException("Wrong arguments.");
        }
        return data[r*cols + c];
    }

    public void set (int r,int c, double value){
        if(r>rows || c > cols) {
            throw new IllegalArgumentException("Wrong arguments.");
        }
        data[r*cols + c] = value;
    }

    public void reshape(int newRows,int newCols){
        if(rows*cols != newRows*newCols) {
            throw new RuntimeException(String.format("%d x %d matrix can't be reshaped to %d x %d", rows, cols, newRows, newCols));
        }
        rows = newRows;
        cols = newCols;
    }

    public int[] shape(){
        return new int[]{rows,cols};
    }

    private Matrix arithmeticOperation(Matrix m,BinaryFunction fun){
        if(this.cols!=m.cols || this.rows!=m.rows){
            throw new IllegalArgumentException("Wrong matrix dimensions.");
        }

        Matrix addedMatrix= new Matrix(rows,cols);
        for(int i = 0; i<rows; i++){
            for (int j = 0; j<cols; j++){
                double addedElements = fun.compute(this.get(i,j),m.get(i,j));
                addedMatrix.set(i,j,addedElements);
            }
        }

        return addedMatrix;
    }

    private Matrix arithmeticOperationScalar(double w,BinaryFunction fun){

        Matrix addedMatrix= new Matrix(rows,cols);
        for(int i = 0; i<rows; i++){
            for (int j = 0; j<cols; j++){
                double addedElements = fun.compute(this.get(i,j),w);
                addedMatrix.set(i,j,addedElements);
            }
        }

        return addedMatrix;
    }

    public Matrix add(Matrix m){
        if(this.cols!=m.cols || this.rows!=m.rows){
            throw new IllegalArgumentException("Wrong matrix dimensions.");
        }

        return this.arithmeticOperation(m, (double a, double b) -> a+b);
    }

    public Matrix sub(Matrix m){
        if(this.cols!=m.cols || this.rows!=m.rows){
            throw new IllegalArgumentException("Wrong matrix dimensions.");
        }

        return this.arithmeticOperation(m, (double a, double b) -> a-b);
    }

    public Matrix mul(Matrix m){
        if(this.cols!=m.cols || this.rows!=m.rows){
            throw new IllegalArgumentException("Wrong matrix dimensions.");
        }

        return this.arithmeticOperation(m, (double a, double b) -> a*b);
    }

    public Matrix div(Matrix m){
        if(this.cols!=m.cols || this.rows!=m.rows){
            throw new IllegalArgumentException("Wrong matrix dimensions.");
        }

        return this.arithmeticOperation(m, (double a, double b) -> a/b);
    }

    public Matrix add(double w){
        return this.arithmeticOperationScalar(w, (double a, double b) -> a+b);
    } // dodaje wartość w do każdego elementu

    public Matrix sub(double w){
        return this.arithmeticOperationScalar(w, (double a, double b) -> a-b);
    } // odejmuje wartośc w od kazdego elementu

    public Matrix mul(double w){
        return this.arithmeticOperationScalar(w, (double a, double b) -> a*b);
    } // mnoży każdy element przez skalar w

    public Matrix div(double w){
        return this.arithmeticOperationScalar(w, (double a, double b) -> a/b);
    } // dzieli każdy element przez skalar w

    public Matrix dot(Matrix m){
        if(this.cols!=m.rows){
            throw new IllegalArgumentException("Wrong matrix dimensions.");
        }

        Matrix addedMatrix= new Matrix(rows,m.cols);

        for(int i = 0; i<rows; i++){
            for (int j = 0; j<m.cols; j++){
                double addedElements = 0;

                for (int k = 0; k<cols; k++){
                    addedElements += this.get(i, k) * m.get( k, j);
                }

                addedMatrix.set(i,j,addedElements);
            }
        }

        return addedMatrix;
    }

    public double frobenius() {
        double sumOfValues = 0;

        for (double value : data){
            sumOfValues += value * value;
        }

        return Math.sqrt(sumOfValues);
    }
    public static Matrix random(int rows, int cols){
        Matrix m = new Matrix(rows,cols);
        Random r = new Random();

        for(int i = 0; i<rows; i++){
            for (int j=0; j<cols; j++){
                m.set(i,j,r.nextDouble());
            }
        }

        return m;
    }
    public static Matrix eye(int n){
        Matrix m = new Matrix(n,n);

        for (int i=0; i<n; i++){
            m.set(i ,i ,1);
        }

        return m;
    }

}
