package emnist.app.service.network;

import java.util.Random;

public class ConvolutionalNeuralNetwork {

    public static float[][][] initializeFilters(int kernalSize, int width, int height) {
        Random random = new Random();
        float[][][] result = new float[kernalSize][width][height];
        for (int k = 0; k < kernalSize; k++) {
            result[k] = new float[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    result[k][x][y] = random.nextFloat();
                }
            }
        }
        return result;
    }

}