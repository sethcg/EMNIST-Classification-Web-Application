package emnist.app;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import emnist.app.service.NetworkService;

@WebMvcTest(NetworkController.class)
public class NetworkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NetworkService networkService;

    @Test
    public void PostMappingOfPredict() throws Exception {
        // ARRANGE
        final Integer prediction = 0;
        String content = new ObjectMapper().writeValueAsString(new float[28][28]);

        Mockito.when(networkService
            .predict(ArgumentMatchers.any()))
            .thenReturn(prediction);

        // ACT
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
            .post("/api/predict")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        // ASSERT
        Mockito.verify(networkService, Mockito.times(1))
            .predict(ArgumentMatchers.any());

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.content().string(prediction.toString()));
    }
}