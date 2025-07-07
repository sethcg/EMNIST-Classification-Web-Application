package emnist.app.service;

import java.io.ByteArrayInputStream;

import java.util.Base64;
import java.util.HashMap;

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
    void read(String uri, Integer batchSize, Consumer<HashMap<Integer, float[][]>> processBatch) {
        ScanOptions options = new ScanOptions(batchSize);
        try (
                BufferAllocator allocator = new RootAllocator();
                DatasetFactory datasetFactory = new FileSystemDatasetFactory(allocator, NativeMemoryPool.getDefault(), FileFormat.PARQUET, uri);
                Dataset dataset = datasetFactory.finish();
                Scanner scanner = dataset.newScan(options);
                ArrowReader reader = scanner.scanBatches()) {
            while (reader.loadNextBatch()) {
                try (VectorSchemaRoot root = reader.getVectorSchemaRoot()) {
                    HashMap<Integer, float[][]> batchResults = new HashMap<Integer, float[][]>();
                    for (int i = 0; i < root.getRowCount(); i++) {
                        String objectString = root.getVector("image").getObject(i).toString();
                        String base64String = objectString.replaceAll("\\{|\\}|\\\"", "").split(":")[1];
                        byte[] bytes = Base64.getDecoder().decode(base64String);
                        ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);
                        BufferedImage image = ImageIO.read(imageStream);             
                        int height = image.getHeight();
                        int width = image.getWidth();
                        float[][] pixels = new float[height][width];
                        for (int row = 0; row < height; row++) {
                            for (int col = 0; col < width; col++) {
                                int colorValue = image.getRGB(row, col);
                                int r = (colorValue & 0x00ff0000) >> 16;
                                int g = (colorValue & 0x0000ff00) >> 8;
                                int b = (colorValue & 0x000000ff);
                                Boolean hasColor = (r + g + b) != 0;
                                pixels[row][col] = hasColor ? 1 : 0;
                            }
                        }
                        // GET LABEL
                        BigIntVector labelVector = (BigIntVector) root.getVector("label");
                        Integer label = Long.valueOf(labelVector.get(i)).intValue();

                        batchResults.put(label, pixels);
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
}