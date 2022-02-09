package com.evolutivelabs.app.counter.kafka.controller;

import com.evolutivelabs.app.counter.CounterKafkaApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class StreamControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // TODO: stream search 有問題
//    @Test
    void getCount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/kafka/stream/box/counter/log/test");
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andReturn();
    }
}