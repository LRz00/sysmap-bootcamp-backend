/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.web;
import com.lrz.sysbackend.DTOs.WalletResponseDTO;
import com.lrz.sysbackend.DTOs.CreditDTO;
import com.lrz.sysbackend.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDateTime;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


/**
 *
 * @author lara
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    public void shouldAddCredit() throws Exception {
        Mockito.doNothing().when(walletService).addCredit(any(CreditDTO.class));

        String jsonRequest = "{\"amount\": 100}";

        mockMvc.perform(post("/wallet/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Sucessfuly added 100 amount"));
    }

    @Test
    public void shouldReturnWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletResponseDTO walletResponseDTO = new WalletResponseDTO(walletId, new BigDecimal("500"), 0, LocalDateTime.now());

        Mockito.when(walletService.getWallet(any(UUID.class))).thenReturn(walletResponseDTO);

        mockMvc.perform(get("/wallet/" + walletId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + walletId.toString() + "\", \"balance\": 500}"));
    }

}
