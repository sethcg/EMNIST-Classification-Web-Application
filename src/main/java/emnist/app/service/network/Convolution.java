package emnist.app.service.network;

import emnist.app.service.helper.Matrix;

public class Convolution {
 
    // 28 x 28
    public static float[][] cachedImage;

    // 3 x 8 x 8
    public static float[][][] cachedFilters;

    public static float[][] convolveImage(float[][] image, float[][] filter) {
        cachedImage = image;
        float[][] result = new float[image.length - 2][image[0].length - 2];

        for (int x = 1; x < image.length - 2; x++) {
            for (int y = 1; y < image[0].length - 2; y++) {
                float[][] convolutionRegion = Matrix.getSubMatrix(image, x - 1, x + 1, y - 1, y + 1);
                result[x][y] = Matrix.getElementWiseMultiplicationSum(convolutionRegion, filter);
            }
        }
        return result;
    }

    public static float[][][] propagateForwards(float[][] image, float[][][] filter) {
        cachedFilters = filter; // 3 x 8 x 8
        float[][][] result = new float[8][26][26];
        for (int k = 0; k < cachedFilters.length; k++) {
            float[][] kernal = convolveImage(image, cachedFilters[k]);
            // ADD KERNAL TO THE RESULT
            result[k] = kernal;
        }
        return result;
    }

    public static void propagateBackwards(float[][][] inputGradientMatrix, float learningRate) {
        float[][][] outputGradientMatrix = new float[cachedFilters.length][cachedFilters[0].length][cachedFilters[0][0].length];
        for(int x = 1; x < cachedImage.length - 2; x++){
            for(int y = 1; y < cachedImage[0].length - 2; y++){
                for(int k = 0; k < cachedFilters.length; k++) {
                    float[][] region = Matrix.getSubMatrix(cachedImage,  x - 1, x + 1, y - 1, y + 1);
                    outputGradientMatrix[k] = Matrix.getElementWiseAddition(outputGradientMatrix[k], Matrix.getElementWiseScaling(region, inputGradientMatrix[k][x - 1][y - 1]));
                }
            }
        }
        for(int i = 0; i < cachedFilters.length; i++) {
            cachedFilters[i]= Matrix.getElementWiseAddition(cachedFilters[i], Matrix.getElementWiseScaling(outputGradientMatrix[i], -learningRate));
        }  
    }

}
