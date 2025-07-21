package emnist.app.service.network;

import java.util.function.Consumer;

import emnist.app.service.helper.Vector;
import emnist.app.service.image.EmnistData;
import emnist.app.service.image.EmnistData.EmnistBatch;
import emnist.app.service.image.EmnistData.EmnistImage;

public class ConvolutionalNeuralNetwork implements Consumer<EmnistData> {

    private static final int INPUT_LAYER_SIZE = 13 * 13 * 8;
    private static final int OUTPUT_LAYER_SIZE = 10;
    private static final float LEARNING_RATE = 0.005f;

    private Convolution convolution;
    private MaxPooling maxPooling;
    private SoftMax softMax;

    public ConvolutionalNeuralNetwork() {
        this.convolution = new Convolution();
        this.maxPooling = new MaxPooling();
        this.softMax = new SoftMax(INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE);
    }

    private class TrainingResult {
        private int accuracy;
        private double loss;

        private TrainingResult(int accuracy, double loss) {
            this.accuracy = accuracy;
            this.loss = loss;
        }
    }

    private class TestingResult {
        private int numCorrect;
        private double loss;

        private TestingResult(int numCorrect, double loss) {
            this.numCorrect = numCorrect;
            this.loss = loss;
        }
    }
        
    @Override
    public void accept(EmnistData data) {
        int rows = 0;
        int batchNum = 0;
        int epochNum = 0;

        switch(data.dataType) {
            case TEST:
                double loss = 0;
                int numCorrect = 0;

                for (EmnistBatch batch : data.batches) {
                    rows += batch.images.length;
                    int steps = (rows / (++batchNum));

                    // DEBUG
                    System.out.print(String.format("%-10s", ("Batch[" + batchNum + "]:")));

                    TestingResult result = this.test(steps, batch.images);

                    loss += result.loss;
                    numCorrect += result.numCorrect;

                    // DEBUG
                    double averageLoss = loss / (double) rows;
                    double averageAccuracy = ((double) numCorrect / (double) rows) * 100.0f;
                    System.out.print(" (Step: " + steps + ")");
                    System.out.print(" Avg Loss: " + String.format("%.2f", averageLoss) + "%");
                    System.out.print(" Avg Accuracy: " + String.format("%.1f", averageAccuracy) + "%\n");
                }

                // DEBUG
                double averageLoss = loss / (double) rows;
                double averageAccuracy = ((double) numCorrect / (double) rows) * 100.0f;
                System.out.print("\nAverage Loss: " + String.format("%.2f", averageLoss) + "%");
                System.out.print("\nAverage Accuracy: " + String.format("%.1f", averageAccuracy) + "%");
                break;
            case TRAIN:
                for (EmnistBatch batch : data.batches) {
                    // DEBUG
                    if(rows % EmnistData.EPOCH_SIZE == 0) {
                        System.out.print("\n---=== Epoch " + (++epochNum) + " ===---\n");
                    }

                    rows += batch.images.length;
                    int steps = (rows / (++batchNum));

                    // DEBUG
                    System.out.print(String.format("%-10s", ("Batch[" + batchNum + "]:")));

                    TrainingResult result = this.train(steps, batch.images);

                    // DEBUG
                    System.out.print(" (Step: " + steps + ")");
                    System.out.print(" Loss: " + String.format("%.2f", result.loss) + " Accuracy: " + result.accuracy + "%\n");
                }
                break;
        }
    }

    private TestingResult test(int steps, EmnistImage[] images) {
        int numCorrect = 0;
        int lossTotal = 0;

        for(EmnistImage emnistImage: images) {
            float[][] image = emnistImage.image;
            int label = emnistImage.label;

            // FORWARD PROPAGATE
            float[][] outputLayer = this.forwards(image, label);

            // CALCULATE CROSS-ENTROPY LOSS AND ACCURACY
            double loss = Math.log(outputLayer[0][label]);
            int accurate = label == Vector.getVectorArrayMaximumIndex(outputLayer) ? 1 : 0;
      
            numCorrect += accurate;
            lossTotal += loss;
        }
        return new TestingResult(numCorrect, lossTotal);
    }

    private TrainingResult train(int steps, EmnistImage[] images) {
        int accurateTotal = 0;
        int lossTotal = 0;

        for(EmnistImage emnistImage: images) {
            float[][] image = emnistImage.image;
            int label = emnistImage.label;
            
            // FORWARD PROPAGATE
            float[][] outputLayer = this.forwards(image, label);

            // CALCULATE CROSS-ENTROPY LOSS AND ACCURACY
            double loss = Math.log(outputLayer[0][label]);
            int accurate = label == Vector.getVectorArrayMaximumIndex(outputLayer) ? 1 : 0;
      
            // BACKWARD PROPAGATE
            this.backwards(outputLayer, label, LEARNING_RATE);

            accurateTotal += accurate;
            lossTotal += loss;
        }

        int accuracy = (int)((accurateTotal * 100.0f) / steps);
        double loss = lossTotal / 100.0;

        return new TrainingResult(accuracy, loss);
    }

    private float[][] forwards(float[][] image, int label) {
        // KERNAL LAYER  [8] x [26] x [26]
        float[][][] filterLayer = convolution.propagateForwards(image);
        // POOLING LAYER [8] x [13] x [13]
        float[][][] poolingLayer = maxPooling.propagateForwards(filterLayer);
        // OUTPUT LAYER  [10]
        return softMax.propagateForwards(poolingLayer);
    }

    private void backwards(float[][] outputLayer, Integer label, Float learningRate) {
        float[][] gradient = Vector.getVectorArrayOfZero(10);
        gradient[0][label] = -1 / outputLayer[0][label];
        float[][][] softMax_gradient = softMax.propagateBackwards(gradient, learningRate);
        float[][][] pooling_gradient = maxPooling.propagateBackwards(softMax_gradient);
        convolution.propagateBackwards(pooling_gradient, learningRate);
    }
    
}