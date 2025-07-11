package emnist.app.service;

import emnist.app.service.helper.ImageProcessor;
import emnist.app.service.helper.ParquetFileReader;
import emnist.app.service.helper.ImageProcessor.TrainingImageProcessor;
import emnist.app.service.network.ConvolutionalNeuralNetwork;

public class Service {

	// private final static int IMAGE_WIDTH = 28;
	// private final static int IMAGE_HEIGHT = 28;
	// private final static int PIXEL_ARRAY_SIZE = 784;

	public static void GetConvolveImageGrid() 
	{
        // TESTING AREA:
        ConvolutionalNeuralNetwork network = new ConvolutionalNeuralNetwork();

        String uri = "file:/" + System.getProperty("user.dir") + "/src/main/java/emnist/app/data/train.parquet";      
        TrainingImageProcessor processor = new ImageProcessor.TrainingImageProcessor(network);
        ParquetFileReader reader = new ParquetFileReader();
        reader.read(uri, 2, processor);
        
        // System.out.println(processor.totalRows);

        // float[][][] test = ConvolutionalNeuralNetwork.initializeFilters(8, 3, 3);
        // Service.printMatrix(test[0]); // PRINT THE FIRST FILTER
        // Service.printMatrix(test[1]); // PRINT THE SECOND FILTER
        // Service.printMatrix(test[2]);
        // Service.printMatrix(test[3]);
        // Service.printMatrix(test[4]);
        // Service.printMatrix(test[5]);
        // Service.printMatrix(test[6]);
        // Service.printMatrix(test[7]);
	}

	public static void printMatrix(float matrix[][])
    {
        for (int i = 0; i < matrix.length; i++){
			System.out.println();
            for (int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j] >= 1 || matrix[i][j] > 0){
                    System.out.print(String.format("%.1f", matrix[i][j]) + " ");
                } else {
                    System.out.print("  ");
                }
			}
		}
    }

}
