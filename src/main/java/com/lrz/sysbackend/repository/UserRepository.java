/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lrz.sysbackend.repository;

import com.lrz.sysbackend.models.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lara
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

    public Optional<User> findByEmail(String email);
    public Boolean existsByEmail(String email);
}
