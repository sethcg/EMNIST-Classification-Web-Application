package emnist.app.service;

import emnist.app.service.image.ParquetFileReader;
import emnist.app.service.image.EmnistData.EmnistEnum;
import emnist.app.service.network.ConvolutionalNeuralNetwork;

public class Service {

	public static void TestNetworkTraining() 
	{
        // TRAIN NETWORK
        ConvolutionalNeuralNetwork network = new ConvolutionalNeuralNetwork();
        String uri = "file:/" + System.getProperty("user.dir") + "/src/main/java/emnist/app/data/train.parquet";      
        ParquetFileReader reader = new ParquetFileReader();
        reader.read(uri, 2, EmnistEnum.TRAIN, network);
	}

	// public static void printMatrix(float matrix[][])
    // {
    //     for (int i = 0; i < matrix.length; i++){
	// 		System.out.println();
    //         for (int j = 0; j < matrix[i].length; j++){
    //             if(matrix[i][j] >= 1 || matrix[i][j] > 0){
    //                 System.out.print(String.format("%.1f", matrix[i][j]) + " ");
    //             } else {
    //                 System.out.print("  ");
    //             }
	// 		}
	// 	}
    // }

}
