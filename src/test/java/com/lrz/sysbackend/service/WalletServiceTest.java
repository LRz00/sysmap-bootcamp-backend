/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.models.Wallet;
import com.lrz.sysbackend.repository.UserRepository;
import com.lrz.sysbackend.repository.WalletRepository;
import com.lrz.sysbackend.DTOs.CreditDTO;
import com.lrz.sysbackend.exceptions.WalletNotFoundException;
import com.lrz.sysbackend.DTOs.WalletResponseDTO;
import com.lrz.sysbackend.exceptions.InvalidAmountException;
import static org.mockito.Mockito.never;

/**
 *
 * @author lara
 */
@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserService userService;

    @Test
    public void testAddCredit_Success() {
        CreditDTO creditDTO = new CreditDTO(BigDecimal.valueOf(100));
        User user = new User();
        user.setId(UUID.randomUUID());
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(50));

        when(userService.getUserByContext()).thenReturn(user);
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        walletService.addCredit(creditDTO);

        assertEquals(BigDecimal.valueOf(150), wallet.getBalance()); // 50 + 100 = 150
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testAddCredit_WalletNotFound() {
        CreditDTO creditDTO = new CreditDTO(BigDecimal.valueOf(100));
        User user = new User();

                when(userService.getUserByContext()).thenReturn(user);

        when(walletRepository.findByUser(user)).thenReturn(Optional.empty());

        WalletNotFoundException thrown = assertThrows(WalletNotFoundException.class, () -> {
            walletService.addCredit(creditDTO);
        });

        assertEquals("Wallet not found", thrown.getMessage());
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    public void testAddCredit_InvalidAmount() {
        CreditDTO creditDTO = new CreditDTO(BigDecimal.ZERO);
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(50)); // saldo inicial

        when(userService.getUserByContext()).thenReturn(user);
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));

        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, () -> {
            walletService.addCredit(creditDTO);
        });

        assertEquals("Invalid amount!", thrown.getMessage());
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    public void testDebit_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setPoints(0);

        BigDecimal debitAmount = BigDecimal.valueOf(50);

        when(userService.getUserByContext()).thenReturn(user);
        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);

        walletService.debit(debitAmount);
        assertEquals(BigDecimal.valueOf(50), wallet.getBalance());
        assertTrue(wallet.getPoints() > 0);
        assertNotNull(wallet.getLastUpdated());
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testDebit_InvalidAmount() {
        User user = new User();

        when(userService.getUserByContext()).thenReturn(user);

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setPoints(0);

        when(walletRepository.findByUser(user)).thenReturn(Optional.of(wallet));
        InvalidAmountException thrown = assertThrows(InvalidAmountException.class, () -> {
            walletService.debit(BigDecimal.ZERO);
        });

        assertEquals("Invalid amount!", thrown.getMessage());
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    public void testGetWallet_Success() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));
        wallet.setPoints(50);
        wallet.setLastUpdated(LocalDateTime.now());

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletResponseDTO result = walletService.getWallet(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getId());
        assertEquals(BigDecimal.valueOf(100), result.getBalance());
        assertEquals(50, result.getPoints());
        assertEquals(wallet.getLastUpdated(), result.getLastUpdated());
    }

    @Test
    public void testGetWallet_WalletNotFound() {
        UUID walletId = UUID.randomUUID();

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        WalletNotFoundException thrown = assertThrows(WalletNotFoundException.class, () -> {
            walletService.getWallet(walletId);
        });
        assertEquals("Wallet not found!", thrown.getMessage());
    }
}
