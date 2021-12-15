package com.company.Module1.Lab7;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class MatrixRecursiveAction extends RecursiveAction {

    private final double[][] matrixA;
    private final double[][] matrixB;
    private double[][] res;
    private final int startI;
    private final int endI;

    public MatrixRecursiveAction(double[][] matrixA, double[][] matrixB, double[][] res, int startI, int endI) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.res = res;
        this.startI = startI;
        this.endI = endI;
    }

    @Override
    protected void compute() {
        if (startI + 1< endI) {
            ForkJoinTask.invokeAll(createSubtasks());
        } else{
            processing();
        }
    }

    private List<MatrixRecursiveAction> createSubtasks() {
        List<MatrixRecursiveAction> subtasks = new ArrayList<>();
        int center = startI + (endI - startI)/2;

        subtasks.add(new MatrixRecursiveAction(matrixA, matrixB, res, startI, center));
        subtasks.add(new MatrixRecursiveAction(matrixA, matrixB, res, center + 1, endI));

        return subtasks;
    }

    private void processing() {
        for(int i = startI; i <= endI; i+=1){
            for(int j = 0; j < res[i].length; j++){
                res[i][j] = 0;
                for (int k = 0; k < matrixA[0].length; k++){
                    res[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }
}