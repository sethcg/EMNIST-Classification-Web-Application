package emnist.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import emnist.app.service.NetworkService;
import emnist.app.service.network.NetworkStats;
import jakarta.servlet.http.HttpServletResponse;

@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class NetworkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NetworkService networkService;

    @Test
    public void PostMappingOfPing() throws Exception {
        // ARRANGE
        final String applicationName = "name";
        final String applicationVersion = "version";
        
        // ACT
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/ping"))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        String contentType = result.getResponse().getContentType();

        // ASSERT
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(contentType);
        Assertions.assertTrue(content.contains(applicationName));
        Assertions.assertTrue(content.contains(applicationVersion));
        Assertions.assertTrue(result.getResponse().getStatus() == HttpServletResponse.SC_OK);
        Assertions.assertTrue(contentType.contains(MediaType.TEXT_PLAIN_VALUE));
    }

    @Test
    public void PostMappingOfTrain() throws Exception {
        // ARRANGE/ACT
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/train"));

        // ASSERT
        Mockito.verify(networkService, Mockito.times(1)).train();
    }

    @Test
    public void PostMappingOfTest() throws Exception {
        // ARRANGE/ACT
        mockMvc.perform(MockMvcRequestBuilders
            .post("/api/test"));

        // ASSERT
        Mockito.verify(networkService, Mockito.times(1)).test();
    }

    @Test
    public void PostMappingOfTrainingStats() throws Exception {
        // ARRANGE
        final int imageNum = 60000;
        final double accuracy = 93.15;
        final double loss = 0.14;
        final boolean hasNetwork = true;
        final NetworkStats networkStats = new NetworkStats(imageNum, accuracy, loss);

        Mockito.when(networkService
            .getTrainingStatistics())
            .thenReturn(networkStats.getMappedObject(hasNetwork));

        // ACT
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/trainingStats"))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        String contentType = result.getResponse().getContentType();

        // ASSERT
        Mockito.verify(networkService, Mockito.times(1))
            .getTrainingStatistics();

        Assertions.assertNotNull(content);
        Assertions.assertNotNull(contentType);
        Assertions.assertTrue(result.getResponse().getStatus() == HttpServletResponse.SC_OK);
        Assertions.assertTrue(content.contains(String.format("\"hasNetwork\":\"%s\"", Boolean.toString(hasNetwork))));
        Assertions.assertTrue(content.contains(String.format("\"imageNum\":\"%s\"", Integer.toString(imageNum))));
        Assertions.assertTrue(content.contains(String.format("\"accuracy\":\"%s\"", Double.toString(accuracy))));
        Assertions.assertTrue(content.contains(String.format("\"loss\":\"%s\"", Double.toString(loss))));
        Assertions.assertTrue(contentType.equals(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void PostMappingOfTestingStats() throws Exception {
        // ARRANGE
        final int imageNum = 10000;
        final double accuracy = 91.15;
        final double loss = 0.13;
        final boolean hasNetwork = true;
        final NetworkStats networkStats = new NetworkStats(imageNum, accuracy, loss);

        Mockito.when(networkService
            .getTestingStatistics())
            .thenReturn(networkStats.getMappedObject(hasNetwork));

        // ACT
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/testingStats"))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        String contentType = result.getResponse().getContentType();

        // ASSERT
        Mockito.verify(networkService, Mockito.times(1))
            .getTestingStatistics();

        Assertions.assertNotNull(content);
        Assertions.assertNotNull(contentType);
        Assertions.assertTrue(result.getResponse().getStatus() == HttpServletResponse.SC_OK);
        Assertions.assertTrue(content.contains(String.format("\"hasNetwork\":\"%s\"", Boolean.toString(hasNetwork))));
        Assertions.assertTrue(content.contains(String.format("\"imageNum\":\"%s\"", Integer.toString(imageNum))));
        Assertions.assertTrue(content.contains(String.format("\"accuracy\":\"%s\"", Double.toString(accuracy))));
        Assertions.assertTrue(content.contains(String.format("\"loss\":\"%s\"", Double.toString(loss))));
        Assertions.assertTrue(contentType.equals(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void PostMappingOfPredict() throws Exception {
        // ARRANGE
        final Integer prediction = 0;

        Mockito.when(networkService
            .predict(ArgumentMatchers.any()))
            .thenReturn(prediction);

        // ACT
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/predict")
            .content(new ObjectMapper().writeValueAsString(new float[28][28]))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        String content = result.getResponse().getContentAsString();
        String contentType = result.getResponse().getContentType();

        // ASSERT
        Mockito.verify(networkService, Mockito.times(1))
            .predict(ArgumentMatchers.any());

        Assertions.assertNotNull(content);
        Assertions.assertNotNull(contentType);
        Assertions.assertTrue(result.getResponse().getStatus() == HttpServletResponse.SC_OK);
        Assertions.assertTrue(content.equals(prediction.toString()));
        Assertions.assertTrue(contentType.equals(MediaType.APPLICATION_JSON_VALUE));
    }

}