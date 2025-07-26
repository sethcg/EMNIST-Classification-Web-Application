package emnist.app.service.network;

import emnist.app.service.helper.Matrix;
import emnist.app.service.helper.Vector;

public class SoftMax {
    
    public float[][] weights;
    public float[][] bias;
    
    public float[][] flattenedInput;
    public float[][] softMaxTotals;

    public SoftMax(int input, int output) {
        weights = Matrix.getElementWiseScaling(Matrix.getRandomizedMatrix(input, output), 1.0f / input);
        bias = Vector.getVectorArrayOfZero(10);
    }

    public float[][] propagateForwards(float[][][] input) {
        // FLATTEN THE MATRIX FOR EASE OF USE
        flattenedInput = Matrix.getFlattenedMatrix(input);

        // EVALUATE THE TOTAL ACTIVATION VALUES AND CACHE THE TOTALS FOR BACK PROPAGATION.
        float[][] activatedValues = Matrix.getElementWiseAddition(Matrix.getMultipliedMatrix(flattenedInput, weights), bias);
        softMaxTotals = new float[1][bias.length];
        softMaxTotals = Vector.getElementWiseExponentiationVectorArray(activatedValues);
        
        // NORMALIZE THE PROBABILTIES SO THAT THE SUM OF ALL PROBABILITIES IS ONE
        float scale = 1 / Vector.getVectorArraySum(softMaxTotals);
        return Vector.getElementWiseScaledVectorArray(softMaxTotals, scale);
    }

    public float[][][] propagateBackwards(float[][] inputGradientMatrix, float learning_rate) {
        // GRADIENT OF THE LOSS, THE TOTAL PROBABILITIES OF THE SOFTMAX LAYER
        float[][] lossGradient = new float[1][inputGradientMatrix[0].length];
        float softMaxSum = Vector.getVectorArraySum(softMaxTotals);
        float[][] lossInputGradient = null;
        
        for (int i = 0; i < inputGradientMatrix[0].length; i++) {
            float scale = inputGradientMatrix[0][i];
            if (scale == 0) {
                continue;
            }

            // GRADIENT OF THE OUTPUT LAYER
            // [1] x [10]
            float[][] outputLayerGradient = Vector.getElementWiseScaledVectorArray(softMaxTotals, -softMaxTotals[0][i] / (softMaxSum * softMaxSum));
            outputLayerGradient[0][i] = softMaxTotals[0][i] * (softMaxSum - softMaxTotals[0][i]) / (softMaxSum * softMaxSum);
            
            lossGradient = Matrix.getElementWiseScaling(outputLayerGradient, scale); 

            // GRADIENT OF THE TOTALS WEIGHTS
            // [1342] x [1]
            float[][] totalWeightGradient = Matrix.getTransposedMatrix(flattenedInput);

            // GRADIENT OF THE LOSS WEIGHTS -> CHAIN RULE
            // [1342] x [10] = [1342] x [1] * [1] x [10]
            float[][] lossWeightGradient = Matrix.getMultipliedMatrix(totalWeightGradient, lossGradient);

            // GRADIENT OF THE LOSS INPUTS -> CHAIN RULE
            // [1342] x [1] = [1342] x [10] * [10] x [1] (TRANSPOSED)
            lossInputGradient = Matrix.getMultipliedMatrix(weights, Matrix.getTransposedMatrix(lossGradient));

            // UPDATE WEIGHTS AND BIAS
            weights = Matrix.getElementWiseAddition(Matrix.getElementWiseScaling(lossWeightGradient, -learning_rate), weights);
            bias = Matrix.getElementWiseAddition(Matrix.getElementWiseScaling(lossGradient, -learning_rate), bias);
        }
        return Matrix.getReshapedMatrix(Matrix.getTransposedMatrix(lossInputGradient), 8, 13, 13);
    }
}
