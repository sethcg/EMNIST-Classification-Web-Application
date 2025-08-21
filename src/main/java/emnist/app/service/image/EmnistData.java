package emnist.app.service.image;

public class EmnistData {

    public int batchSize, epochSize;

    public static enum EmnistEnum {
        TRAIN, TEST
    }

    public EmnistEnum dataType;
    public EmnistBatch emnistBatch;

    public EmnistData(int epochSize, EmnistEnum dataType) {
        this.batchSize = 1000;
        this.epochSize = epochSize;
        this.dataType = dataType;
    }

    public static class EmnistImage {
        public Integer label;
        public float[][] image;

        public EmnistImage(Integer label, float[][] image) {
            this.label = label;
            this.image = image;
        }
    }

    public static class EmnistBatch {
        public int epochNum;
        public int batchNum;
        public EmnistImage[] images;
        public boolean isLastBatch = false;

        public EmnistBatch(int epochNum, int batchNum, EmnistImage[] images, boolean isLastBatch) {
            this.epochNum = epochNum;
            this.batchNum = batchNum;
            this.images = images;
            this.isLastBatch = isLastBatch;
        }
    }

}
