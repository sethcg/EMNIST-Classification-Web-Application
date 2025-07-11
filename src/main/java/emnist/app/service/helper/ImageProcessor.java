package emnist.app.service.helper;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

import emnist.app.service.Service;
import emnist.app.service.network.ConvolutionalNeuralNetwork;
import emnist.app.service.network.ConvolutionalNeuralNetwork.ForwardPropogation;

public class ImageProcessor {

    public static class TrainingImageProcessor implements Consumer<HashMap<Integer, float[][]>> {

        public static ConvolutionalNeuralNetwork network;
        public static Float learningRate = 0.005f;
        public static Integer accuracySum = 0;
        
        public static Integer totalRows = 0;
        public static Integer batches = 0;

        public TrainingImageProcessor(ConvolutionalNeuralNetwork network) {
            TrainingImageProcessor.network = network;
        }

        @Override
        public void accept(HashMap<Integer, float[][]> images) {
            totalRows += images.size();
            batches += 1;

            int index = 0;
    
            // DO TRAINING WITH IMAGES HERE
            for(Entry<Integer, float[][]> entry : images.entrySet()) {
                Integer label = entry.getKey();
                float[][] image = entry.getValue();
                
                // FORWARD PROPAGATE
                ForwardPropogation forward = network.forwards(image, label);

                // BACKWARD PROPAGATE
                network.backwards(forward.outputLayer, learningRate);

                if(index % 100 == 0) {
                    System.out.println(" step: "+ index + " loss: " + forward.loss / 100.0 + " accuracy: " + forward.accuracy);
                    accuracySum += forward.accuracy;
                }
                index++;
            }
            System.out.println("average accuracy:- "+ accuracySum / totalRows + "%");

        }
    }

    public static class TestingImageProcessor implements Consumer<HashMap<Integer, float[][]>> {

        public Integer totalRows = 0;
        public Integer batches = 0;

        @Override
        public void accept(HashMap<Integer, float[][]> images) {
            totalRows += images.size();
            batches += 1;

            // DO TESTING WITH IMAGES HERE


        }
    }
}