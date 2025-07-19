package emnist.app.service.helper;

import java.util.concurrent.atomic.AtomicReference;

import emnist.app.service.helper.FunctionHelper.BiFunction;
import emnist.app.service.helper.FunctionHelper.TriFunction;

public class Matrix {

    public static float[][] getRandomizedMatrix(Integer rowLength, Integer columnLength) {
        float[][] outputMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer> function = (x, y) -> { 
            outputMatrix[x][y] = (float) Math.random();
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
    }

    public static float[][] getSubMatrix(float[][] matrix, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        float[][] subMatrix = new float[rowEnd - rowStart + 1][columnEnd - columnStart + 1];
        int rowLength = subMatrix.length;
        int columnLength = subMatrix[0].length;
        BiFunction<Integer, Integer> function = (x, y) -> { 
            subMatrix[x][y] = matrix[rowStart + x][columnStart + y];
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return subMatrix;
	}

    public static float[][] getFlattenedMatrix(float[][][] matrix) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        int depthLength = matrix[0][0].length;
        AtomicReference<Integer> index = new AtomicReference<Integer>(0);
        float[][] outputMatrix = new float[1][rowLength * columnLength * depthLength];
        TriFunction<Integer, Integer, Integer> function = (x, y, k) -> {
            outputMatrix[0][index.get()] = matrix[x][y][k];
            index.set(index.get() + 1);
        };
        FunctionHelper.executeFunction(rowLength, columnLength, depthLength, function);
        return outputMatrix;
    }

    public static float[][][] getReshapedMatrix(float[][] matrix, int depthLength, int rowLength, int columnLength) {
        AtomicReference<Integer> index = new AtomicReference<Integer>(0);
        float[][][] outputMatrix = new float[depthLength][rowLength][columnLength];
        TriFunction<Integer, Integer, Integer> function = (x, y, k) -> {
            outputMatrix[k][x][y] = matrix[0][index.get()];
            index.set(index.get() + 1);
        };
        FunctionHelper.executeFunction(rowLength, columnLength, depthLength, function);
        return outputMatrix;
    }

    public static float[][] getMultipliedMatrix(float[][] matrixOne, float[][] matrixTwo) {
        int rowLength = matrixOne.length;
        int columnLength = matrixTwo[0].length;
        int depthLength = matrixOne[0].length;
        float[][] outputMatrix = new float[matrixOne.length][matrixTwo[0].length];
        TriFunction<Integer, Integer, Integer> function = (x, y, k) -> {
            outputMatrix[x][y] += matrixOne[x][k] * matrixTwo[k][y];
        };
        FunctionHelper.executeFunction(rowLength, columnLength, depthLength, function);
        return outputMatrix;
    }

    public static float[][] getTransposedMatrix(float[][] matrix) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        float[][] outputMatrix = new float[columnLength][rowLength];
        BiFunction<Integer, Integer> function = (x, y) -> {
            outputMatrix[y][x] = matrix[x][y];
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
    }

    public static float getMatrixMaximum(float[][] matrix) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        AtomicReference<Float> max = new AtomicReference<Float>(matrix[0][0]);
        BiFunction<Integer, Integer> function = (x, y) -> {
            if(max.get() < matrix[x][y]) {
                max.set(matrix[x][y]);
            }
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return max.get();
    }

	public static float getElementWiseMultiplicationSum(float[][] matrixOne, float[][] matrixTwo) {
        int rowLength = matrixOne.length;
        int columnLength = matrixTwo[0].length;
        AtomicReference<Float> sum = new AtomicReference<Float>(0.0f);
        BiFunction<Integer, Integer> function = (x, y) -> {
            sum.set(sum.get() + matrixOne[x][y] * matrixTwo[x][y]);
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return sum.get();
	}

    public static float[][] getElementWiseAddition(float[][] matrixOne, float[][] matrixTwo) {
        int rowLength = matrixOne.length;
        int columnLength = matrixOne[0].length;
        float[][] outputMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer> function = (x, y) -> { 
            outputMatrix[x][y] = matrixOne[x][y] + matrixTwo[x][y];
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
	}

    public static float[][] getElementWiseScaling(float[][] matrix, float scale) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        float[][] outputMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer> function = (x, y) -> { 
            outputMatrix[x][y] = (float) matrix[x][y] * scale;
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
	}

}
