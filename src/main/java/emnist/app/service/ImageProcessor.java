package emnist.app.service;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class ImageProcessor {

    static class TrainingImageProcessor implements Consumer<HashMap<Integer, float[][]>> {

        public Integer totalRows = 0;
        public Integer batches = 0;

        @Override
        public void accept(HashMap<Integer, float[][]> images) {
            totalRows += images.size();
            batches += 1;

            // DO TRAINING WITH IMAGES HERE
            for(Entry<Integer, float[][]> entry : images.entrySet()) {
                Integer key = entry.getKey();
                System.out.println(key);
                Service.printMatrix(entry.getValue());
            }

        }
    }

    static class TestingImageProcessor implements Consumer<HashMap<Integer, float[][]>> {

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