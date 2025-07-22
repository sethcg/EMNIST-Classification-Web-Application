package emnist.app.service.image;

public class EmnistData {

    public static final int BATCH_SIZE = 100;
    public static final int EPOCH_SIZE = 1000;

    public static enum EmnistEnum { TRAIN, TEST }

    public EmnistEnum dataType;
    public EmnistBatch[] batches;

    public EmnistData(EmnistEnum dataType) {
        this.dataType = dataType;
    }

    public static class EmnistImage {
        public int label;
        public float[][] image;

        public EmnistImage(int label, float[][] image) {
            this.label = label;
            this.image = image;
        }
    }

    public static class EmnistBatch {
        public EmnistImage[] images;

        public EmnistBatch(EmnistImage[] images) {
            this.images = images;
        }
    }

}
