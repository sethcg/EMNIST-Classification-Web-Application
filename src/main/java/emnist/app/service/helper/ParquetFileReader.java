package emnist.app.service.helper;

import java.io.ByteArrayInputStream;

import java.util.Base64;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import org.apache.arrow.dataset.file.FileFormat;
import org.apache.arrow.dataset.file.FileSystemDatasetFactory;
import org.apache.arrow.dataset.jni.NativeMemoryPool;
import org.apache.arrow.dataset.scanner.ScanOptions;
import org.apache.arrow.dataset.scanner.Scanner;
import org.apache.arrow.dataset.source.Dataset;
import org.apache.arrow.dataset.source.DatasetFactory;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BigIntVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowReader;

import java.util.function.Consumer;

public class ParquetFileReader {

    public class ImageItem {
        public Integer label;
        public float[][] image;

        ImageItem(Integer label, float[][] image) {
            this.label = label;
            this.image = image;
        }
    }

    public void read(String uri, Integer batchSize, Consumer<ImageItem[]> processBatch) {
        ScanOptions options = new ScanOptions(batchSize);
        try (
                BufferAllocator allocator = new RootAllocator();
                DatasetFactory datasetFactory = new FileSystemDatasetFactory(allocator, NativeMemoryPool.getDefault(), FileFormat.PARQUET, uri);
                Dataset dataset = datasetFactory.finish();
                Scanner scanner = dataset.newScan(options);
                ArrowReader reader = scanner.scanBatches()) {
            while (reader.loadNextBatch()) {
                try (VectorSchemaRoot root = reader.getVectorSchemaRoot()) {
                    ImageItem[] batchResults = new ImageItem[root.getRowCount()];
                    for (int i = 0; i < root.getRowCount(); i++) {
                        // GET IMAGE
                        String objectString = root.getVector("image").getObject(i).toString();
                        String base64String = objectString.replaceAll("\\{|\\}|\\\"", "").split(":")[1];
                        byte[] bytes = Base64.getDecoder().decode(base64String);
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);
                        BufferedImage image = ImageIO.read(imageStream);             
                        float[][] pixelMatrix = getMatrixFromImage(image);

                        // GET LABEL
                        BigIntVector labelVector = (BigIntVector) root.getVector("label");
                        Integer label = Long.valueOf(labelVector.get(i)).intValue();

                        ImageItem imageItem = new ImageItem(label, pixelMatrix);
                        batchResults[i] = imageItem;
                    }
                    processBatch.accept(batchResults);
                }
            }
        } catch (IllegalArgumentException exception) {
            // IGNORE THIS ERROR WITH THE SCHEMA HAVING EMPTY CHILDREN,
            // THERE IS CURRENTLY NO WORK AROUND, AND IT CAUSES NO REAL ISSUES.
            // exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static float[][] getMatrixFromImage(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        float[][] matrix = new float[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int colorValue = image.getRGB(row, col);
                // NORMALIZE THE COLOR VALUE BETWEEN 0.0 AND 1.0
                matrix[row][col] = ((colorValue >> 16 & 0xff)) / 255.0f;
            }
        }
        return matrix;
    }

}