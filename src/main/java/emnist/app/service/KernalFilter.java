package emnist.app.service;

public class KernalFilter {

    public static float[][] PREWITT_HORIZONTAL_FILTER = new float[][] {
        { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 }
    };

    public static float[][] PREWITT_VERTICAL_FILTER = new float[][] {
        { 1, 1, 1 }, { 0, 0, 0 }, { -1, -1, -1 }
    };

}
