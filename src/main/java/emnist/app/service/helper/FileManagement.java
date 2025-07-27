package emnist.app.service.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileManagement {

    private static final String DATA_DIRECTORY = System.getProperty("user.dir") + "/src/main/java/emnist/app/data/network/";
    private static final String FILTERS_FILENAME =  DATA_DIRECTORY + "filters.ser";
    private static final String WEIGHTS_FILENAME =  DATA_DIRECTORY + "weights.ser";
    private static final String BIAS_FILENAME =  DATA_DIRECTORY + "bias.ser";

    public static class Filters {

        public static void saveMatrix(float[][][] filters) {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) directory.mkdir();
            
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILTERS_FILENAME, false))) {
                outputStream.writeObject(filters);
            } catch (IOException exception) {
                // TODO: MORE VERBOSE EXCEPTION HANDLING
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

        public static void saveMatrix(float[][] weights) {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) directory.mkdir();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(WEIGHTS_FILENAME, false))) {
                outputStream.writeObject(weights);
            } catch (Exception exception) {
                // TODO: MORE VERBOSE EXCEPTION HANDLING
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

        public static void saveMatrix(float[][] bias) {
            File directory = new File(DATA_DIRECTORY);
            if (!directory.exists()) directory.mkdir();

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(BIAS_FILENAME, false))) {
                outputStream.writeObject(bias);
            } catch (IOException exception) {
                // TODO: MORE VERBOSE EXCEPTION HANDLING
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

    
}