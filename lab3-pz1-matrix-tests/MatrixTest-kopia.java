package lab2;


import lab2.Matrix;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MatrixTest {

    @org.junit.Test
    public void ordinaryMatrixConstructor(){
        Matrix m = new Matrix(2,3);

        assertEquals(m.shape()[0],2);
        assertEquals(m.shape()[1],3);
    }

    @org.junit.Test
    public void arrayDoubleMatrixConstructor(){
        Matrix m = new Matrix(new double[][]{{1,2,3},{1,2,3,4},{1,2},{1,2,3,4}});
        double[][] arrayForTesting = {{1,2,3,0},{1,2,3,4},{1,2,0,0},{1,2,3,4}};

        for(int i = 0; i < m.shape()[0]; i++){
            double[] rowFromMatrix = m.asArray()[i];
            double[] rowFromTestArray = arrayForTesting[i];

            assertEquals(rowFromMatrix.length, rowFromTestArray.length);
            assertArrayEquals(rowFromMatrix, rowFromTestArray,.1);
        }

    }
    @org.junit.Test
    public void asArray() {
        Matrix m = new Matrix(new double[][]{{3,1},{2,1},{1,0}});
        double[][] arrayForTesting = {{3,1},{2,1},{1,0}};

        for(int i = 0; i < m.shape()[0]; i++){
            assertArrayEquals(m.asArray()[i], arrayForTesting[i],.1);
        }
    }

    @org.junit.Test
    public void get() {
        Matrix m = new Matrix(new double[][]{{3,1},{2,1},{1,0}});

        assertThrows(RuntimeException.class, ()-> m.get(5,5));
        assertEquals(m.get(1,1),1,0.001);
        assertEquals(m.get(0,0),3,0.001);

    }

    @org.junit.Test
    public void set() {
        Matrix m = new Matrix(new double[][]{{3,1},{2,1},{1,0}});
        m.set(1,0, 4);

        assertThrows(RuntimeException.class, ()-> m.set(5,5,0));
        assertEquals(m.get(1,0),4,0.001);
    }

    @org.junit.Test
    public void testToString() {
        Matrix m = new Matrix(new double[][]{{1,2,3},{1,2,3,4},{1,2},{1,2,3,4}});
        String matrixAsString = m.toString();

        long countComas = matrixAsString.chars().filter(ch -> ch == ',').count();
        assertEquals( m.shape()[0] * (m.shape()[1] - 1), countComas);

        long countOpeningBracket = matrixAsString.chars().filter(ch -> ch == '[').count();
        assertEquals(m.shape()[0] + 1, countOpeningBracket);

        long countClosingBracket = matrixAsString.chars().filter(ch -> ch == ']').count();
        assertEquals(m.shape()[0] + 1, countClosingBracket);
    }


    @org.junit.Test
    public void reshape() {
        Matrix m = new Matrix(new double[][]{{1,2,3},{1,2,3,4},{1,2},{1,2,3,4}});

        assertThrows(RuntimeException.class, ()-> m.reshape(4,2));
    }

    @org.junit.Test
    public void sub() {
        Matrix m1 = new Matrix(new double[][]{{1,2,3},{1,2,3,4},{1,2},{1,2,3,4}});
        Matrix m2 = new Matrix(new double[][]{{1,2,3},{1,2,3,4},{1,2},{1,2,3,4}});

        assertEquals(m1.sub(m2).frobenius(),0,.001);
    }

    @org.junit.Test
    public void mul() {
        Matrix m1 = new Matrix(new double[][]{{1,2,3},{1,2,3,4},{1,2},{1,2,3,4}});
        Matrix m2 = m1.mul(-1);

        Matrix incorrectSizeMatrix = new Matrix(new double[][]{{1,2,3},{1,2,2}});

        assertThrows(IllegalArgumentException.class, ()-> m1.mul(incorrectSizeMatrix));
        assertEquals(m1.add(m2).frobenius(),0,.01);

        //second test
        Matrix expectedResult = new Matrix(new double[][]{{1,4,9,0},{1,4,9,16},{1,4,0,0},{1,4,9,16}});
        Matrix resultMatrix = m1.mul(m1);

        for (int i = 0; i < expectedResult.shape()[0]; i++){
            assertArrayEquals(expectedResult.asArray()[i], resultMatrix.asArray()[i],.01);
        }
    }

    @org.junit.Test
    public void add() {
        Matrix m = new Matrix(new double[][]{{3,1},{2,1},{1,0}});

        Matrix incorrectSizeMatrix = new Matrix(new double[][]{{1,2,3},{1,2,2}});

        assertThrows(IllegalArgumentException.class, ()-> m.mul(incorrectSizeMatrix));
        assertEquals(m.add(m.mul(-1)).frobenius(),0,.01);
    }

    @org.junit.Test
    public void div() {
        Matrix m = new Matrix(new double[][]{{3,1},{2,1},{1,2}});

        Matrix incorrectSizeMatrix = new Matrix(new double[][]{{1,2,3},{1,2,2}});

        assertThrows(IllegalArgumentException.class, ()-> m.mul(incorrectSizeMatrix));
        assertEquals(m.div(m).frobenius(), Math.sqrt(m.shape()[0] * m.shape()[1]),.01);

    }

    @org.junit.Test
    public void eye() {
        Matrix m = Matrix.eye(3);

        assertEquals(m.frobenius(), Math.sqrt(3), .001);
    }

    @org.junit.Test
    public void dot() {
        // Checked by WolframAlpha

        Matrix m1 = new Matrix(new double[][] {{1, 2, 3},{4, 5, 6}});
        Matrix m2 = new Matrix(new double[][] {{10, 11}, {20, 21}, {30, 31}});


        Matrix expectedMatrix = new Matrix(new double[][] {{140, 146}, {320, 335}});

        Matrix resultMatrix = m1.dot(m2);

        for (int i = 0; i < expectedMatrix.shape()[0]; i++){
            assertArrayEquals(expectedMatrix.asArray()[i], resultMatrix.asArray()[i],.01);
        }

        Matrix wrongSize = new Matrix(new double[][] {{1, 2, 6, 2}, {4, 5, 4, 5}});

        assertThrows(IllegalArgumentException.class, () -> m1.dot(wrongSize));
    }

}