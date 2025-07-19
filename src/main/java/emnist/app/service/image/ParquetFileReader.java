package emnist.app.service.image;

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

import emnist.app.service.helper.FunctionHelper;
import emnist.app.service.helper.FunctionHelper.BiFunction;

import emnist.app.service.image.EmnistData.EmnistBatch;
import emnist.app.service.image.EmnistData.EmnistEnum;
import emnist.app.service.image.EmnistData.EmnistImage;

import java.util.function.Consumer;

public class ParquetFileReader {

    public void read(String uri, int numEpoch, EmnistEnum dataType, Consumer<EmnistData> processData) {
        final int MAX_BATCH_NUM = ((numEpoch * EmnistData.EPOCH_SIZE) / EmnistData.BATCH_SIZE);
        try (   BufferAllocator allocator = new RootAllocator();
                DatasetFactory datasetFactory = new FileSystemDatasetFactory(allocator, NativeMemoryPool.getDefault(), FileFormat.PARQUET, uri);
                Dataset dataset = datasetFactory.finish();
                Scanner scanner = dataset.newScan(new ScanOptions(EmnistData.BATCH_SIZE));
                ArrowReader reader = scanner.scanBatches();
                ArrowSchema consumerArrowSchema = ArrowSchema.allocateNew(allocator)) {

            // PRODUCER FILL CONSUMER SCHEMA STRUCTURE
            Data.exportSchema(allocator, reader.getVectorSchemaRoot().getSchema(), reader, consumerArrowSchema);

            // Consumer loads it as an empty vector schema root
            try (   CDataDictionaryProvider consumerDictionaryProvider = new CDataDictionaryProvider();
                    VectorSchemaRoot consumerRoot = Data.importVectorSchemaRoot(allocator, consumerArrowSchema, consumerDictionaryProvider)) {
                        
                EmnistBatch[] batches = new EmnistBatch[MAX_BATCH_NUM];
                
                int batchNum = 0;
                while (batchNum < MAX_BATCH_NUM && reader.loadNextBatch()) {
                    try (ArrowArray consumerArray = ArrowArray.allocateNew(allocator)) {

                        // PRODUCER EXPORT DATA TO "consumerRoot"
                        Data.exportVectorSchemaRoot(allocator, reader.getVectorSchemaRoot(), reader, consumerArray);

                        // PRODUCER IMPORT NEXT BATCH INTO "consumerRoot"
                        Data.importIntoVectorSchemaRoot(allocator, consumerArray, consumerRoot, consumerDictionaryProvider);

                        int rowCount = consumerRoot.getRowCount();
                        EmnistImage[] images = new EmnistImage[rowCount];
                        for (int i = 0; i < rowCount; i++) {
                            // GET IMAGE
                            String objectString = consumerRoot.getVector("image").getObject(i).toString();
                            String base64String = objectString.replaceAll("\\{|\\}|\\\"", "").split(":")[1];
                            byte[] bytes = Base64.getDecoder().decode(base64String);
                            ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);
                            BufferedImage bufferedImage = ImageIO.read(imageStream);             
                            float[][] pixelMatrix = getMatrixFromImage(bufferedImage);

                            // GET LABEL
                            BigIntVector labelVector = (BigIntVector) consumerRoot.getVector("label");
                            int label = Long.valueOf(labelVector.get(i)).intValue();

                            images[i] = new EmnistImage(label, pixelMatrix);
                        }
                        batches[batchNum] = new EmnistBatch(images);
                        batchNum++;
                    }
                }
                EmnistData emnistData = new EmnistData(dataType, batches);
                processData.accept(emnistData);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static float[][] getMatrixFromImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float[][] outputMatrix = new float[width][height];
        BiFunction<Integer, Integer> function = (x, y) -> { 
            int colorValue = image.getRGB(x, y);
            // NORMALIZE THE COLOR VALUE BETWEEN 0.0 AND 1.0
            outputMatrix[x][y] = ((colorValue >> 16 & 0xff)) / 255.0f;
        };
        FunctionHelper.executeFunction(width, height, function);
        return outputMatrix;
    }

}