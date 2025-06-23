package emnist.app.service;

public class Convolution {
 
    
    public static int[][] ConvolveImage(int[][] image, int width, int height) {
		int KERNAL_SIZE = 3;
		int[][] KERNAL = {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}};

		int row_start = (int) Math.floor(KERNAL_SIZE / 2);
		int row_end = height - KERNAL_SIZE + 2;

		int col_start = (int) Math.floor(KERNAL_SIZE / 2);
		int col_end = width - KERNAL_SIZE + 2;

		// Create a new image filled with zeros, smaller than the original by the size of the kernel
		int[][] new_image = new int[(height - KERNAL_SIZE + 1)][(width - KERNAL_SIZE + 1)];
		for (int x = row_start; x < row_end; x++) {
			for (int y = col_start; y < col_end; y++) {
				// Convolve for each point in the area defined by the borders
				new_image[y - row_start][x - col_start] = Convolution.Convolve(image, KERNAL, KERNAL_SIZE, x, y);
			}
		}

        return new_image;
    }


	public static int Convolve(int[][] image, int[][] filter, int kernelSize, int rowOffset, int columnOffset) {
		int sum = 0;

		// Determine the starting point of the kernel
		int kernelOffset = (int) -Math.floor(kernelSize / 2);

		// Convolution: multiply the image and filter elements and sum them
		for (int x = 0; x < kernelSize; x++) {
			for (int y = 0; y < kernelSize; y++) {
				sum += image[columnOffset + kernelOffset + x][rowOffset + kernelOffset + y] * filter[x][y];
			}
		}

		// Return the result of convolution for a given point
		return sum;
	}

}
