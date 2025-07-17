package emnist.app.service.helper;

import emnist.app.service.helper.FunctionHelper.BiFunction;

public class Matrix {

    public static float[][] getRandomizedMatrix(Integer rowLength, Integer columnLength) {
        float[][] outputMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer, Void> function = (x, y) -> { 
            outputMatrix[x][y] = (float) Math.random();
            return null;
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
    }

    public static float[][] getSubMatrix(float[][] matrix, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        float[][] subMatrix = new float[rowEnd - rowStart + 1][columnEnd - columnStart + 1];
        int rowLength = subMatrix.length;
        int columnLength = subMatrix[0].length;
        BiFunction<Integer, Integer, Void> function = (x, y) -> { 
            subMatrix[x][y] = matrix[rowStart + x][columnStart + y];
            return null;
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return subMatrix;
	}

    public static float[][] getFlattenedMatrix(float[][][] matrix) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        int kernalSize = matrix[0][0].length;
        float[][] vector = new float[1][rowLength * columnLength * kernalSize];
        int index = 0;
        for (int x = 0; x < rowLength; x++) {
            for (int y = 0; y < columnLength; y++) {
                for (int k = 0; k < kernalSize; k++) {
                    vector[0][index] = matrix[x][y][k];
                    index++;
                }
            }
        }
        return vector;
    }

    public static float[][][] getReshapedMatrix(float[][] matrix, int depth, int rowLength, int columnLength) {
        int index = 0;
        float[][][] outputMatrix = new float[depth][rowLength][columnLength];
        for(int k = 0; k < depth; k++) {
            for(int x = 0; x < rowLength; x++) {
                for(int y = 0; y < columnLength; y++) {
                    outputMatrix[k][x][y] = matrix[0][index];
                    index++;
                }
            }
        }
        return outputMatrix;
    }

    public static float[][] getMultipliedMatrix(float[][] matrixOne, float[][] matrixTwo) {
        int rowLength = matrixOne.length;
        int columnLength = matrixTwo[0].length;
        float[][] outputMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer, Void> function = (x, y) -> {
            for (int k = 0; k < matrixOne[0].length; k++) {
                outputMatrix[x][y] += matrixOne[x][k] * matrixTwo[k][y];
            }
            return null;
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
    }

    public static float[][] getTransposedMatrix(float[][] matrix) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        float[][] outputMatrix = new float[columnLength][rowLength];
        BiFunction<Integer, Integer, Void> function = (x, y) -> {
            outputMatrix[y][x] = matrix[x][y];
            return null;
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
    }

    public static float getMatrixMaximum(float[][] matrix) {
        float max = matrix[0][0];
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                max = max < matrix[x][y] ? matrix[x][y] : max;
            }
        }
        return max;
    }

	public static float getElementWiseMultiplicationSum(float[][] matrixOne, float[][] matrixTwo) {
        float sum = 0;
        for (int x = 0; x < matrixOne.length; x++) {
            for (int y = 0; y < matrixTwo[0].length; y++) {
                sum += matrixOne[x][y] * matrixTwo[x][y];
            }
        }
        return sum;
	}

    public static float[][] getElementWiseAddition(float[][] matrixOne, float[][] matrixTwo) {
        int rowLength = matrixOne.length;
        int columnLength = matrixOne[0].length;
        float[][] outputMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer, Void> function = (x, y) -> { 
            outputMatrix[x][y] = matrixOne[x][y] + matrixTwo[x][y];
            return null;
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
	}

    public static float[][] getElementWiseScaling(float[][] matrix, float scale) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        float[][] outputMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer, Void> function = (x, y) -> { 
            outputMatrix[x][y] = (float) matrix[x][y] * scale;
            return null;
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return outputMatrix;
	}

}
