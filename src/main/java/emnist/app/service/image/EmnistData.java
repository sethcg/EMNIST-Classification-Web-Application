package emnist.app.service.image;

public class EmnistData {

    public static int batchSize = 1000;
    public static int epochSize = 60000;

    public static enum EmnistEnum { TRAIN, TEST }

    public EmnistEnum dataType;
    public EmnistBatch emnistBatch;

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
        public int rows;
        public int epochNum;
        public int batchNum;
        public EmnistImage[] images;
        public boolean isLastBatch = false;

        public EmnistBatch(int rows, int epochNum, int batchNum, EmnistImage[] images, boolean isLastBatch) {
            this.rows = rows;
            this.epochNum = epochNum;
            this.batchNum = batchNum;
            this.images = images;
            this.isLastBatch = isLastBatch;
        }
    }

}
