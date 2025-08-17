package emnist.app.service.network;

import java.util.function.Consumer;

import emnist.app.service.helper.FileManagement;
import emnist.app.service.helper.Vector;
import emnist.app.service.image.EmnistData;
import emnist.app.service.image.EmnistData.EmnistImage;
import emnist.app.service.network.layer.Convolution;
import emnist.app.service.network.layer.MaxPooling;
import emnist.app.service.network.layer.SoftMax;
import emnist.app.service.notification.Notification;
import emnist.app.service.notification.NotificationService;

public class Network implements Consumer<EmnistData> {

    private static final int INPUT_LAYER_SIZE = 13 * 13 * 8;
    private static final int OUTPUT_LAYER_SIZE = 10;
    private static final float LEARNING_RATE = 0.005f;

    private Convolution convolution;
    private MaxPooling maxPooling;
    private SoftMax softMax;

    public NetworkStats trainingStats;
    public NetworkStats testingStats;

    public Network() {
        this.convolution = new Convolution(false);
        this.maxPooling = new MaxPooling();
        this.softMax = new SoftMax(false, INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE);
        this.trainingStats = new NetworkStats();
        this.testingStats = new NetworkStats();
    }

    private class BatchResult {
        private double accuracy, loss = 0.0f;

        private BatchResult(double accuracy, double loss) {
            this.accuracy = accuracy;
            this.loss = loss;
        }
    }

    public void reset() {
        this.convolution = new Convolution(true);
        this.maxPooling = new MaxPooling();
        this.softMax = new SoftMax(true, INPUT_LAYER_SIZE, OUTPUT_LAYER_SIZE);
        this.trainingStats = new NetworkStats();
        this.testingStats = new NetworkStats();
    }

    @Override
    public void accept(EmnistData data) {
        int epochNum = data.emnistBatch.epochNum;
        int batchNum = data.emnistBatch.batchNum;

        EmnistImage[] images = data.emnistBatch.images;
        Notification notification = new Notification(epochNum, batchNum);

        switch (data.dataType) {
            case TRAIN:
                BatchResult trainingResult = this.train(images);

                notification.batchNum = batchNum;
                notification.steps = data.emnistBatch.images.length;
                notification.loss = String.format("%.2f", trainingResult.loss);
                notification.accuracy = String.format("%.1f", trainingResult.accuracy) + "%";
                NotificationService.sendNotification("trainingUpdate", notification);

                // AVERAGE NETWORK STATISTICS
                trainingStats.imageNum += data.emnistBatch.images.length;
                trainingStats.accuracy = batchNum > 1 
                        ? (trainingStats.accuracy + trainingResult.accuracy) / 2.0f
                        : trainingResult.accuracy;
                trainingStats.loss = batchNum > 1 
                        ? (trainingStats.loss + trainingResult.loss) / 2.0f
                        : trainingResult.loss;

                if (data.emnistBatch.isLastBatch) {
                    // AFTER TRAINING IS DONE SAVE THE FILTERS, WEIGHTS, BIAS, AND STATISTICS FOR
                    // LATER USE
                    FileManagement.Filters.saveMatrix(convolution.cachedFilters);
                    FileManagement.Weights.saveMatrix(softMax.cachedWeights);
                    FileManagement.Bias.saveMatrix(softMax.cachedBias);
                    FileManagement.Statistics.saveStatistics(trainingStats, FileManagement.TRAINING_STATISTICS_FILENAME);

                    // REMOVE PREVIOUS (OUTDATED) TESTING STATISTICS
                    FileManagement.RemoveFile(FileManagement.TESTING_STATISTICS_FILENAME);
                }
                break;
            case TEST:
                BatchResult testingResult = this.test(images);

                notification.batchNum = batchNum;
                notification.steps = data.emnistBatch.images.length;
                notification.loss = String.format("%.2f", testingResult.loss);
                notification.accuracy = String.format("%.1f", testingResult.accuracy) + "%";
                NotificationService.sendNotification("testingUpdate", notification);

                // AVERAGE NETWORK STATISTICS
                testingStats.imageNum += data.emnistBatch.images.length;
                testingStats.accuracy = batchNum > 1 
                        ? (testingStats.accuracy + testingResult.accuracy) / 2.0f
                        : testingResult.accuracy;
                testingStats.loss = batchNum > 1 
                        ? (testingStats.loss + testingResult.loss) / 2.0f 
                        : testingResult.loss;

                if (data.emnistBatch.isLastBatch) {
                    // AFTER TESTING IS DONE SAVE THE STATISTICS
                    FileManagement.Statistics.saveStatistics(testingStats, FileManagement.TESTING_STATISTICS_FILENAME);
                }
                break;
        }
    }

    public int predict(EmnistImage emnistImage) {
        float[][] outputLayer = this.forwards(emnistImage.image);
        return Vector.getVectorArrayMaximumIndex(outputLayer);
    }

    private BatchResult test(EmnistImage[] images) {
        int accurateTotal = 0;
        int lossTotal = 0;

        for (EmnistImage emnistImage : images) {
            float[][] image = emnistImage.image;
            int label = emnistImage.label;

            // FORWARD PROPAGATE
            float[][] outputLayer = this.forwards(image);

            // CALCULATE CROSS-ENTROPY LOSS AND ACCURACY
            double loss = Math.log(outputLayer[0][label]);
            int accurate = label == Vector.getVectorArrayMaximumIndex(outputLayer) ? 1 : 0;

            accurateTotal += accurate;
            lossTotal += loss;
        }

        double loss = lossTotal / (double) images.length;
        double accuracy = ((double) accurateTotal / (double) images.length) * 100.0f;

        return new BatchResult(accuracy, loss);
    }

    private BatchResult train(EmnistImage[] images) {
        int accurateTotal = 0;
        int lossTotal = 0;

        for (EmnistImage emnistImage : images) {
            float[][] image = emnistImage.image;
            int label = emnistImage.label;

            // FORWARD PROPAGATE
            float[][] outputLayer = this.forwards(image);

            // CALCULATE CROSS-ENTROPY LOSS AND ACCURACY
            double loss = Math.log(outputLayer[0][label]);
            int accurate = label == Vector.getVectorArrayMaximumIndex(outputLayer) ? 1 : 0;

            // BACKWARD PROPAGATE
            this.backwards(outputLayer, label, LEARNING_RATE);

            accurateTotal += accurate;
            lossTotal += loss;
        }

        double loss = lossTotal / (double) images.length;
        double accuracy = ((double) accurateTotal / (double) images.length) * 100.0f;

        return new BatchResult(accuracy, loss);
    }

    private float[][] forwards(float[][] image) {
        // KERNAL LAYER [8] x [26] x [26]
        float[][][] filterLayer = convolution.propagateForwards(image);
        // POOLING LAYER [8] x [13] x [13]
        float[][][] poolingLayer = maxPooling.propagateForwards(filterLayer);
        // OUTPUT LAYER [10]
        return softMax.propagateForwards(poolingLayer);
    }

    private void backwards(float[][] outputLayer, int label, Float learningRate) {
        float[][] gradient = Vector.getVectorArrayOfZero(10);
        gradient[0][label] = -1 / outputLayer[0][label];
        float[][][] softMax_gradient = softMax.propagateBackwards(gradient, learningRate);
        float[][][] pooling_gradient = maxPooling.propagateBackwards(softMax_gradient);
        convolution.propagateBackwards(pooling_gradient, learningRate);
    }

}