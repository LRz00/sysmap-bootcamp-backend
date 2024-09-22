/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;   
import java.util.UUID;   

import com.lrz.sysbackend.models.Album;
import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.DTOs.TransactionDTO;
import com.lrz.sysbackend.exceptions.MethodException;
import com.lrz.sysbackend.models.Transaction;
import com.lrz.sysbackend.repository.TransactionRepository;


/**
 *
 * @author lara
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testGetMetrics() {
        Transaction transaction = new Transaction();
        transaction.setAlbum(new Album()); 
        transaction.setPointsEarned(10);
        transaction.getAlbum().setValue(new BigDecimal("60.00"));
        transaction.setUser(new User());
        when(transactionRepo.findAll()).thenReturn(Arrays.asList(transaction));

        List<TransactionDTO> result = transactionService.getMetrics();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("60.00", result.get(0).getValue().toString());
        assertEquals("10", result.get(0).getPointsEarned().toString());
    }
    
     @Test
    public void testGetUserMetrics() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Transaction transaction = new Transaction();
        transaction.setAlbum(new Album());
        transaction.setPointsEarned(10);
        transaction.getAlbum().setValue(new BigDecimal("60.00"));
        transaction.setUser(user);

        when(userService.getUserByContext()).thenReturn(user);
        when(transactionRepo.findByUserId(userId)).thenReturn(Arrays.asList(transaction));

        List<TransactionDTO> result = transactionService.getUserMetrics();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("60.00", result.get(0).getValue().toString());
        assertEquals("10", result.get(0).getPointsEarned().toString());
    }
    
    @Test
    public void testGetMetricsAboveFiftyValue() {
        Transaction transaction = new Transaction();
        transaction.setAlbum(new Album());
        transaction.setPointsEarned(10);
        transaction.getAlbum().setValue(new BigDecimal("60.00"));
        transaction.setUser(new User());

        when(transactionRepo.findByValueGreaterThan(new BigDecimal("50.00")))
            .thenReturn(Arrays.asList(transaction));

        List<TransactionDTO> result = transactionService.getMetricsAboveFiftyValue();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("60.00", result.get(0).getValue().toString());
        assertEquals("10", result.get(0).getPointsEarned().toString());
    }
}
