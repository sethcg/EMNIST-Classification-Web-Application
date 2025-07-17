package emnist.app.service.image;

public class EmnistData {

    public static final int BATCH_SIZE = 100;
    public static final int EPOCH_SIZE = 1000;

    public static enum EmnistEnum {
        TRAIN,
        TEST,
    }

    public static class EmnistImage {
        public int label;
        public float[][] image;

        EmnistImage(int label, float[][] image) {
            this.label = label;
            this.image = image;
        }
    }

    public static class EmnistBatch {
        public EmnistImage[] images;

        EmnistBatch(EmnistImage[] images) {
            this.images = images;
        }
    }

    public EmnistBatch[] batches;
    public EmnistEnum dataType;

    EmnistData(EmnistEnum dataType, EmnistBatch[] batches) {
        this.dataType = dataType;
        this.batches = batches;
    }
}
