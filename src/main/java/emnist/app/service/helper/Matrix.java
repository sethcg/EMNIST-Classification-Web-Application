package emnist.app.service.helper;

import java.util.function.BiConsumer;

public class Matrix {

    public static float[][] getSubMatrix(float[][] matrix, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        float[][] subMatrix = new float[rowEnd - rowStart + 1][columnEnd - columnStart + 1];
        Matrix.executeFunction(rowLength, columnLength, (x, y) -> { 
            subMatrix[x][y] = matrix[rowStart + x][columnStart + y];
        });
        return subMatrix;
        // float[][] subMatrix = new float[rowEnd - rowStart + 1][columnEnd - columnStart + 1];
        // for (int x = 0; x < subMatrix.length; x++) {
        //     for (int y = 0; y < subMatrix[0].length; y++) {
        //         subMatrix[x][y] = matrix[rowStart + x][columnStart + y];
        //     }
        // }
        // return subMatrix;
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
        Matrix.executeFunction(rowLength, columnLength, (x, y) -> { 
            outputMatrix[x][y] = matrixOne[x][y] + matrixTwo[x][y];
        });
        return outputMatrix;
        // float[][] outputMatrix = new float[matrixOne.length][matrixOne[0].length];
        // for (int x = 0; x < matrixOne.length; x++) {
        //     for(int y = 0; y < matrixOne[0].length; y++){
        //         outputMatrix[x][y] = matrixOne[x][y] + matrixTwo[x][y];
        //     }
        // }
        // return outputMatrix;
	}

    public static float[][] getElementWiseScaling(float[][] matrix, float scale) {
        int rowLength = matrix.length;
        int columnLength = matrix[0].length;
        float[][] outputMatrix = new float[rowLength][columnLength];
        Matrix.executeFunction(rowLength, columnLength, (x, y) -> { 
            outputMatrix[x][y] = (float) matrix[x][y] * scale;
        });
        return outputMatrix;
        // float[][] outputMatrix = new float[matrix.length][matrix[0].length];
        // for (int x = 0; x < matrix.length; x++) {
        //     for (int y = 0; y < matrix[0].length; y++) {
        //         outputMatrix[x][y] = (float) matrix[x][y] * scale;
        //     }
        // }
        // return outputMatrix;
	}

    private static void executeFunction(Integer rowLength, Integer columnLength, BiConsumer<Integer, Integer> function) {
        for (int x = 0; x < rowLength; x++) {
            for(int y = 0; y < columnLength; y++){
                function.accept(x, y);
            }
        }
    }

}
