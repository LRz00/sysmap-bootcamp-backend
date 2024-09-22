/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.DTOs.TransactionDTO;
import com.lrz.sysbackend.exceptions.MethodException;
import com.lrz.sysbackend.models.Transaction;
import com.lrz.sysbackend.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lara
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepo;
    private final UserService userService;

    private TransactionDTO toDTO(Transaction data) {
        TransactionDTO dto = TransactionDTO.builder().albumTitle(data.getAlbum().getName())
                .pointsEarned(data.getPointsEarned()).value(data.getAlbum().getValue())
                .userName(data.getUser().getName()).build();

        return dto;
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getMetrics() {
            log.info("Getting transactions metrics");
            List<Transaction> transactions = this.transactionRepo.findAll();

            List<TransactionDTO> response = transactions.stream().map(this::toDTO).collect(Collectors.toList());

            return response;
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getUserMetrics() {
        try {
            log.info("Getting transactions metrics");
            User user = this.userService.getUserByContext();

            List<Transaction> transactions = this.transactionRepo.findByUserId(user.getId());

            List<TransactionDTO> response = transactions.stream().map(this::toDTO).collect(Collectors.toList());

            return response;
        } catch (Exception e) {
            throw new MethodException("Failure while getting User metrics");
        }

    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getMetricsAboveFiftyValue() {
            log.info("Getting transactions metrics");
            List<Transaction> transactions = this.transactionRepo.findByValueGreaterThan(new BigDecimal("50.00"));

            List<TransactionDTO> response = transactions.stream().map(this::toDTO).collect(Collectors.toList());

            return response;

    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getMetricsBellowFiftyValue() {
        try {
            log.info("Getting transactions metrics");
            List<Transaction> transactions = this.transactionRepo.findByValueLessThan(new BigDecimal("50.00"));

            List<TransactionDTO> response = transactions.stream().map(this::toDTO).collect(Collectors.toList());

            return response;
        } catch (Exception e) {
            throw new MethodException("Failed getting metrics bellow 50 value");
        }

    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getAllSundayMetrics() {
        try {
            log.info("Getting transactions metrics");
            List<Transaction> transactions = this.transactionRepo.findByPointsEarned(25);

            List<TransactionDTO> response = transactions.stream().map(this::toDTO).collect(Collectors.toList());

            return response;
        } catch (Exception e) {
            throw new MethodException("Failure while getting sunday metrics");
        }

    }
}
