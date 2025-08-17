package emnist.app.service.helper;

import java.util.concurrent.atomic.AtomicReference;

import emnist.app.service.helper.FunctionHelper.Function;

public class Vector {

    public static float[][] getVectorArrayOfZero(Integer size) {
        float[][] outputVectors = new float[1][size];
        Function<Integer> function = (index) -> outputVectors[0][index] = 0.0f;
        FunctionHelper.executeFunction(size, function);
        return outputVectors;
    }

    public static int getVectorArrayMaximumIndex(float[][] vectors) {
        AtomicReference<Integer> maxIndex = new AtomicReference<Integer>(0);
        Function<Integer> function = (index) -> {
            if (vectors[0][maxIndex.get()] < vectors[0][index]) {
                maxIndex.set(index);
            }
        };
        FunctionHelper.executeFunction(vectors[0].length, function);
        return maxIndex.get();
    }

    public static float getVectorArraySum(float[][] vectors) {
        AtomicReference<Float> sum = new AtomicReference<Float>(0.0f);
        Function<Integer> function = (index) -> {
            sum.set(sum.get() + vectors[0][index]);
        };
        FunctionHelper.executeFunction(vectors[0].length, function);
        return sum.get();
    }

    public static float[][] getElementWiseExponentiationVectorArray(float[][] vectors) {
        float[][] outputVector = new float[1][vectors[0].length];
        Function<Integer> function = (index) -> {
            outputVector[0][index] = (float) Math.exp(vectors[0][index]);
        };
        FunctionHelper.executeFunction(vectors[0].length, function);
        return outputVector;
    }

    public static float[][] getElementWiseScaledVectorArray(float[][] vectors, float scale) {
        float[][] outputVector = new float[1][vectors[0].length];
        Function<Integer> function = (index) -> {
            outputVector[0][index] = (float) vectors[0][index] * scale;
        };
        FunctionHelper.executeFunction(vectors[0].length, function);
        return outputVector;
    }

}
