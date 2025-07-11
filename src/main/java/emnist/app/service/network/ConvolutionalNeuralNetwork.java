package emnist.app.service.network;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

import ch.qos.logback.core.joran.sanity.Pair;
import emnist.app.service.helper.ImageProcessor;
import emnist.app.service.helper.ImageProcessor.TrainingImageProcessor;
import emnist.app.service.helper.ParquetFileReader;


public class ConvolutionalNeuralNetwork {

    public static float[][][] cachedFilters;

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

    public class ForwardPropogation {
        public float[] outputLayer;
        public double loss;
        public int accuracy;

        public ForwardPropogation(float[] outputLayer, double loss, int accuracy) {
            this.outputLayer = outputLayer;
            this.loss = loss;
            this.accuracy = accuracy;
        }
    }

    public ForwardPropogation forwards(float[][] image, Integer label) {
        // We transform the image from [0, 255] to [-0.5, 0.5] to make it easier
        // to work with. This is standard practice.
        float[][][] filterLayer = Convolution.propagateForwards(image, cachedFilters);  // 28 x 28 x 1 => 26 x 26 x 8
        float[][][] poolingLayer = MaxPooling.propagateForwards(filterLayer);           // 26 x 26 x 8 => 13 x 13 x 8
        float[] outputLayer = SoftMax.propagateForwards(poolingLayer);                  // 13 x 13 x 8 => 10

        // Calculate cross-entropy loss and accuracy. np.log() is the natural log.
        double loss = Math.log(outputLayer[label]);
        
        int prediction = 0;
        float confidence = 0.0f;
        for(int i = 0; i < outputLayer.length; i++){
            float value = outputLayer[i];
            if (value > confidence) {
                confidence = value;
                prediction = i;
            }
        }
        int accuracy = prediction == label ? 1 : 0;

        return new ForwardPropogation(outputLayer, loss, accuracy);
    }

}