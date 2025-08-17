package emnist.app.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import emnist.app.service.helper.FileManagement;
import emnist.app.service.image.EmnistData;
import emnist.app.service.image.EmnistData.EmnistEnum;
import emnist.app.service.image.EmnistData.EmnistImage;
import emnist.app.service.image.ParquetFileReader;
import emnist.app.service.network.Network;
import emnist.app.service.network.NetworkStats;

@Service
public class NetworkService {

    public static final String DATA_DIRECTORY = System.getProperty("user.dir") + "/src/main/java/emnist/app/data/";

    private static final String TRAINING_DATA_URI = "file:/" + DATA_DIRECTORY + "train.parquet";
    private static final String TESTING_DATA_URI = "file:/" + DATA_DIRECTORY + "test.parquet";

    private static final Network network = new Network();

    public void train() {
        EmnistData.epochSize = 60000;
        EmnistData emnistData = new EmnistData(EmnistEnum.TRAIN);

        // RESET THE NETWORK SAVED FILTER, WEIGHT, BIAS DATA
        network.reset();

        ParquetFileReader reader = new ParquetFileReader();
        reader.read(TRAINING_DATA_URI, 1, emnistData, network);
    }

    public void test() {
        EmnistData.epochSize = 10000;
        EmnistData emnistData = new EmnistData(EmnistEnum.TEST);

        // RESET THE NETWORK SAVED TESTING STATS
        network.testingStats = new NetworkStats();

        ParquetFileReader reader = new ParquetFileReader();
        reader.read(TESTING_DATA_URI, 1, emnistData, network);
    }

    public int predict(float[][] image) {
        EmnistImage emnistImage = new EmnistImage(-1, image);
        return network.predict(emnistImage);
    }

    public HashMap<String, String> getTrainingStatistics() {
        final String fileName = FileManagement.TRAINING_STATISTICS_FILENAME;
        if (    FileManagement.Filters.hasFile() &&
                FileManagement.Weights.hasFile() &&
                FileManagement.Bias.hasFile() &&
                FileManagement.Statistics.hasFile(fileName)) 
        {
            NetworkStats stats = FileManagement.Statistics.getObjectFromFile(fileName);
            return stats.getMappedObject(true);
        } else {
            return new NetworkStats().getMappedObject(false);
        }
    }

    public HashMap<String, String> getTestingStatistics() {
        final String fileName = FileManagement.TESTING_STATISTICS_FILENAME;
        if (    FileManagement.Filters.hasFile() &&
                FileManagement.Weights.hasFile() &&
                FileManagement.Bias.hasFile() &&
                FileManagement.Statistics.hasFile(fileName)) 
        {
            NetworkStats stats = FileManagement.Statistics.getObjectFromFile(fileName);
            return stats.getMappedObject(true);
        } else {
            return new NetworkStats().getMappedObject(false);
        }
    }

}
