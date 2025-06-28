package emnist.app.service;

public class Convolution {
 
    // 28 x 28
    public static float[][] cachedImage;

    // 3 x 8 x 8
    public static float[][][] cachedFilters;

    public static float[][] ConvolveImage(float[][] image, float[][] filter) {
        cachedImage = image;
        float[][] result = new float[image.length - 2][image[0].length - 2];

        for (int i = 1; i < image.length - 2; i++) {
            for (int j = 1; j < image[0].length - 2; j++) {
                float[][] conv_region = GetSubMatrix(image, i - 1, i + 1, j - 1, j + 1);
                result[i][j] = MatrixMultiplication(conv_region, filter);
            }
        }
        return result;
    }

    public static float[][][] ForwardPropagation(float[][] image, float[][][] filter) {
        cachedFilters = filter; // 3 x 8 x 8
        float[][][] result = new float[8][26][26];
        for (int k = 0; k < cachedFilters.length; k++) {
            float[][] kernal = ConvolveImage(image, cachedFilters[k]);
            // ADD KERNAL TO THE RESULT
            result[k] = kernal;
        }
        return result;
    }

    public static float[][] GetSubMatrix(float[][] matrix, int rowStart, int rowEnd, int columnStart, int columnEnd) {
        float[][] sub_matrix = new float[rowEnd - rowStart + 1][columnEnd - columnStart + 1];
        for (int x = 0; x < sub_matrix.length; x++) {
            for (int y = 0; y < sub_matrix[0].length; y++) {
                sub_matrix[x][y] = matrix[rowStart + x][columnStart + y];
            }
        }
        return sub_matrix;
	}

	public static float MatrixMultiplication(float[][] matrixOne, float[][] matrixTwo) {
        float sum = 0;
        for (int x = 0; x < matrixOne.length; x++) {
            for (int y = 0; y < matrixTwo[0].length; y++) {
                sum += matrixOne[x][y] * matrixTwo[x][y];
            }
        }
        return sum;
	}

}
