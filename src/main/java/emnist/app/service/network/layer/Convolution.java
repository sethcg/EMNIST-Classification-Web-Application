package emnist.app.service.network.layer;

import java.util.Random;

import emnist.app.service.helper.FileManagement;
import emnist.app.service.helper.FunctionHelper;
import emnist.app.service.helper.FunctionHelper.Function;
import emnist.app.service.helper.FunctionHelper.TriFunction;
import emnist.app.service.helper.Matrix;

public class Convolution {

    public float[][] cachedImage;

    public float[][][] cachedFilters;

    private float[][][] getFilters(boolean reset) {
        float[][][] filters = FileManagement.Filters.getMatrixFromFile();
        return reset || filters == null
                ? initializeFilters(8, 3, 3)
                : filters;
    }

    public Convolution(boolean reset) {
        this.cachedFilters = getFilters(reset);
    }

    private static float[][][] initializeFilters(int kernalSize, int width, int height) {
        Random random = new Random();
        float[][][] outputFilter = new float[kernalSize][width][height];
        TriFunction<Integer, Integer, Integer> function = (k, x, y) -> {
            outputFilter[k][x][y] = random.nextFloat();
        };
        FunctionHelper.executeFunction(kernalSize, width, height, function);
        return outputFilter;
    }

    private float[][] convolveImage(float[][] image, float[][] filter) {
        cachedImage = image;
        int rowLength = image.length - 2;
        int columnLength = image[0].length - 2;
        float[][] convolvedImage = new float[rowLength][columnLength];
        for (int x = 1; x < image.length - 2; x++) {
            for (int y = 1; y < image[0].length - 2; y++) {
                float[][] subRegion = Matrix.getSubMatrix(image, x - 1, x + 1, y - 1, y + 1);
                convolvedImage[x][y] = Matrix.getElementWiseMultiplicationSum(subRegion, filter);
            }
        }
        return convolvedImage;
    }

    public float[][][] propagateForwards(float[][] image) {
        // CONVOLVE EACH [3] x [3] REGION OF THE IMAGE,
        // USING "VALID" PADDING: NO ADDITIONAL ROWS/COLUMNS ARE ADDED TO THE IMAGE'S
        // EDGE
        float[][][] outputMatrix = new float[8][26][26];
        Function<Integer> function = (index) -> {
            outputMatrix[index] = convolveImage(image, cachedFilters[index]);
        };
        FunctionHelper.executeFunction(cachedFilters.length, function);
        return outputMatrix;
    }

    public void propagateBackwards(float[][][] inputGradientMatrix, float learningRate) {
        float[][][] outputGradientMatrix = new float[cachedFilters.length][cachedFilters[0].length][cachedFilters[0][0].length];
        for (int x = 1; x < cachedImage.length - 2; x++) {
            for (int y = 1; y < cachedImage[0].length - 2; y++) {
                for (int k = 0; k < cachedFilters.length; k++) {
                    // GET [3] x [3] REGION OF THE ORIGINAL IMAGE
                    float[][] region = Matrix.getSubMatrix(cachedImage, x - 1, x + 1, y - 1, y + 1);
                    float[][] scaledGradient = Matrix.getElementWiseScaling(region,
                            inputGradientMatrix[k][x - 1][y - 1]);
                    outputGradientMatrix[k] = Matrix.getElementWiseAddition(outputGradientMatrix[k], scaledGradient);
                }
            }
        }
        // UPDATE THE FILTERS WITH THE LOSS GRADIENTS CALCULATED ABOVE
        for (int i = 0; i < cachedFilters.length; i++) {
            float[][] lossGradient = Matrix.getElementWiseScaling(outputGradientMatrix[i], -learningRate);
            cachedFilters[i] = Matrix.getElementWiseAddition(cachedFilters[i], lossGradient);
        }
    }

}
