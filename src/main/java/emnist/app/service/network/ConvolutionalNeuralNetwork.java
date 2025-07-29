package emnist.app.service.network;

import java.util.function.Consumer;

import emnist.app.service.helper.FileManagement;
import emnist.app.service.helper.Vector;
import emnist.app.service.image.EmnistData;
import emnist.app.service.image.EmnistData.EmnistImage;
import emnist.app.service.notification.Notification;
import emnist.app.service.notification.NotificationService;

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
        private double accuracy;
        private double loss;

        private TrainingResult(double accuracy, double loss) {
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
        int rows = data.emnistBatch.rows;
        int epochNum = data.emnistBatch.epochNum;
        int batchNum = data.emnistBatch.batchNum;

        EmnistImage[] images = data.emnistBatch.images;
        int steps = rows / batchNum;

        Notification notification = new Notification(epochNum, batchNum);

        switch(data.dataType) {
            case TRAIN:
                // DEBUG
                // String batchMessage = String.format("%-10s", ("Batch[" + batchNum + "]:"));

                TrainingResult result = this.train(steps, images);

                // DEBUG
                // batchMessage += " (Step: " + steps + ")";
                // batchMessage += " Loss: " + String.format("%.2f", result.loss);
                // batchMessage += " Accuracy: " + String.format("%.1f", result.accuracy) + "%";
                // System.out.println(batchMessage);

                notification.steps = steps;
                notification.batchNum = batchNum;
                notification.loss = String.format("%.2f", result.loss);
                notification.accuracy = String.format("%.1f", result.accuracy) + "%";
                NotificationService.sendNotification("trainingUpdate", notification);

                if(data.emnistBatch.isLastBatch) {
                    // AFTER TRAINING IS DONE SAVE THE FILTERS, WEIGHTS, BIAS FOR LATER USE
                    FileManagement.Filters.saveMatrix(convolution.cachedFilters);
                    FileManagement.Weights.saveMatrix(softMax.cachedWeights);
                    FileManagement.Bias.saveMatrix(softMax.cachedBias);
                }
                break;
            case TEST:
                double loss = 0;
                int numCorrect = 0;

                // DEBUG
                // String batchMessage = String.format("%-10s", ("Batch[" + batchNum + "]:"));

                TestingResult testingResult = this.test(steps, images);

                loss += testingResult.loss;
                numCorrect += testingResult.numCorrect;

                double averageLoss = loss / (double) steps;
                double averageAccuracy = ((double) numCorrect / (double) steps) * 100.0f;

                // DEBUG
                // batchMessage += " (Step: " + steps + ")";
                // batchMessage += " Avg Loss: " + String.format("%.2f", averageLoss) + "%";
                // batchMessage += " Avg Accuracy: " + String.format("%.1f", averageAccuracy) + "%";
                // System.out.println(batchMessage);

                notification.steps = steps;
                notification.batchNum = batchNum;
                notification.loss = String.format("%.2f", averageLoss);
                notification.accuracy = String.format("%.1f", averageAccuracy) + "%";
                NotificationService.sendNotification("testingUpdate", notification);
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

        double accuracy = (double)((accurateTotal * 100.0f) / steps);
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