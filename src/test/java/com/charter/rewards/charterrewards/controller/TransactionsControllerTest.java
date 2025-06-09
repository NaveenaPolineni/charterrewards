package com.charter.rewards.charterrewards.controller;

import com.charter.rewards.charterrewards.dto.RetailTransaction;
import com.charter.rewards.charterrewards.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(TransactionsController.class)
public class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private RetailTransaction transaction;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        transaction = new RetailTransaction(1L, 100L, LocalDate.now(), 120.00);
        objectMapper.registerModule(new JavaTimeModule());

    }

    @Test
    @DisplayName("Create a new transaction and return 201 Created")
    void testCreateTransaction() throws Exception {
        when(transactionService.createTransaction(any(RetailTransaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/customers/100/transactions/1"))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.customerId").value(100L));
    }

    @Test
    @DisplayName("Return 400 Bad Request when customerId is null in createTransaction")
    void testCreateTransactionValidationExceptionWhenCustomerIdIsNull() throws Exception {
        RetailTransaction invalidTransaction = new RetailTransaction(null, null, LocalDate.now(), 120.00);

        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTransaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get transactions by customer ID and return 200 OK")
    void testGetTransactionsByCustomerId() throws Exception {
        List<RetailTransaction> transactions = Arrays.asList(transaction);
        when(transactionService.getTransactionsByCustomerId(100L)).thenReturn(transactions);

        mockMvc.perform(get("/api/v1/customers/100/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value(1L))
                .andExpect(jsonPath("$[0].customerId").value(100L));
    }

    @Test
    @DisplayName("Delete a transaction by ID and return 200 OK")
    void testDeleteTransactionById() throws Exception {
        doNothing().when(transactionService).deleteTransactionById(1L);

        mockMvc.perform(delete("/api/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction with ID: 1 deleted successfully"));
    }
}
