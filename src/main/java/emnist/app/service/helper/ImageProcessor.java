package emnist.app.service.helper;

import java.util.function.Consumer;

import emnist.app.service.helper.ParquetFileReader.ImageItem;
import emnist.app.service.network.ConvolutionalNeuralNetwork;
import emnist.app.service.network.ConvolutionalNeuralNetwork.ForwardPropogation;

public class ImageProcessor {

    public static class TrainingImageProcessor implements Consumer<ImageItem[]> {

        public static ConvolutionalNeuralNetwork network;
        public static Float learningRate = 0.005f;
        public static Integer accuracySum = 0;
        
        public static Integer totalRows = 0;
        public static Integer batches = 0;

        public TrainingImageProcessor(ConvolutionalNeuralNetwork network) {
            TrainingImageProcessor.network = network;
        }

        @Override
        public void accept(ImageItem[] images) {
            totalRows += images.length;
            batches += 1;

            System.out.println("Total Rows: " + totalRows + " Batch Number: " + batches);

            // DO TRAINING WITH IMAGES HERE
            for(int index = 0; index < images.length; index++) {
                float[][] image = images[index].image;
                Integer label = images[index].label;
                
                // FORWARD PROPAGATE
                ForwardPropogation forward = network.forwards(image, label);

                // BACKWARD PROPAGATE
                network.backwards(network, forward.outputLayer, label, learningRate);

                if(index % 100 == 0) {
                    System.out.println(" step: " + index + " loss: " + forward.loss / 100.0 + " accuracy: " + forward.accuracy);
                    accuracySum += forward.accuracy;
                }
            }
            System.out.println("average accuracy:- "+ accuracySum / totalRows + "%");

        }
    }

    public static class TestingImageProcessor implements Consumer<ImageItem[]> {

        public Integer totalRows = 0;
        public Integer batches = 0;

        @Override
        public void accept(ImageItem[] images) {
            totalRows += images.length;
            batches += 1;

            // DO TESTING WITH IMAGES HERE


        }
    }
}