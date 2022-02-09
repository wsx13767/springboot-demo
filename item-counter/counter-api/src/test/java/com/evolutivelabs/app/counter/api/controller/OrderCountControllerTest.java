package com.evolutivelabs.app.counter.api.controller;

import com.evolutivelabs.app.counter.common.model.ordercounter.InBoxItems;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemKafkaLog;
import com.evolutivelabs.app.counter.database.mysql.entity.ItemLog;
import com.evolutivelabs.app.counter.database.mysql.repository.ItemLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootTest
@AutoConfigureMockMvc
class OrderCountControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemLogRepository itemLogRepository;

    void setMockBean() {
        Mockito.when(itemLogRepository.save(Mockito.any())).thenReturn(Mockito.any());
    }

    @Test
    void inBoxItems() throws Exception {
        InBoxItems inBoxItems = new InBoxItems();
        inBoxItems.setBoxId("test");
        inBoxItems.setBarcode("321321321");
        inBoxItems.setMultiple(1L);
        inBoxItems.setError(false);
        inBoxItems.setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/orderCounter/inBoxItems")
                .content(objectMapper.writeValueAsString(inBoxItems))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
//                .andExpect(status().is(200))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        response.setCharacterEncoding("UTF-8");
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }

}