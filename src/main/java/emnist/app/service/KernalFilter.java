package emnist.app.service;

public class KernalFilter {

    // PREWITT FILTERS: basic edge detection filter
    public final static float[][] PREWITT_HORIZONTAL_FILTER = new float[][] {
        { -1, 0, 1 },
        { -1, 0, 1 },
        { -1, 0, 1 }
    };

    public final static float[][] PREWITT_VERTICAL_FILTER = new float[][] {
        { 1, 1, 1 },
        { 0, 0, 0 },
        { -1, -1, -1 }
    };

    // SOBEL FILTERS: edge detection similar to prewitt filters, but higher edge pixel intensity (sharper)
    public final static float[][] SOBEL_HORIZONTAL_FILTER = new float[][] {
        { -1, 0, 1 },
        { -2, 0, 2 },
        { -1, 0, 1 }
    };

    public final static float[][] SOBEL_VERTICAL_FILTER = new float[][] {
        { 1, 2, 1 },
        { 0, 0, 0 },
        { -1, -2, -1 }
    };

}
