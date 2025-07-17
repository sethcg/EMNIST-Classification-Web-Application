package emnist.app.service.helper;

import java.io.ByteArrayInputStream;

import java.util.Base64;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import org.apache.arrow.c.ArrowArray;
import org.apache.arrow.c.ArrowSchema;
import org.apache.arrow.c.CDataDictionaryProvider;
import org.apache.arrow.c.Data;
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

    private static final int BATCH_SIZE = 100;
    private static final int EPOCH_SIZE = 1000;

    public class ImageItem {
        public Integer label;
        public float[][] image;

        ImageItem(Integer label, float[][] image) {
            this.label = label;
            this.image = image;
        }
    }

    public void read(String uri, int numEpoch, Consumer<ImageItem[]> processBatch) {
        int epochNum = 1;
        int batchNum = 1;
        int totalBatchSize = 0;

        try (   BufferAllocator allocator = new RootAllocator();
                DatasetFactory datasetFactory = new FileSystemDatasetFactory(allocator, NativeMemoryPool.getDefault(), FileFormat.PARQUET, uri);
                Dataset dataset = datasetFactory.finish();
                Scanner scanner = dataset.newScan(new ScanOptions(BATCH_SIZE));
                ArrowReader reader = scanner.scanBatches();
                ArrowSchema consumerArrowSchema = ArrowSchema.allocateNew(allocator)) {

            // PRODUCER FILL CONSUMER SCHEMA STRUCTURE
            Data.exportSchema(allocator, reader.getVectorSchemaRoot().getSchema(), reader, consumerArrowSchema);

            // Consumer loads it as an empty vector schema root
            try (   CDataDictionaryProvider consumerDictionaryProvider = new CDataDictionaryProvider();
                    VectorSchemaRoot consumerRoot = Data.importVectorSchemaRoot(allocator, consumerArrowSchema, consumerDictionaryProvider)) {
                
                while (reader.loadNextBatch()) {          
                    if(totalBatchSize % EPOCH_SIZE == 0) {
                        if(epochNum > numEpoch) return;
                        System.out.println("--- Epoch " + epochNum++ + " ---");
                    }
                    try (ArrowArray consumerArray = ArrowArray.allocateNew(allocator)) {

                        // PRODUCER EXPORT DATA TO "consumerRoot"
                        Data.exportVectorSchemaRoot(allocator, reader.getVectorSchemaRoot(), reader, consumerArray);

                        // PRODUCER IMPORT NEXT BATCH INTO "consumerRoot"
                        Data.importIntoVectorSchemaRoot(allocator, consumerArray, consumerRoot, consumerDictionaryProvider);

                        totalBatchSize += consumerRoot.getRowCount();
                        System.out.println("Batch[" + batchNum++ + "]: " + " Rows: " + consumerRoot.getRowCount());

                        ImageItem[] batchResults = new ImageItem[consumerRoot.getRowCount()];
                        for (int i = 0; i < consumerRoot.getRowCount(); i++) {
                            // GET IMAGE
                            String objectString = consumerRoot.getVector("image").getObject(i).toString();
                            String base64String = objectString.replaceAll("\\{|\\}|\\\"", "").split(":")[1];
                            byte[] bytes = Base64.getDecoder().decode(base64String);
                            ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);
                            BufferedImage image = ImageIO.read(imageStream);             
                            float[][] pixelMatrix = getMatrixFromImage(image);

                            // GET LABEL
                            BigIntVector labelVector = (BigIntVector) consumerRoot.getVector("label");
                            Integer label = Long.valueOf(labelVector.get(i)).intValue();

                            ImageItem imageItem = new ImageItem(label, pixelMatrix);
                            batchResults[i] = imageItem;
                        }
                        processBatch.accept(batchResults);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        System.out.println();
        System.out.println("Total batch size: " + totalBatchSize);
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