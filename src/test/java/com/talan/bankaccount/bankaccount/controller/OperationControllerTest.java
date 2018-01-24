package com.talan.bankaccount.bankaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OperationController.class)
public class OperationControllerTest {

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private OperationDTO operationDTO;
    @Before
    public void initialize() {
        objectMapper = new ObjectMapper();
        operationDTO = new OperationDTO(123123123L, 1000D);

    }

    @Test
    public void depositMoney_validAccount_expect_depositSuccess() throws Exception {

        //given(operationService.deposit(anyObject())).willReturn(deposit);
        mockMvc.perform(MockMvcRequestBuilders.post("/deposit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("account.accountNumber").value(123123123L))
                .andExpect(jsonPath("account.balance").value(1000D))
                .andExpect(jsonPath("amount").value(1000D))
                .andExpect(jsonPath("type").value("DEPOSIT"));
    }
}
