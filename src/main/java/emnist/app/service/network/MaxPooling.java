package emnist.app.service.network;

import emnist.app.service.helper.FunctionHelper;
import emnist.app.service.helper.FunctionHelper.Function;
import emnist.app.service.helper.FunctionHelper.BiFunction;
import emnist.app.service.helper.FunctionHelper.TriFunction;
import emnist.app.service.helper.Matrix;

public class MaxPooling {

    public float[][][] cachedImage;

    public float[][][] cachedPooledImage;

    public float[][] getMaxPoolingMatrix(float[][] image) {
        int rowLength = image.length / 2;
        int columnLength = image[0].length / 2;
        float[][] pooledMatrix = new float[rowLength][columnLength];
        BiFunction<Integer, Integer> function = (x, y) -> {
            pooledMatrix[x][y] = Matrix.getMatrixMaximum(Matrix.getSubMatrix(image, x * 2, x * 2 + 1, y * 2, y * 2 + 1));
        };
        FunctionHelper.executeFunction(rowLength, columnLength, function);
        return pooledMatrix;
    }

    public float[][][] propagateForwards(float[][][] image) {
        cachedImage = image;
        cachedPooledImage = new float[image.length][image[0].length][image[0][0].length];
        Function<Integer> function = (index) -> {
            cachedPooledImage[index] = getMaxPoolingMatrix(image[index]);
        };
        FunctionHelper.executeFunction(image.length, function);
        return cachedPooledImage;
    }

    public float[][][] propagateBackwards(float[][][] inputGradientMatrix) {
        // [8] x [26] x [26] OUTPUT GRADIENT MATRIX
        float[][][] outputGradientMatrix = new float[cachedImage.length][cachedImage[0].length][cachedImage[0][0].length];
        TriFunction<Integer, Integer, Integer> function = (x, y, k) -> {
            // GET THE IMAGE REGION [2] x [2]
            float[][] subRegion = Matrix.getSubMatrix(cachedImage[x], y * 2, y * 2 + 1, k * 2, k * 2 + 1);
            // FIND THE REGION'S MAX VALUE
            int maxRowIndex = 0;
            int maxColumnIndex = 0;
            for (int rowIndex = 0; rowIndex < subRegion.length; rowIndex++) {
                for (int columnIndex = 0; columnIndex < subRegion[0].length; columnIndex++) {
                    if(subRegion[rowIndex][columnIndex] > subRegion[maxRowIndex][maxColumnIndex]) {
                        maxRowIndex = rowIndex;
                        maxColumnIndex = columnIndex;
                    }
                }
            }
            // ASSIGN THE MAXIMUM TO THE [8] x [26] x [26] OUTPUT GRADIENT MATRIX
            outputGradientMatrix[x][y * 2 + maxRowIndex][k * 2 + maxColumnIndex] = inputGradientMatrix[x][y][k];
        };
        // LOOP THROUGH THE POOLED IMAGE [8] X [13] X [13] APPLYING THE MAXIMUM TO THE OUTPUT GRADIENT MATRIX
        FunctionHelper.executeFunction(cachedPooledImage.length, cachedPooledImage[0].length, cachedPooledImage[0][0].length, function);
        return outputGradientMatrix;
    }
}
