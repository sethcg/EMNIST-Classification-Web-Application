package emnist.app.service.network;

import emnist.app.service.helper.Vector;


public class ConvolutionalNeuralNetwork {

    private static final int INPUT_LAYER_SIZE = 13 * 13 * 8;
    private static final int OUTPUT_LAYER_SIZE = 10;

    private Convolution convolution;
    private MaxPooling maxPooling;
    private SoftMax softMax;

    public ConvolutionalNeuralNetwork() {
        this.convolution = new Convolution();
        this.maxPooling = new MaxPooling();
        this.softMax = new SoftMax(INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE);
    }

    public class ForwardPropogation {
        public float[][] outputLayer;
        public double loss;
        public int accuracy;

        public ForwardPropogation(float[][] outputLayer, double loss, int accuracy) {
            this.outputLayer = outputLayer;
            this.loss = loss;
            this.accuracy = accuracy;
        }
    }

    public ForwardPropogation forwards(float[][] image, Integer label) {
        // We transform the image from [0, 255] to [-0.5, 0.5] to make it easier
        // to work with. This is standard practice.
        float[][][] filterLayer = convolution.propagateForwards(image, convolution.cachedFilters);  // 28 x 28 x 1 => 26 x 26 x 8
        float[][][] poolingLayer = maxPooling.propagateForwards(filterLayer);                       // 26 x 26 x 8 => 13 x 13 x 8
        float[][] outputLayer = softMax.propagateForwards(poolingLayer);                            // 13 x 13 x 8 => 10

        // Calculate cross-entropy loss and accuracy
        double loss = Math.log(outputLayer[0][label]);

        int prediction = 0;
        float confidence = 0.0f;
        for(int i = 0; i < outputLayer.length; i++) {
            float value = outputLayer[0][i];
            if (value > confidence) {
                confidence = value;
                prediction = i;
            }
        }
        int accuracy = prediction == label ? 1 : 0;

        return new ForwardPropogation(outputLayer, loss, accuracy);
    }

    public void backwards(ConvolutionalNeuralNetwork network, float[][] outputLayer, Integer label, Float learningRate) {
        float[][] gradient = Vector.getVectorArrayOfZero(10);
        gradient[0][label] = -1 / outputLayer[0][label];
        float[][][] softMax_gradient = softMax.propagateBackwards(gradient, learningRate);
        float[][][] pooling_gradient = maxPooling.propagateBackwards(softMax_gradient);

        convolution.propagateBackwards(pooling_gradient, learningRate);
    }

}