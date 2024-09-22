/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import com.lrz.sysbackend.DTOs.CreditDTO;
import com.lrz.sysbackend.exceptions.MethodException;
import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.models.Wallet;
import com.lrz.sysbackend.repository.WalletRepository;
import com.lrz.sysbackend.DTOs.WalletResponseDTO;
import com.lrz.sysbackend.exceptions.InvalidAmountException;
import com.lrz.sysbackend.exceptions.WalletNotFoundException;
import com.lrz.sysbackend.exceptions.UserNotFoundException;
import com.lrz.sysbackend.repository.UserRepository;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author lara
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WalletService {

    private final WalletRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;


    public void addCredit(CreditDTO amount) {
            log.info("Finding user.");
            User user = this.userService.getUserByContext();

            log.info("Finding User's wallet");
            Wallet wallet = this.repository.findByUser(user)
                    .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

            if (amount.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidAmountException("Invalid amount!");
            }

            log.info("Setting new ballance and update time");
            wallet.setBalance(wallet.getBalance().add(amount.getAmount()));
            wallet.setLastUpdated(LocalDateTime.now());

            log.info("Saving wallet to repository");
            this.repository.save(wallet);
    }
    
    public void debit(BigDecimal amount) { //to be called on purchase
    
            log.info("Finding user.");
            User user = this.userService.getUserByContext();

            log.info("Finding user's wallet");
            Wallet wallet = this.repository.findByUser(user)
                    .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidAmountException("Invalid amount!");
            }
            
            log.info("Setting new balance, points and update time");
            wallet.setBalance(wallet.getBalance().subtract(amount));
            wallet.setPoints(wallet.getPoints() + this.getPointsToAdd(LocalDateTime.now()));            
            wallet.setLastUpdated(LocalDateTime.now());

            this.repository.save(wallet);

       
    }
    
    public WalletResponseDTO getWallet(UUID id){
        log.info("Finding wallet by id" + id.toString());
        
        Wallet wallet = this.repository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet not found!"));
        
        WalletResponseDTO response = WalletResponseDTO.builder()
                .balance(wallet.getBalance())
                .id(wallet.getId())
                .lastUpdated(wallet.getLastUpdated())
                .points(wallet.getPoints())
                .build();
        
        return response;
    }
    
    public Integer getPointsToAdd(LocalDateTime date){
        log.info("Findig points by weekday");
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY:
                return 7; // valor específico para segunda-feira
            case TUESDAY:
                return 6; // valor específico para terça-feira
            case WEDNESDAY:
                return 2; // valor específico para quarta-feira
            case THURSDAY:
                return 10; // valor específico para quinta-feira
            case FRIDAY:
                return 15; // valor específico para sexta-feira
            case SATURDAY:
                return 20; // valor específico para sábado
            case SUNDAY:
                return 25; // valor específico para domingo
            default:
                throw new IllegalArgumentException("Invalid day of the week");
    }

}
}