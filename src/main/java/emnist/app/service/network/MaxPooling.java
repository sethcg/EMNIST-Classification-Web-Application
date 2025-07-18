package emnist.app.service.network;

import emnist.app.service.helper.Matrix;

public class MaxPooling {

    public float[][][] cachedImage;

    public float[][][] cachedPooledImage;

    public float[][] getMaxPoolingMatrix(float[][] image) {
        float[][] pool = new float[image.length / 2][image[0].length / 2];
        for (int x = 0; x < pool.length - 1; x++) {
            for (int y = 0; y < pool[0].length - 1; y++) {
                pool[x][y] = Matrix.getMatrixMaximum(Matrix.getSubMatrix(image, x * 2, x * 2 + 1, y * 2, y * 2 + 1));
            }
        }
        return pool;
    }

    public float[][][] propagateForwards(float[][][] image) {
        cachedImage = image;
        // APPLY MAX POOLING TO REDUCE FROM [8] X [26] X [26] TO [8] X [13] X [13]
        float[][][] result = new float[image.length][image[0].length][image[0][0].length];
        for (int k = 0; k < image.length; k++) {
            result[k] = getMaxPoolingMatrix(image[k]);
        }
        cachedPooledImage = result;
        return result;
    }

    public float[][][] propagateBackwards(float[][][] inputGradientMatrix) {
        float[][][] outputGradientMatrix = new float[cachedImage.length][cachedImage[0].length][cachedImage[0][0].length];
        for (int x = 0; x < cachedPooledImage.length; x++) {
            for (int y = 0; y < cachedPooledImage[0].length; y++) {
                for (int k = 0; k < cachedPooledImage[0][0].length; k++) {
                    float[][] region = Matrix.getSubMatrix(cachedImage[x], y * 2, y * 2 + 1, k * 2, k * 2 + 1);
                    for (int m = 0; m < region.length; m++) {
                        for (int n = 0; n < region[0].length; n++) {
                            if (Math.abs(cachedPooledImage[x][y][k] - region[m][n]) < 0.00000001) {
                                outputGradientMatrix[x][y * 2 + m][k * 2 + n] = inputGradientMatrix[x][y][k];
                            }
                        }
                    }

                }
            }
        }
        return outputGradientMatrix;
    }
}
