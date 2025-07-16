package emnist.app.service.network;

import emnist.app.service.helper.Matrix;
import emnist.app.service.helper.Vector;

public class SoftMax {
    
    public float[][] weights;

    public float[][] input;
    
    public float[][] bias;
    
    public float[][] output;

    public SoftMax(int input, int output) {
        weights = Matrix.getElementWiseScaling(Matrix.getRandomizedMatrix(input, output), 1.0f / input);
        bias = Vector.getVectorArrayOfZero(10);
    }

    public float[][] propagateForwards(float[][][] input) {
        float[][] flattenedInput = Matrix.getFlattenedMatrix(input);
        output = new float[1][bias.length];
        output = Matrix.getElementWiseAddition(Matrix.getMultipliedMatrix(flattenedInput, weights), bias);
        float[][] totals = Vector.getElementWiseExponentiationVectorArray(output);
        float inv_activation_sum = 1 / Vector.getVectorArraySum(totals);
        this.input = flattenedInput;
        return Vector.getElementWiseScaledVectorArray(totals, inv_activation_sum);
    }

    public float[][][] propagateBackwards(float[][] d_L_d_out, float learning_rate) {
        //gradient of loss w.r.t. the total probabilites of the softmax layer.
        float[][] d_L_d_t = new float[1][d_L_d_out[0].length];
        //repeat softmax probability computations (caching can be used to avoid this.)
        float[][] t_exp = Vector.getElementWiseExponentiationVectorArray(output);
        float S = Vector.getVectorArraySum(t_exp);
        float[][] d_L_d_inputs = null;
        
        for (int i = 0; i < d_L_d_out[0].length; i++) {
            float grad = d_L_d_out[0][i];
            if (grad == 0) {
                continue;
            }
            //gradient of the output layer w.r.t. the totals [1] X [10]
            float[][] d_out_d_t = Vector.getElementWiseScaledVectorArray(t_exp, -t_exp[0][i] / (S * S));
            d_out_d_t[0][i] = t_exp[0][i] * (S - t_exp[0][i]) / (S * S);
            
            d_L_d_t = Matrix.getElementWiseScaling(d_out_d_t, grad); 
            //gradient of totals w.r.t weights -- [1342] X [1]
            float[][] d_t_d_weight = Matrix.getTransposedMatrix(input);
            //gradient of totals w.r.t inputs -- [1342] X [10] 
            float[][] d_t_d_inputs = weights;
            //gradient of Loss w.r.t. weights ---> chain rule 
            //        [1342] X [10] = [1342] X [1] * [1] X [10]
            float[][] d_L_d_w = Matrix.getMultipliedMatrix(d_t_d_weight, d_L_d_t);
            //gradient of Loss w.r.t. inputs ---> chain rule
            // [1342] X [1]      [1342] X [10]    *   [10] X [1](transposed)
            d_L_d_inputs = Matrix.getMultipliedMatrix(d_t_d_inputs, Matrix.getTransposedMatrix(d_L_d_t));
            //gradient of loss w.r.t. bias
            float[][] d_L_d_b = d_L_d_t;
            //update the weight and bias matrices.
            weights = Matrix.getElementWiseAddition(Matrix.getElementWiseScaling(d_L_d_w, -learning_rate), weights);
            bias = Matrix.getElementWiseAddition(Matrix.getElementWiseScaling(d_L_d_b, -learning_rate), bias);
        }
        return Matrix.getReshapedMatrix(Matrix.getTransposedMatrix(d_L_d_inputs), 8, 13, 13);
    }
}
