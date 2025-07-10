package emnist.app.service.network;

public class KernalFilter {

    // PREWITT FILTERS: BASIC EDGE DETECTION FILTER
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

    // SOBEL FILTERS: EDGE DETECTION SIMILAR TO PREWITT FILTERS, BUT HIGHER EDGE PIXEL INTENSITY (SHARPER)
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
