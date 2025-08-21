package emnist.app.service.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import emnist.app.service.image.EmnistData.EmnistEnum;
import emnist.app.service.network.NetworkStats;

public class FileManagement {

    public static final String DATA_DIRECTORY = System.getProperty("user.dir") + "/src/main/java/emnist/app/data/";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManagement.class);
    
    private static final String NETWORK_DATA_DIRECTORY = DATA_DIRECTORY + "network/";
    private static final String FILTERS_FILENAME = NETWORK_DATA_DIRECTORY + "Filters.ser";
    private static final String WEIGHTS_FILENAME = NETWORK_DATA_DIRECTORY + "Weights.ser";
    private static final String BIAS_FILENAME = NETWORK_DATA_DIRECTORY + "Bias.ser";

    private static final String TRAINING_STATISTICS_FILENAME = NETWORK_DATA_DIRECTORY + "TrainingStats.ser";
    private static final String TESTING_STATISTICS_FILENAME = NETWORK_DATA_DIRECTORY + "TestingStats.ser";

    public static boolean HasNetwork() {
        return Filters.hasFile() && Weights.hasFile() && Bias.hasFile();
    }

    private static void CreateSaveDirectory() {
        File directory = new File(NETWORK_DATA_DIRECTORY);
        if (!directory.exists()) directory.mkdir();
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
            FileManagement.CreateSaveDirectory();

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
            FileManagement.CreateSaveDirectory();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(WEIGHTS_FILENAME, false))) {
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
            FileManagement.CreateSaveDirectory();

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

        private static String getFilename(EmnistEnum dataType) {
            return (dataType == EmnistEnum.TRAIN) 
                ? TRAINING_STATISTICS_FILENAME 
                : TESTING_STATISTICS_FILENAME;
        }

        public static void removeFile(EmnistEnum dataType) {
            String filename = getFilename(dataType);
            File file = new File(filename);
            if (!file.exists()) file.delete();
        }

        public static boolean hasFile(EmnistEnum dataType) {
            String filename = getFilename(dataType);
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
                return (NetworkStats) inputStream.readObject() != null;
            } catch (Exception exception) {
                return false;
            }
        }

        public static void saveStatistics(NetworkStats stats, EmnistEnum dataType) {
            FileManagement.CreateSaveDirectory();

            String filename = getFilename(dataType);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename, false))) {
                outputStream.writeObject(stats);
            } catch (IOException exception) {
                LOGGER.error(String.format("[SAVING STATISTICS FAILED] %s", exception.getMessage()));
                LOGGER.debug(String.format("[FILE PATH] %s", filename));
            }
        }

        public static NetworkStats getObjectFromFile(EmnistEnum dataType) {
            String filename = getFilename(dataType);
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
                return (NetworkStats) inputStream.readObject();
            } catch (Exception exception) {
                return null;
            }
        }

    }

}