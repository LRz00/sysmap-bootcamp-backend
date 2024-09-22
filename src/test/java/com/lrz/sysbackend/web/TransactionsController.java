/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.web;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.lrz.sysbackend.DTOs.TransactionDTO;
import com.lrz.sysbackend.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author lara
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TransactionsController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void testGetMetrics_Success() throws Exception {
        TransactionDTO transaction1 = new TransactionDTO("User1", new BigDecimal("100"), "Album 1", 10);
        TransactionDTO transaction2 = new TransactionDTO("User2", new BigDecimal("150"), "Album 2", 15);
        List<TransactionDTO> transactions = List.of(transaction1, transaction2);

        Mockito.when(transactionService.getMetrics()).thenReturn(transactions);

        mockMvc.perform(get("/transactions/metrics")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userName\":\"User1\", \"value\":100, \"albumTitle\":\"Album 1\", \"pointsEarned\":10},"
                        + "{\"userName\":\"User2\", \"value\":150, \"albumTitle\":\"Album 2\", \"pointsEarned\":15}]"));
    }
@Test
    public void testGetUserMetrics_Success() throws Exception {
        TransactionDTO transaction1 = new TransactionDTO("User1", new BigDecimal("100"), "Album 1", 10);
        List<TransactionDTO> userTransactions = List.of(transaction1);

        Mockito.when(transactionService.getUserMetrics()).thenReturn(userTransactions);

        mockMvc.perform(get("/transactions/user/metrics")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userName\":\"User1\", \"value\":100, \"albumTitle\":\"Album 1\", \"pointsEarned\":10}]"));
    }

    @Test
    public void testGetHighMetrics_Success() throws Exception {
        TransactionDTO transaction = new TransactionDTO("User1", new BigDecimal("200"), "Album 1", 20);
        List<TransactionDTO> highValueTransactions = List.of(transaction);

        Mockito.when(transactionService.getMetricsAboveFiftyValue()).thenReturn(highValueTransactions);

        mockMvc.perform(get("/transactions/metrics/high")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userName\":\"User1\", \"value\":200, \"albumTitle\":\"Album 1\", \"pointsEarned\":20}]"));
    }

    @Test
    public void testGetLowMetrics_Success() throws Exception {
        TransactionDTO transaction = new TransactionDTO("User1", new BigDecimal("30"), "Album 1", 5);
        List<TransactionDTO> lowValueTransactions = List.of(transaction);

        Mockito.when(transactionService.getMetricsBellowFiftyValue()).thenReturn(lowValueTransactions);

        mockMvc.perform(get("/transactions/metrics/low")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userName\":\"User1\", \"value\":30, \"albumTitle\":\"Album 1\", \"pointsEarned\":5}]"));
    }

    @Test
    public void testGetSundayMetrics_Success() throws Exception {
        TransactionDTO sundayTransaction = new TransactionDTO("User1", new BigDecimal("100"), "Album 1", 25);
        List<TransactionDTO> sundayTransactions = List.of(sundayTransaction);

        Mockito.when(transactionService.getAllSundayMetrics()).thenReturn(sundayTransactions);

        mockMvc.perform(get("/transactions/metrics/sunday-hits")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"userName\":\"User1\", \"value\":100, \"albumTitle\":\"Album 1\", \"pointsEarned\":25}]"));
    }
}
