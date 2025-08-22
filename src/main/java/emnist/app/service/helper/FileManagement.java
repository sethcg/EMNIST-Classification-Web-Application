package emnist.app.service.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import emnist.app.service.image.EmnistData.EmnistEnum;
import emnist.app.service.network.NetworkStats;

public class FileManagement {

    public static final String DATA_DIRECTORY = System.getProperty("user.dir") + "/src/main/java/emnist/app/data/";

    private static final String NETWORK_DATA_DIRECTORY = DATA_DIRECTORY + "network/";
    private static final String FILTERS_FILENAME = NETWORK_DATA_DIRECTORY + "Filters.ser";
    private static final String WEIGHTS_FILENAME = NETWORK_DATA_DIRECTORY + "Weights.ser";
    private static final String BIAS_FILENAME = NETWORK_DATA_DIRECTORY + "Bias.ser";
    private static final String TRAINING_FILENAME = NETWORK_DATA_DIRECTORY + "TrainingStats.ser";
    private static final String TESTING_FILENAME = NETWORK_DATA_DIRECTORY + "TestingStats.ser";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagement.class);

    public static boolean hasNetwork() {
        return hasFile(FILTERS_FILENAME, float[][][].class) &&
                hasFile(WEIGHTS_FILENAME, float[][].class) &&
                hasFile(BIAS_FILENAME, float[][].class);
    }

    private static String getFilename(EmnistEnum dataType) {
        return (dataType == EmnistEnum.TRAIN) ? TRAINING_FILENAME : TESTING_FILENAME;
    }

    private static <T> boolean hasFile(String filename, Class<T> classObject) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return classObject.cast(inputStream.readObject()) != null;
        } catch (Exception exception) {
            return false;
        }
    }

    private static <T> void saveFile(String filename, T saveObject) {
        File directory = new File(NETWORK_DATA_DIRECTORY);
        if (!directory.exists()) directory.mkdir();

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename, false))) {
            outputStream.writeObject(saveObject);
        } catch (Exception exception) {
            LOGGER.error(String.format("[SAVING FILTERS FAILED] %s", exception.getMessage()));
            LOGGER.debug(String.format("[FILE PATH] %s", filename));
        }
    }

    private static <T> T getObjectFromFile(String filename, Class<T> classObject) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return classObject.cast(inputStream.readObject());
        } catch (Exception exception) {
            return null;
        }
    }

    public static void saveFilters(float[][][] filters) {
        saveFile(FILTERS_FILENAME, filters);
    }

    public static void saveWeights(float[][] weights) {
        saveFile(WEIGHTS_FILENAME, weights);
    }

    public static void saveBias(float[][] bias) {
        saveFile(BIAS_FILENAME, bias);
    }

    public static float[][][] getSavedFilters() {
        return (float[][][]) getObjectFromFile(FILTERS_FILENAME, float[][][].class);
    }

    public static float[][] getSavedWeights() {
        return (float[][]) getObjectFromFile(WEIGHTS_FILENAME, float[][].class);
    }

    public static float[][] getSavedBias() {
        return (float[][]) getObjectFromFile(BIAS_FILENAME, float[][].class);
    }

    public static boolean hasFile(EmnistEnum dataType) {
        String filename = getFilename(dataType);
        return hasFile(filename, NetworkStats.class);
    }

    public static void saveStats(NetworkStats networkStats, EmnistEnum dataType) {
        String filename = getFilename(dataType);
        saveFile(filename, networkStats);
    }

    public static void deleteStats(EmnistEnum dataType) {
        String filename = getFilename(dataType);
        File file = new File(filename);
        if (!file.exists())
            file.delete();
    }

    public static NetworkStats getSavedStats(EmnistEnum dataType) {
        String filename = getFilename(dataType);
        return (NetworkStats) getObjectFromFile(filename, NetworkStats.class);
    }

}