package emnist.app.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;

import emnist.app.service.helper.FileManagement;
import emnist.app.service.image.ParquetFileReader;
import emnist.app.service.network.Network;
import emnist.app.service.network.NetworkStats;

@ExtendWith(MockitoExtension.class)
class NetworkServiceTests {

    private MockedStatic<FileManagement> mockFileManagement;

    @BeforeEach
    public void setUp() {
        mockFileManagement = Mockito.mockStatic(FileManagement.class);
        mockFileManagement.when(FileManagement::hasNetwork).thenReturn(true);
    }

    @AfterEach
    public void tearDown() {
        mockFileManagement.close();
    }

    @Mock
    private Network network;

    @Mock
    private ParquetFileReader reader;

    @InjectMocks
    private static NetworkService networkService;

    @Test
    public void Train_CompletesWithNoErrors() throws Exception {
        // ARRANGE
        Mockito.doNothing().when(network).reset();
        Mockito.doNothing().when(reader).read(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(),
                ArgumentMatchers.any());

        // ACT
        networkService.train();

        // ASSERT
        Mockito.verify(network, Mockito.times(1)).reset();
        Mockito.verify(reader, Mockito.times(1)).read(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void Test_CompletesWithNoErrors() throws Exception {
        // ARRANGE
        Mockito.doNothing().when(reader).read(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(),
                ArgumentMatchers.any());

        // ACT
        networkService.test();

        // ASSERT
        Mockito.verify(reader, Mockito.times(1)).read(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void Predict_ReturnsInt() throws Exception {
        // ARRANGE
        final int expectedResult = 0;
        Mockito.when(network.predict(ArgumentMatchers.any())).thenReturn(expectedResult);

        // ACT
        int result = networkService.predict(new float[28][28]);

        // ASSERT
        Mockito.verify(network, Mockito.times(1)).predict(ArgumentMatchers.any());
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    public void getTrainingStatistics_ReturnsHashMap() throws Exception {
        // ARRANGE
        Integer imageNum = 60000;
        Double accuracy = 93.2;
        Double loss = 0.04;

        mockFileManagement.when(() -> FileManagement.hasFile(ArgumentMatchers.any()))
                .thenReturn(true);
        mockFileManagement.when(() -> FileManagement.getSavedStats(ArgumentMatchers.any()))
                .thenReturn(new NetworkStats(imageNum, accuracy, loss));

        // ACT
        HashMap<String, String> result = networkService.getTrainingStatistics();

        // ASSERT
        Assertions.assertTrue(Boolean.parseBoolean(result.get("hasNetwork")));
        Assertions.assertEquals(imageNum, Integer.parseInt(result.get("imageNum")));
        Assertions.assertEquals(accuracy, Double.parseDouble(result.get("accuracy")));
        Assertions.assertEquals(loss, Double.parseDouble(result.get("loss")));
    }

    @Test
    public void getTestingStatistics_ReturnsHashMap() throws Exception {
        // ARRANGE
        Integer imageNum = 10000;
        Double accuracy = 92.2;
        Double loss = 0.07;

        mockFileManagement.when(() -> FileManagement.hasFile(ArgumentMatchers.any()))
                .thenReturn(true);
        mockFileManagement.when(() -> FileManagement.getSavedStats(ArgumentMatchers.any()))
                .thenReturn(new NetworkStats(imageNum, accuracy, loss));

        // ACT
        HashMap<String, String> result = networkService.getTestingStatistics();

        // ASSERT
        Assertions.assertTrue(Boolean.parseBoolean(result.get("hasNetwork")));
        Assertions.assertEquals(imageNum, Integer.parseInt(result.get("imageNum")));
        Assertions.assertEquals(accuracy, Double.parseDouble(result.get("accuracy")));
        Assertions.assertEquals(loss, Double.parseDouble(result.get("loss")));
    }

}
