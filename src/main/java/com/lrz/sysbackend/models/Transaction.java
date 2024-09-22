/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 *
 * @author lara
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions_data")
public class Transaction {
    @Id
    @UuidGenerator
    private UUID id;
    
    @Column(name = "value")
    private BigDecimal value;
    
    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    @Column(name="points_earned")
    private Integer pointsEarned;
    
    @OneToOne
    @JoinColumn(name="album_id")
    private Album album;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
