/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import com.lrz.sysbackend.DTOs.SaveUserDTO;
import com.lrz.sysbackend.DTOs.UpdateUserDTO;
import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.models.Wallet;
import com.lrz.sysbackend.repository.UserRepository;
import com.lrz.sysbackend.DTOs.UserDetailsResponseDTO;
import com.lrz.sysbackend.exceptions.MethodException;
import com.lrz.sysbackend.exceptions.UserNotFoundException;
import com.lrz.sysbackend.exceptions.InvalidCredentialsException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lara
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserDetailsResponseDTO signUp(SaveUserDTO user) {
        if (this.repository.existsByEmail(user.getEmail())) {
            throw new InvalidCredentialsException("This email is alredy in use! Login to your account or try another email");
        }
        log.info("Creating user");
        User newUser = User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .name(user.getName())
                .build();

        log.info("Creating user's empty wallet");
        Wallet newWallet = createEmptyWalletForUser(newUser);

        newUser.setWallet(newWallet);

        log.info("Saving user");
        try {
            this.repository.save(newUser);
            return new UserDetailsResponseDTO(newUser.getId(), newUser.getEmail(), newUser.getName());
        } catch (Exception e) {
            throw new MethodException("Error saving user");
        }

    }

    public String signIn(SaveUserDTO user) {

        User u = this.repository.findByEmail(user.getEmail()).orElseThrow(() -> new InvalidCredentialsException("User with this Email not found"));
        if (!this.passwordEncoder.matches(user.getPassword(), u.getPassword())) {
            throw new InvalidCredentialsException("Incorrect password");
        }
        return this.jwtService.generateToken(u);
    }

    @Transactional(readOnly = true)
    public Page<UserDetailsResponseDTO> getAllUsers(Pageable pageable) {
        try {
            log.info("Fetching all users");
            Page<User> usersPage = this.repository.findAll(pageable);
            List<UserDetailsResponseDTO> dtoList;
            dtoList = usersPage.getContent().stream()
                    .map(user -> new UserDetailsResponseDTO(user.getId(), user.getEmail(), user.getName()))
                    .collect(Collectors.toList());

            return new PageImpl<>(dtoList, pageable, usersPage.getTotalElements());

        } catch (Exception e) {
            throw new MethodException("Error fetching users");
        }
    }

    @Transactional(readOnly = true)
    public UserDetailsResponseDTO getUserById(UUID id) {
        log.info("Searching for user of id: " + id.toString());
        Optional<User> user = this.repository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User by id" + id.toString() + "not found!");
        }

        log.info("User found!");

        return convertToDTO(user.get());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserDetailsResponseDTO updateUser(UpdateUserDTO newInfo) {
        log.info("Searching for user");
        Optional<User> user = this.repository.findById(newInfo.getId());

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        user.get().setEmail(newInfo.getEmail());
        user.get().setName(newInfo.getName());
                if(!(newInfo.getEmail().isEmpty()|| newInfo.getPassword().isBlank())){
                 user.get().setPassword(passwordEncoder.encode(newInfo.getPassword()));
   
        }
        return convertToDTO(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = this.repository.findByEmail(username);

        return optionalUser.map(users -> new org.springframework.security.core.userdetails.User(users.getEmail(), users.getPassword(), new ArrayList<GrantedAuthority>()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }

    private UserDetailsResponseDTO convertToDTO(User user) {
        return new UserDetailsResponseDTO(user.getId(), user.getEmail(), user.getName());
    }

    public Wallet createEmptyWalletForUser(User user) {
        return Wallet.builder()
                .balance(BigDecimal.ZERO)
                .points(0)
                .lastUpdated(LocalDateTime.now())
                .user(user)
                .build();
    }

    public User getUserByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = this.repository.findByEmail(email).get();

        return user;
    }
}
