package dp.module1.lab7;

import dp.module1.lab6_java.MatrixGenerator;

import java.util.concurrent.ForkJoinPool;
public class Main {
    public static ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    public static void main(String[] args) {
        int size = 10;
        double[][]
                matrix1 = new double[size][size],
                matrix2 = new double[size][size];
        double[][] res = new double[matrix1.length][matrix2[0].length];
        matrix1 = MatrixGenerator.generateMatrix(matrix1.length,matrix1.length);
        matrix2 = MatrixGenerator.generateMatrix(matrix2.length,matrix2.length);
        var action = new MatrixRecursiveAction(matrix1,matrix2,res, 0, size-1);
        forkJoinPool.execute(action);
        long start = System.currentTimeMillis();
        action.compute();
//        SequentialMatrixMultiplication.multiply(matrix1, matrix2);
        long end = System.currentTimeMillis();

        System.out.println("Time: " +(end - start)/1000.0 + "s");
    }
}
