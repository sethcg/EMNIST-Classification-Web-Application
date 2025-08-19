package emnist.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import emnist.app.service.image.ParquetFileReader;
import emnist.app.service.network.Network;

@ExtendWith(MockitoExtension.class)
class NetworkServiceTests {

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
            ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(), ArgumentMatchers.any());

        // ACT
        networkService.train();

        // ASSERT
        Mockito.verify(network, Mockito.times(1)).reset();
        Mockito.verify(reader, Mockito.times(1)).read(
            ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void Test_CompletesWithNoErrors() throws Exception {     
        // ARRANGE
        Mockito.doNothing().when(reader).read(
            ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(), ArgumentMatchers.any());

        // ACT
        networkService.test();

        // ASSERT
        Mockito.verify(reader, Mockito.times(1)).read(
            ArgumentMatchers.anyString(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

}
