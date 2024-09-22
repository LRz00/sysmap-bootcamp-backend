/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.repository;

import com.lrz.sysbackend.models.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lara
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserId(UUID id);  
    List<Transaction> findByValueGreaterThan(BigDecimal value);
    List<Transaction> findByValueLessThan(BigDecimal value);
    List<Transaction> findByPointsEarned(Integer points);
}
