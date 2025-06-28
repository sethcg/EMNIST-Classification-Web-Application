package emnist.app.service;

public class Matrix {
    
    public static float[][] getSubMatrix(float[][] matrix, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        float[][] subMatrix = new float[rowEnd - rowStart + 1][columnEnd - columnStart + 1];
        for (int x = 0; x < subMatrix.length; x++) {
            for (int y = 0; y < subMatrix[0].length; y++) {
                subMatrix[x][y] = matrix[rowStart + x][columnStart + y];
            }
        }
        return subMatrix;
	}

	public static float getMatrixMultiplicationSum(float[][] matrixOne, float[][] matrixTwo) {
        float sum = 0;
        for (int x = 0; x < matrixOne.length; x++) {
            for (int y = 0; y < matrixTwo[0].length; y++) {
                sum += matrixOne[x][y] * matrixTwo[x][y];
            }
        }
        return sum;
	}

}
