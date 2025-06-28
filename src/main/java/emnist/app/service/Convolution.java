package emnist.app.service;

public class Convolution {
 
    // 28 x 28
    public static float[][] cachedImage;

    // 3 x 8 x 8
    public static float[][][] cachedFilters;

    public static float[][] convolveImage(float[][] image, float[][] filter) {
        cachedImage = image;
        float[][] result = new float[image.length - 2][image[0].length - 2];

        for (int i = 1; i < image.length - 2; i++) {
            for (int j = 1; j < image[0].length - 2; j++) {
                float[][] convolutionRegion = Matrix.getSubMatrix(image, i - 1, i + 1, j - 1, j + 1);
                result[i][j] = Matrix.getMatrixMultiplicationSum(convolutionRegion, filter);
            }
        }
        return result;
    }

    public static float[][][] forwardPropagate(float[][] image, float[][][] filter) {
        cachedFilters = filter; // 3 x 8 x 8
        float[][][] result = new float[8][26][26];
        for (int k = 0; k < cachedFilters.length; k++) {
            float[][] kernal = convolveImage(image, cachedFilters[k]);
            // ADD KERNAL TO THE RESULT
            result[k] = kernal;
        }
        return result;
    }

}
