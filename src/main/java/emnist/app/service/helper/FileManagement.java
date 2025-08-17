package emnist.app.service.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import emnist.app.service.network.NetworkStats;

public class FileManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagement.class);

    private static final String DATA_DIRECTORY = System.getProperty("user.dir") + "/src/main/java/emnist/app/data/network/";
    private static final String FILTERS_FILENAME = DATA_DIRECTORY + "Filters.ser";
    private static final String WEIGHTS_FILENAME = DATA_DIRECTORY + "Weights.ser";
    private static final String BIAS_FILENAME = DATA_DIRECTORY + "Bias.ser";

    public static final String TRAINING_STATISTICS_FILENAME = DATA_DIRECTORY + "TrainingStats.ser";
    public static final String TESTING_STATISTICS_FILENAME = DATA_DIRECTORY + "TestingStats.ser";

    public static void RemoveFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) file.delete();
    }

    public static class Filters {

        public static boolean hasFile() {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILTERS_FILENAME))) {
                return (float[][][]) inputStream.readObject() != null;
            } catch (Exception exception) {
                return false;
            }
        }

        public static void saveMatrix(float[][][] filters) {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) directory.mkdir();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILTERS_FILENAME, false))) {
                outputStream.writeObject(filters);
            } catch (Exception exception) {
                LOGGER.error(String.format("[SAVING FILTERS FAILED] %s", exception.getMessage()));
                LOGGER.debug(String.format("[FILE PATH] %s", FILTERS_FILENAME));
            }
        }

        public static float[][][] getMatrixFromFile() {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILTERS_FILENAME))) {
                return (float[][][]) inputStream.readObject();
            } catch (Exception exception) {
                return null;
            }
        }

    }

    public static class Weights {

        public static boolean hasFile() {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(WEIGHTS_FILENAME))) {
                return (float[][]) inputStream.readObject() != null;
            } catch (Exception exception) {
                return false;
            }
        }

        public static void saveMatrix(float[][] weights) {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) directory.mkdir();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(WEIGHTS_FILENAME, false))) {
                outputStream.writeObject(weights);
            } catch (Exception exception) {
                LOGGER.error(String.format("[SAVING WEIGHTS FAILED] %s", exception.getMessage()));
                LOGGER.debug(String.format("[FILE PATH] %s", WEIGHTS_FILENAME));
            }
        }

        public static float[][] getMatrixFromFile() {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(WEIGHTS_FILENAME))) {
                return (float[][]) inputStream.readObject();
            } catch (Exception exception) {
                return null;
            }
        }

    }

    public static class Bias {

        public static boolean hasFile() {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(BIAS_FILENAME))) {
                return (float[][]) inputStream.readObject() != null;
            } catch (Exception exception) {
                return false;
            }
        }

        public static void saveMatrix(float[][] bias) {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) directory.mkdir();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(BIAS_FILENAME, false))) {
                outputStream.writeObject(bias);
            } catch (IOException exception) {
                LOGGER.error(String.format("[SAVING BIAS FAILED] %s", exception.getMessage()));
                LOGGER.debug(String.format("[FILE PATH] %s", BIAS_FILENAME));
            }
        }

        public static float[][] getMatrixFromFile() {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(BIAS_FILENAME))) {
                return (float[][]) inputStream.readObject();
            } catch (Exception exception) {
                return null;
            }
        }

    }

    public static class Statistics {

        public static boolean hasFile(String fileName) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
                return (NetworkStats) inputStream.readObject() != null;
            } catch (Exception exception) {
                return false;
            }
        }

        public static void saveStatistics(NetworkStats stats, String fileName) {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) directory.mkdir();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName, false))) {
                outputStream.writeObject(stats);
            } catch (IOException exception) {
                LOGGER.error(String.format("[SAVING STATISTICS FAILED] %s", exception.getMessage()));
                LOGGER.debug(String.format("[FILE PATH] %s", fileName));
            }
        }

        public static NetworkStats getObjectFromFile(String fileName) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
                return (NetworkStats) inputStream.readObject();
            } catch (Exception exception) {
                return null;
            }
        }

    }

}