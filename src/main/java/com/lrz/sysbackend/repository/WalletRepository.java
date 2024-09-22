/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import com.lrz.sysbackend.models.Wallet;
import com.lrz.sysbackend.models.User;
import org.springframework.stereotype.Repository;
        
/**
 *
 * @author lara
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID>{
    public Optional<Wallet> findByUser(User user);
}
