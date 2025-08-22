package emnist.app.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import emnist.app.service.helper.FileManagement;
import emnist.app.service.image.EmnistData;
import emnist.app.service.image.EmnistData.EmnistEnum;
import emnist.app.service.image.ParquetFileReader;
import emnist.app.service.network.Network;
import emnist.app.service.network.NetworkStats;

@Service
public class NetworkService {

    private static final String TRAINING_DATA_URI = "file:/" + FileManagement.DATA_DIRECTORY + "train.parquet";
    private static final String TESTING_DATA_URI = "file:/" + FileManagement.DATA_DIRECTORY + "test.parquet";

    private Network network;
    
    private ParquetFileReader reader;

    public NetworkService() {
        this.network = new Network();
        this.reader = new ParquetFileReader();
    }

    public void train() {
        EmnistData trainingData = new EmnistData(60000, EmnistEnum.TRAIN);

        // RESET THE NETWORK SAVED FILTER, WEIGHT, BIAS DATA
        network.reset();
        
        reader.read(TRAINING_DATA_URI, 1, trainingData, network);
    }

    public void test() {
        EmnistData testingData = new EmnistData(10000, EmnistEnum.TEST);

        // RESET THE NETWORK SAVED TESTING STATS
        network.testingStats = new NetworkStats();
        
        reader.read(TESTING_DATA_URI, 1, testingData, network);
    }

    public int predict(float[][] image) {
        return network.predict(image);
    }

    public HashMap<String, String> getTrainingStatistics() {
        if (FileManagement.hasNetwork() && FileManagement.hasFile(EmnistEnum.TRAIN)) {
            NetworkStats networkStats = FileManagement.getSavedStats(EmnistEnum.TRAIN);
            return networkStats.getMappedObject(true);
        } else {
            NetworkStats networkStats = new NetworkStats();
            return networkStats.getMappedObject(false);
        }
    }

    public HashMap<String, String> getTestingStatistics() {
        if (FileManagement.hasNetwork() && FileManagement.hasFile(EmnistEnum.TEST)) {
            NetworkStats networkStats = FileManagement.getSavedStats(EmnistEnum.TEST);
            return networkStats.getMappedObject(true);
        } else {
            NetworkStats networkStats = new NetworkStats();
            return networkStats.getMappedObject(false);
        }
    }

}
