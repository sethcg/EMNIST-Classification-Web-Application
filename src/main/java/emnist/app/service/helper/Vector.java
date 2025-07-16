package emnist.app.service.helper;

import emnist.app.service.helper.FunctionHelper.Function;

public class Vector {
    
    public static float[][] getVectorArrayOfZero(Integer size) {
        float[][] outputVectors = new float[1][size];
        Function<Integer, Void> function = (index) -> { 
            outputVectors[0][index] = 0.0f;
            return null;
        };
        FunctionHelper.executeFunction(size, function);
        return outputVectors;
    }

    public static int getVectorArrayMaximumIndex(float[][] vectors) {
        int max = 0;
        for (int index = 0; index < vectors[0].length; index++) {
            max = vectors[0][max] < vectors[0][index] ? index : max;
        }
        return max;
    }

    public static float getVectorArraySum(float[][] vectors) {
        float sum = 0;
        for (int i = 0; i < vectors[0].length; i++) {
            sum += vectors[0][i];
        }
        return sum;
    }

    public static float[][] getElementWiseExponentiationVectorArray(float[][] vectors) {
        int size = vectors[0].length;
        float[][] outputVector = new float[1][size];
        Function<Integer, Void> function = (index) -> { 
            outputVector[0][index] = (float) Math.exp(vectors[0][index]);
            return null;
        };
        FunctionHelper.executeFunction(size, function);
        return outputVector;
    }

    public static float[][] getElementWiseScaledVectorArray(float[][] vectors, float scale) {
        int size = vectors[0].length;
        float[][] outputVector = new float[1][size];
        Function<Integer, Void> function = (index) -> { 
            outputVector[0][index] = (float) vectors[0][index] * scale;
            return null;
        };
        FunctionHelper.executeFunction(size, function);
        return outputVector;
    }

}
