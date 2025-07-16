package emnist.app.service.helper;

import java.util.function.Consumer;

import emnist.app.service.Service;
import emnist.app.service.helper.ParquetFileReader.ImageItem;
import emnist.app.service.network.ConvolutionalNeuralNetwork;
import emnist.app.service.network.ConvolutionalNeuralNetwork.ForwardPropogation;

public class ImageProcessor {

    public static class TrainingImageProcessor implements Consumer<ImageItem[]> {

        public static ConvolutionalNeuralNetwork network;
        public static Float learningRate = 0.005f;
        
        public static Integer rows = 0;
        public static Integer batchNum = 0;

        public TrainingImageProcessor(ConvolutionalNeuralNetwork network) {
            TrainingImageProcessor.network = network;
        }

        @Override
        public void accept(ImageItem[] images) {
            rows += images.length;
            batchNum += 1;

            int steps = rows / batchNum;
            int accurateTotal = 0;
            double lossTotal = 0;

            // DO TRAINING WITH IMAGES HERE
            for(int index = 0; index < images.length; index++) {
                float[][] image = images[index].image;
                Integer label = images[index].label;
                
                // FORWARD PROPAGATE
                ForwardPropogation forward = network.forwards(image, label);
                
                // BACKWARD PROPAGATE
                network.backwards(network, forward.outputLayer, label, learningRate);

                accurateTotal += forward.accurate;
                lossTotal += forward.loss;
                // if(index % 99 == 0 && index != 0) {
                //     int accuracy = (int)((accuracySum * 100.0f) / totalRows);
                //     System.out.println(" Step: " + (index + 1) + " Loss: " + forward.loss / 100.0 + " Accuracy: " + accuracy + "%");
                // }
            }

            int accuracy = (int)((accurateTotal * 100.0f) / steps);
            double loss = lossTotal / 100.0;

            System.out.print("[Step " + steps + "] ");
            System.out.println(" Loss: " + loss + " Accuracy: " + accuracy + "%");
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