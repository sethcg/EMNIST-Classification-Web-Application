package emnist.app.service.network;

import emnist.app.service.helper.Matrix;

public class MaxPooling {

    public float[][][] input;

    public float[][][] output;

    public float[][] max_pool(float[][] image) {
        float[][] pool = new float[image.length / 2][image[0].length / 2];
        for (int i = 0; i < pool.length - 1; i++) {
            for (int j = 0; j < pool[0].length - 1; j++) {
                pool[i][j] = Matrix.getMatrixMaximum(Matrix.getSubMatrix(image, i * 2, i * 2 + 1, j * 2, j * 2 + 1));
            }
        }
        return pool;
    }

    public float[][][] propagateForwards(float[][][] dta) {
        input = dta;
        float[][][] result = new float[dta.length][dta[0].length][dta[0][0].length];
        for (int k = 0; k < dta.length; k++) {
            float[][] res = max_pool(dta[k]);
            result[k] = res;
        }
        output = result;
        return result;
    }

    public float[][][] propagateBackwards(float[][][] d_L_d_out) {
        float[][][] d_L_d_input = new float[input.length][input[0].length][input[0][0].length];
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[0].length; j++) {
                for (int k = 0; k < output[0][0].length; k++) {
                    float[][] region = Matrix.getSubMatrix(input[i], j * 2, j * 2 + 1, k * 2, k * 2 + 1);
                    for (int m = 0; m < region.length; m++) {
                        for (int n = 0; n < region[0].length; n++) {
                            if (Math.abs(output[i][j][k] - region[m][n]) < 0.00000001) {
                                d_L_d_input[i][j * 2 + m][k * 2 + n] = d_L_d_out[i][j][k];
                            }
                        }
                    }
                }
            }
        }
        return d_L_d_input;
    }
}
